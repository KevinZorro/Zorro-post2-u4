# pedidos-comportamiento

Proyecto Spring Boot con cuatro patrones de comportamiento GoF:
**Chain of Responsibility**, **Command con Undo**, **Observer** y **Strategy**.

## Patrones Aplicados

### Observer — Eventos de Pedido (Spring ApplicationEvents)

`GestorPedidosService` publica eventos sin conocer a ningún suscriptor.
Cada suscriptor se registra automáticamente con `@EventListener`:
GestorPedidosService ──publica──▶ PedidoConfirmadoEvent
EmailNotifier &  InventarioUpdater & AuditoriaLogger


**Agregar un nuevo suscriptor** sin modificar nada existente:
```java
@Component
public class LogisticaNotifier {
    @EventListener
    public void onConfirmado(PedidoConfirmadoEvent e) {
        System.out.println("[LOGISTICA] Preparando envío para: " + e.getPedido().getId());
    }
}
```
Spring lo detecta automáticamente al arrancar la aplicación.

### Strategy — Motor de Descuentos

`CarritoService` recibe todas las estrategias por inyección de lista.
La estrategia activa se cambia en tiempo de ejecución:

| Estrategia           | Algoritmo               |
|----------------------|-------------------------|
| `DescuentoTemporada` | subtotal × 20%          |
| `DescuentoVIP`       | subtotal × 30%          |
| `DescuentoCupon`     | min(subtotal, $15,000)  |

**Agregar una nueva estrategia** sin modificar `CarritoService`:
```java
@Service
public class DescuentoEstudiante implements EstrategiaDescuento {
    public double calcular(double s) { return s * 0.15; }
    public String getNombre()        { return "Estudiante (15%)"; }
}
```

### Chain of Responsibility y Command
Ver Post-Contenido 1 (`pom.xml` compartido, paquetes `cor/` y `command/`).

## Requisitos
- Java 17+
- Maven 3.8+

## Ejecución

```bash
git clone https://github.com/KevinZorro/Zorro-post2-u4.git
cd Zorro-post2-u4
mvn clean package
mvn spring-boot:run
```

## Pruebas

```bash
mvn test
```

Resultado esperado: `BUILD SUCCESS` con todos los tests pasando.

## Salida de Consola Esperada (Observer)
OBSERVER — EVENTOS DE PEDIDO

[GESTOR] Pedido P-002 confirmado.
[EMAIL] Confirmación enviada para pedido: P-002
[INVENTARIO] Reserva de stock aplicada para: P-002
[AUDITORIA] 2026-04-16 21:00:00 — Pedido P-002 -> CONFIRMADO

[GESTOR] Pedido P-003 cancelado.
[EMAIL] Aviso de cancelación enviado para: P-003
[INVENTARIO] Stock liberado para: P-003
[AUDITORIA] 2026-04-16 21:00:00 — Pedido P-003 -> CANCELADO

### Chain of Responsibility — Cadena de Validación

Cada validación está encapsulada en un manejador independiente.
La cadena se construye con fluent API (`setNext()`):
ValidadorStock → ValidadorMonto → ValidadorCredito

| Validador          | Condición de rechazo                          |
|--------------------|-----------------------------------------------|
| `ValidadorStock`   | `cantidad > 10` (stock simulado)              |
| `ValidadorMonto`   | `total < $5,000`                              |
| `ValidadorCredito` | `creditoOk == false`                          |

Si un manejador rechaza el pedido, la cadena se detiene y no delega.

### Command con Undo — Operaciones Reversibles

Cada acción sobre el pedido se encapsula como un objeto `Comando`
que almacena el estado anterior para soportar `undo()`:

| Comando                   | execute()                  | undo()                        |
|---------------------------|----------------------------|-------------------------------|
| `ComandoConfirmar`        | estado → CONFIRMADO        | restaura estado anterior      |
| `ComandoAplicarDescuento` | total × (1 − porcentaje)   | restaura total anterior       |

`HistorialComandos` actúa como **Invoker** usando una pila `ArrayDeque`
(LIFO): `push` al ejecutar, `pop` al deshacer.

## Requisitos

- Java 17+
- Maven 3.8+

## Ejecución

```bash
git clone https://github.com/<usuario>/Zorro-post1-u4.git
cd Zorro-post1-u4
mvn clean package
mvn spring-boot:run
```

## Pruebas

```bash
mvn test
```

Resultado esperado: `BUILD SUCCESS` con 5 tests pasando.

## Salida de Consola Esperada

PEDIDO P-001 (válido)
[STOCK] OK: 3 unidades disponibles.
[MONTO] OK: total $45000.00 supera el mínimo.
[CREDITO] OK: crédito del cliente aprobado.
Resultado validación: true
[CMD] Pedido P-001 confirmado.
[CMD] Descuento 10% aplicado: $45000.00 → $40500.00
Estado actual: Pedido{id='P-001', estado='CONFIRMADO', total=40500.00}

--- Deshaciendo última acción (descuento) ---
[UNDO] Descuento revertido: $45000.00 restaurado
Estado después de undo: Pedido{id='P-001', estado='CONFIRMADO', total=45000.00}

--- Evidencias de Tests ---

<img width="686" height="512" alt="image" src="https://github.com/user-attachments/assets/4c573ff6-322b-43f2-be2d-44a7bbf23e5b" />
<img width="692" height="572" alt="image" src="https://github.com/user-attachments/assets/fcdd0f37-497d-439f-8adb-4d68a5af383d" />


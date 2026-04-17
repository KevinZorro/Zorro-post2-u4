package com.universidad.pedidos;

import com.universidad.pedidos.command.ComandoAplicarDescuento;
import com.universidad.pedidos.command.ComandoConfirmar;
import com.universidad.pedidos.command.HistorialComandos;
import com.universidad.pedidos.cor.ValidadorCredito;
import com.universidad.pedidos.cor.ValidadorMonto;
import com.universidad.pedidos.cor.ValidadorPedido;
import com.universidad.pedidos.cor.ValidadorStock;
import com.universidad.pedidos.modelo.Pedido;
import com.universidad.pedidos.observer.GestorPedidosService;
import com.universidad.pedidos.strategy.CarritoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PedidosApp implements CommandLineRunner {

    private final HistorialComandos    historial;
    private final GestorPedidosService gestor;
    private final CarritoService       carrito;

    public PedidosApp(HistorialComandos historial,
                      GestorPedidosService gestor,
                      CarritoService carrito) {
        this.historial = historial;
        this.gestor    = gestor;
        this.carrito   = carrito;
    }

    public static void main(String[] args) {
        SpringApplication.run(PedidosApp.class, args);
    }

    @Override
    public void run(String... args) {

        // ── Post-Contenido 1: Chain of Responsibility + Command ────────────
        System.out.println("\n══════════════════════════════════════");
        System.out.println("   CADENA DE VALIDACIÓN (CoR)");
        System.out.println("══════════════════════════════════════");
        ValidadorPedido cadena = new ValidadorStock();
        cadena.setNext(new ValidadorMonto()).setNext(new ValidadorCredito());

        Pedido p1 = new Pedido("P-001", "PROD-A", 3, 45_000.0, true);
        boolean ok = cadena.validar(p1);
        System.out.println("Resultado validación: " + ok);

        if (ok) {
            historial.ejecutar(new ComandoConfirmar(p1));
            historial.ejecutar(new ComandoAplicarDescuento(p1, 10));
            historial.deshacer();
            System.out.println("Estado tras undo: " + p1);
        }

        // ── Post-Contenido 2: Observer con Spring ApplicationEvents ────────
        System.out.println("\n══════════════════════════════════════");
        System.out.println("   OBSERVER — EVENTOS DE PEDIDO");
        System.out.println("══════════════════════════════════════");
        Pedido p2 = new Pedido("P-002", "PROD-B", 2, 60_000.0, true);
        gestor.confirmarPedido(p2);

        System.out.println();
        Pedido p3 = new Pedido("P-003", "PROD-C", 1, 20_000.0, true);
        gestor.cancelarPedido(p3);

        // ── Post-Contenido 2: Strategy — Motor de Descuentos ──────────────
        System.out.println("\n══════════════════════════════════════");
        System.out.println("   STRATEGY — MOTOR DE DESCUENTOS");
        System.out.println("══════════════════════════════════════");
        System.out.println("Estrategias disponibles: " + carrito.listarEstrategias());

        double subtotal = 100_000.0;
        System.out.println("\n[Subtotal base: $" + subtotal + "]");

        carrito.activarDescuento("Temporada");
        carrito.calcularTotal(subtotal);

        carrito.activarDescuento("VIP");
        carrito.calcularTotal(subtotal);

        carrito.activarDescuento("Cupon");
        carrito.calcularTotal(subtotal);

        System.out.println("\n══════════════════════════════════════");
        System.out.println("   FIN DE LA DEMO");
        System.out.println("══════════════════════════════════════");
    }
}
package com.universidad.pedidos.strategy;

import org.springframework.stereotype.Service;
import java.util.List;

// Context: delega el cálculo del descuento a la estrategia activa
// Spring inyecta automáticamente TODAS las implementaciones de EstrategiaDescuento
@Service
public class CarritoService {

    private final List<EstrategiaDescuento> estrategiasDisponibles;
    private EstrategiaDescuento estrategiaActiva;

    // Constructor injection: Spring detecta todos los @Service que implementan EstrategiaDescuento
    public CarritoService(List<EstrategiaDescuento> estrategias) {
        this.estrategiasDisponibles = estrategias;
        this.estrategiaActiva       = null;  // sin descuento por defecto
    }

    // Selecciona la estrategia activa por nombre (parcial, case-insensitive)
    public void activarDescuento(String nombre) {
        this.estrategiaActiva = estrategiasDisponibles.stream()
                .filter(e -> e.getNombre().toLowerCase().contains(nombre.toLowerCase())
                          || e.getClass().getSimpleName().toLowerCase()
                              .contains(nombre.toLowerCase()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Estrategia no encontrada: " + nombre));
        System.out.println("[CARRITO] Estrategia activada: " + estrategiaActiva.getNombre());
    }

    public double calcularTotal(double subtotal) {
        if (estrategiaActiva == null) {
            System.out.printf("[CARRITO] Sin descuento activo. Total: $%.2f%n", subtotal);
            return subtotal;
        }
        double descuento = estrategiaActiva.calcular(subtotal);
        double total     = subtotal - descuento;
        System.out.printf("[CARRITO] %s → Descuento: $%.2f | Total: $%.2f%n",
                estrategiaActiva.getNombre(), descuento, total);
        return total;
    }

    public List<String> listarEstrategias() {
        return estrategiasDisponibles.stream()
                .map(EstrategiaDescuento::getNombre)
                .toList();
    }
}
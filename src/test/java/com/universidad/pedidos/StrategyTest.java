package com.universidad.pedidos;

import com.universidad.pedidos.strategy.CarritoService;
import com.universidad.pedidos.strategy.DescuentoCupon;
import com.universidad.pedidos.strategy.DescuentoTemporada;
import com.universidad.pedidos.strategy.DescuentoVIP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StrategyTest {

    private CarritoService carrito;

    // Construye el contexto sin levantar el contexto Spring completo
    @BeforeEach
    void setUp() {
        carrito = new CarritoService(List.of(
                new DescuentoTemporada(),
                new DescuentoVIP(),
                new DescuentoCupon()
        ));
    }

    @Test
    @DisplayName("Descuento Temporada aplica 20% correctamente")
    void testDescuentoTemporadaVeintePercent() {
        carrito.activarDescuento("Temporada");
        assertEquals(80_000.0, carrito.calcularTotal(100_000.0), 0.01);
    }

    @Test
    @DisplayName("Descuento VIP aplica 30% correctamente")
    void testDescuentoVIPTreintaPercent() {
        carrito.activarDescuento("VIP");
        assertEquals(70_000.0, carrito.calcularTotal(100_000.0), 0.01);
    }

    @Test
    @DisplayName("Descuento Cupón aplica valor fijo de $15,000")
    void testDescuentoCuponFijo() {
        carrito.activarDescuento("Cupon");
        assertEquals(85_000.0, carrito.calcularTotal(100_000.0), 0.01);
    }

    @Test
    @DisplayName("Cambio de estrategia en tiempo de ejecución produce resultados distintos")
    void testCambioDeEstrategiaEnTiempoDeEjecucion() {
        carrito.activarDescuento("VIP");
        double conVip = carrito.calcularTotal(100_000.0);

        carrito.activarDescuento("Temporada");
        double conTemporada = carrito.calcularTotal(100_000.0);

        assertNotEquals(conVip, conTemporada,
                "Estrategias distintas deben producir totales distintos");
    }

    @Test
    @DisplayName("Nombre de estrategia inválido lanza IllegalArgumentException")
    void testEstrategiaInvalidaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.activarDescuento("NoExiste"));
    }
}
package com.universidad.pedidos.strategy;

import org.springframework.stereotype.Service;

// ConcreteStrategy 3: cupón con valor fijo de $15,000 (no puede superar el subtotal)
@Service
public class DescuentoCupon implements EstrategiaDescuento {

    private static final double VALOR_CUPON = 15_000.0;

    @Override
    public double calcular(double subtotal) {
        return Math.min(subtotal, VALOR_CUPON);
    }

    @Override
    public String getNombre() { return "Cupón (-$15.000)"; }
}
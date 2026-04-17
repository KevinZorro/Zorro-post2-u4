package com.universidad.pedidos.strategy;

import org.springframework.stereotype.Service;

// ConcreteStrategy 1: descuento de temporada del 20%
@Service
public class DescuentoTemporada implements EstrategiaDescuento {

    @Override
    public double calcular(double subtotal) {
        return subtotal * 0.20;
    }

    @Override
    public String getNombre() { return "Temporada (20%)"; }
}
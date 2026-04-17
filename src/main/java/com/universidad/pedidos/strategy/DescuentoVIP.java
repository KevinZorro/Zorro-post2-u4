package com.universidad.pedidos.strategy;

import org.springframework.stereotype.Service;

// ConcreteStrategy 2: descuento para clientes VIP del 30%
@Service
public class DescuentoVIP implements EstrategiaDescuento {

    @Override
    public double calcular(double subtotal) {
        return subtotal * 0.30;
    }

    @Override
    public String getNombre() { return "VIP (30%)"; }
}
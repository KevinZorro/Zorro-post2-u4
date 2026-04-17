package com.universidad.pedidos.strategy;

// Strategy interface: contrato para todos los algoritmos de descuento
public interface EstrategiaDescuento {
    double calcular(double subtotal);
    String getNombre();
}
package com.universidad.pedidos.observer;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Subscriber 3: registra cada cambio de estado del pedido con timestamp para auditoría
@Component
public class AuditoriaLogger {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @EventListener
    public void onConfirmado(PedidoConfirmadoEvent e) {
        System.out.println("[AUDITORIA] " + LocalDateTime.now().format(FMT)
                + " — Pedido " + e.getPedido().getId() + " -> CONFIRMADO");
    }

    @EventListener
    public void onCancelado(PedidoCanceladoEvent e) {
        System.out.println("[AUDITORIA] " + LocalDateTime.now().format(FMT)
                + " — Pedido " + e.getPedido().getId() + " -> CANCELADO");
    }
}
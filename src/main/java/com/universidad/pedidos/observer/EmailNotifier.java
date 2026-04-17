package com.universidad.pedidos.observer;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// Subscriber 1: reacciona a eventos de confirmación y cancelación con notificación por email
@Component
public class EmailNotifier {

    @EventListener
    public void onConfirmado(PedidoConfirmadoEvent e) {
        System.out.println("[EMAIL] Confirmación enviada para pedido: "
                + e.getPedido().getId());
    }

    @EventListener
    public void onCancelado(PedidoCanceladoEvent e) {
        System.out.println("[EMAIL] Aviso de cancelación enviado para: "
                + e.getPedido().getId());
    }
}
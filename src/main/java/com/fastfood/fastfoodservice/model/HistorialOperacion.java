package com.fastfood.fastfoodservice.model;

import lombok.Data;

@Data
public class HistorialOperacion {
    private String tipoOperacion;
    private Pedido pedidoAntes;
    private Pedido pedidoDespues;

    public HistorialOperacion() {}

    public HistorialOperacion(String tipoOperacion, Pedido pedidoAntes, Pedido pedidoDespues) {
        this.tipoOperacion = tipoOperacion;
        this.pedidoAntes = pedidoAntes;
        this.pedidoDespues = pedidoDespues;
    }
}

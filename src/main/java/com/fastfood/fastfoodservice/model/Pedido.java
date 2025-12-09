package com.fastfood.fastfoodservice.model;

import lombok.Data;

@Data
public class Pedido {
    private Integer id;
    private String nombreCliente;
    private String descripcion;
    private double monto;
    private String estado;

    public Pedido() {}

    public Pedido(Integer id, String nombreCliente, String descripcion, double monto, String estado) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.descripcion = descripcion;
        this.monto = monto;
        this.estado = estado;
    }
}

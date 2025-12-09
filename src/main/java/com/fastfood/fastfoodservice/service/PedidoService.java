package com.fastfood.fastfoodservice.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fastfood.fastfoodservice.datastructures.ArrayQueue;
import com.fastfood.fastfoodservice.datastructures.ArrayStack;
import com.fastfood.fastfoodservice.datastructures.syngleLinkedList.ListNode;
import com.fastfood.fastfoodservice.datastructures.syngleLinkedList.SyngleLinkedList;
import com.fastfood.fastfoodservice.model.HistorialOperacion;
import com.fastfood.fastfoodservice.model.Pedido;

@Service
public class PedidoService {
    private SyngleLinkedList listaPedidos;
    private ArrayQueue colaPedidos;
    private ArrayStack historial;

    private int ultimoId = 0;

    public PedidoService() {
        this.listaPedidos = new SyngleLinkedList();
        this.colaPedidos = new ArrayQueue();
        this.historial = new ArrayStack();
    }

    public Pedido registrarPedido(Pedido request) {
        if (request.getNombreCliente() == null || request.getNombreCliente().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío");
        }

        if (request.getDescripcion() == null || request.getDescripcion().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }

        if (request.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        Pedido nuevoPedido = new Pedido(
            ++ultimoId,
            request.getNombreCliente(),
            request.getDescripcion(),
            request.getMonto(),
            "REGISTRADO"
        );

        listaPedidos.add(nuevoPedido);

        colaPedidos.enqueue(nuevoPedido);

        historial.push(
            new HistorialOperacion(
                "CREAR",
                null,
                null
            )
        );

        return nuevoPedido;
    }

    public Pedido[] listarPedidos() {
        int size = listaPedidos.size();
        Pedido[] result = new Pedido[size];
        ListNode actual = listaPedidos.getHead();
        int contador = 0;

        while (actual != null) {
            Pedido p = actual.getData();
            
            result[contador] = p;

            contador++;
            actual = actual.getNext();
        }

        return result;
    }

    public Pedido encontrarPedido(int requestId) {
        Pedido pedido = listaPedidos.finById(requestId);

        if (pedido == null) {
            return null;
        }

        return pedido;
    }

    public Pedido cancelarPedido(int requestId) {
        Pedido pedido = listaPedidos.finById(requestId);

        if (pedido == null) {
            return null;
        }

        pedido.setEstado("CANCELADO");
        colaPedidos.removeById(requestId);

        historial.push(
            new HistorialOperacion(
                "CANCELAR",
                null,
                null
            )
        );

        return pedido;
    }

    public Pedido despacharPedido() {
        if(colaPedidos.isEmpty()){
            throw new IllegalStateException("No hay pedidos en cola.");
        }
        
        Pedido pedido = colaPedidos.dequeue();
        listaPedidos.finById(pedido.getId()).setEstado("DESPACHADO");

        historial.push(
            new HistorialOperacion(
                "DESPACHAR",
                null,
                null
            )
        );
        
        return pedido;
    }

    public Map<String, Object> mostrarEstadisticas() {
        int totalPedidos = 0;
        double totalMonto = listaPedidos.montoTotalRecursivo();
        int totalRegistrados = 0;
        int totalDespachados = 0;
        int totalCancelados = 0;

        ListNode actual = listaPedidos.getHead();

        while (actual != null) {
            Pedido p = actual.getData();
            totalPedidos++;

            switch (p.getEstado()) {
                case "REGISTRADO":
                    totalRegistrados++;
                    break;
                case "DESPACHADO":
                    totalDespachados++;
                    break;
                case "CANCELADO":
                    totalCancelados++;
                    break;
            }

            actual = actual.getNext();
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("totalPedidos", totalPedidos);
        resultado.put("totalMonto", totalMonto);
        resultado.put("totalRegistrados", totalRegistrados);
        resultado.put("totalDespachados", totalDespachados);
        resultado.put("totalCancelados", totalCancelados);

        return resultado;
    }

    public String calcularTotal() {
        double total = listaPedidos.montoTotalRecursivo();

        return "El total es de: " + total;
    }
}
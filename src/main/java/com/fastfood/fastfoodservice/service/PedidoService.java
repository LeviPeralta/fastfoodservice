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
                nuevoPedido
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
        colaPedidos.removeByIdFIFO(requestId);

        historial.push(
            new HistorialOperacion(
                "CANCELAR",
                new Pedido(
                    pedido.getId(),
                    pedido.getNombreCliente(),
                    pedido.getDescripcion(),
                    pedido.getMonto(),
                    "REGISTRADO"
                ),
                pedido
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
                new Pedido(
                    pedido.getId(),
                    pedido.getNombreCliente(),
                    pedido.getDescripcion(),
                    pedido.getMonto(),
                    "REGISTRADO"
                ),
                new Pedido(
                    pedido.getId(),
                    pedido.getNombreCliente(),
                    pedido.getDescripcion(),
                    pedido.getMonto(),
                    "DESPACHADO"
                )
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

    public HistorialOperacion rollback() {
        if (historial.isEmpty()) {
            return null;
        }

        // Obtener la última operación del historial
        HistorialOperacion op = historial.pop();

        Pedido antes = op.getPedidoAntes();
        Pedido despues = op.getPedidoDespues();

        switch (op.getTipoOperacion()) {

            case "CREAR":
                // Si se creó → borrar de lista y cola
                if (despues != null) {
                    listaPedidos.removeById(despues.getId());
                    colaPedidos.removeByIdFIFO(despues.getId());
                }
                break;

            case "CANCELAR":
                // Restauramos el estado previo
                if (antes != null) {
                    Pedido p = listaPedidos.finById(antes.getId());
                    if (p != null) {
                        p.setEstado(antes.getEstado());
                    }

                    // Si antes estaba REGISTRADO → regresarlo a la cola
                    if (antes.getEstado().equals("REGISTRADO")) {
                        colaPedidos.enqueue(p);
                    }
                }
                break;

            case "DESPACHAR":
                // Restauramos el estado previo y lo metemos a la cola
                if (antes != null) {
                    Pedido p = listaPedidos.finById(antes.getId());
                    if (p != null) {
                        p.setEstado(antes.getEstado());
                        colaPedidos.enqueue(p);
                    }
                }
                break;
        }

        return op; // <-- regreso objeto COMPLETO
    }

}
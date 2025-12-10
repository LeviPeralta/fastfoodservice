package com.fastfood.fastfoodservice.controller;

import java.util.HashMap;
import java.util.Map;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastfood.fastfoodservice.model.HistorialOperacion;
import com.fastfood.fastfoodservice.model.Pedido;
import com.fastfood.fastfoodservice.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class Controller {
     @Autowired
    private PedidoService service;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody Pedido pedido) {
        try{
            Pedido creado = service.registrarPedido(pedido);
            return ResponseEntity.ok(creado);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listAll() {
        try{
            return ResponseEntity.ok(service.listarPedidos());
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> searchById(@PathVariable int id) {
        Pedido pedido = service.encontrarPedido(id);

        if (pedido == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
        }

        return ResponseEntity.ok(pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelById(@PathVariable int id) {
        Pedido pedido = service.cancelarPedido(id);

        if (pedido == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Se borró con éxito el pedido.");
        response.put("pedido", pedido);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelar(@PathVariable("id") int id) {

        Pedido pedido = service.cancelarPedido(id);

        if (pedido == null) {
            return ResponseEntity
                    .badRequest()
                    .body("No se encontró el pedido con id: " + id);
        }

        // Construimos la respuesta JSON
        Map<String, Object> json = new HashMap<>();
        json.put("mensaje", "Pedido cancelado correctamente");
        json.put("pedido", pedido);

        return ResponseEntity.ok(json);
    }


    @PostMapping("/despachar")
    public ResponseEntity<?> dispatch() {
        try {
            Pedido pedido = service.despacharPedido();

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Se despachó con éxito el pedido.");
            response.put("pedido", pedido);

            return ResponseEntity.ok(response);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> stats() {
        return ResponseEntity.ok(service.mostrarEstadisticas());
    }

    @GetMapping("/total-recursivo")
    public ResponseEntity<?> total() {
        return ResponseEntity.ok(service.calcularTotal());
    }

    @PostMapping("/rollback")
    public ResponseEntity<?> rollback() {

        HistorialOperacion evento = service.rollback();

        if (evento == null) {
            return ResponseEntity.badRequest().body("No hay operaciones para deshacer.");
        }

        // pedido afectado = antes si existe, sino después
        Pedido afectado = evento.getPedidoAntes() != null
                ? evento.getPedidoAntes()
                : evento.getPedidoDespues();

        Map<String, Object> json = new HashMap<>();
        json.put("tipo", evento.getTipoOperacion());
        json.put("pedidoAfectado", afectado);

        return ResponseEntity.ok(json);
    }
}

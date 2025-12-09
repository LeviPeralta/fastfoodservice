package com.fastfood.fastfoodservice.datastructures.syngleLinkedList;

import com.fastfood.fastfoodservice.model.Pedido;

public class ListNode {
    Pedido data;
    ListNode next;

    public ListNode(Pedido data) {
        this.data = data;
        this.next = null;
    }

    public Pedido getData() {
        return data;
    }

    public ListNode getNext() {
        return next;
    }
}

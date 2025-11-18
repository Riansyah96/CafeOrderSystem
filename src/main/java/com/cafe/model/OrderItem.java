package com.cafe.model;

/** Item dalam order */
public class OrderItem {
    private MenuItem menu;
    private int qty;

    public OrderItem(MenuItem menu, int qty) {
        this.menu = menu;
        this.qty = qty;
    }

    public double subtotal() {
        return menu.getHarga() * qty;
    }

    public MenuItem getMenu() { return menu; }
    public int getQty() { return qty; }
}

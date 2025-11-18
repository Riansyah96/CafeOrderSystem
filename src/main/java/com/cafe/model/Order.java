package com.cafe.model;

import java.util.*;

public class Order {
    private UUID id = UUID.randomUUID();
    private User pemesan;
    private OrderStatus status = OrderStatus.DRAFT;

    private List<OrderItem> items = new ArrayList<>();

    public Order(User pemesan) {
        this.pemesan = pemesan;
    }

    public UUID getId() { return id; }
    public User getPemesan() { return pemesan; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public List<OrderItem> getItems() { return items; }

    public void addItem(MenuItem m, int qty) {
        items.add(new OrderItem(m, qty));
    }

    public double total() {
        return items.stream().mapToDouble(OrderItem::subtotal).sum();
    }

    public Struk cetakStruk() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== STRUK PEMBAYARAN ===\nOrder: ").append(id).append("\n");

        for (OrderItem i : items) {
            sb.append(i.getMenu().getNama())
              .append(" x ").append(i.getQty())
              .append(" = ").append(i.subtotal()).append("\n");
        }

        sb.append("TOTAL = ").append(total());

        return new Struk(UUID.randomUUID().toString(), new Date(), sb.toString());
    }
}

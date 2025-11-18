package com.cafe.system;

import com.cafe.model.*;

import java.util.*;

/**
 * Mekanisme utama pemrosesan order.
 */
public class CaffeSystem {

    private final Map<String, MenuItem> menu = new HashMap<>();

    public CaffeSystem() {
        // Inisialisasi menu default saat sistem dibuat
        tambahMenu(new MenuItem("Americano", 20000, "Coffee", true, "Hot black coffee"));
        tambahMenu(new MenuItem("Latte", 25000, "Coffee", true, "Milk coffee"));
        tambahMenu(new MenuItem("Espresso", 18000, "Coffee", true, "Shot of espresso"));
        tambahMenu(new MenuItem("Croissant", 15000, "Pastry", true, "Buttery pastry"));
    }

    public List<MenuItem> lihatMenu() {
        return new ArrayList<>(menu.values());
    }

    public void tambahMenu(MenuItem item) {
        menu.put(item.getNama(), item);
    }

    public Order buatOrder(User u) {
        return new Order(u);
    }

    public boolean tambahItem(Order o, String namaMenu, int qty) {
        MenuItem m = menu.get(namaMenu);
        if (m == null) {
            return false; // Menu tidak ditemukan
        }
        o.addItem(m, qty);
        return true;
    }

    public void kirimPesanan(Order o) {
        if (o.getItems().isEmpty()) {
            throw new IllegalStateException("Pesanan kosong, tidak bisa dikirim.");
        }
        o.setStatus(OrderStatus.SUBMITTED);
    }

    public void konfirmasiBarista(Order o, OrderStatus s) {
        if (s == OrderStatus.PREPARING || s == OrderStatus.READY) {
            o.setStatus(s);
        } else {
            throw new IllegalArgumentException("Barista hanya bisa set status ke PREPARING atau READY.");
        }
    }

    public void bayar(Order o) {
        if (o.getStatus() != OrderStatus.READY) {
            throw new IllegalStateException("Pesanan belum siap atau sudah dibayar.");
        }
        o.setStatus(OrderStatus.PAID);
    }
}
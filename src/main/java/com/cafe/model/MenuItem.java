package com.cafe.model;

import java.util.UUID;

/** Item menu (nama, harga, deskripsi, dll) */
public class MenuItem {
    private UUID id = UUID.randomUUID();
    private String nama;
    private double harga;
    private String kategori;
    private boolean tersedia;
    private String deskripsi;

    public MenuItem(String nama, double harga, String kategori, boolean tersedia, String deskripsi) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
        this.tersedia = tersedia;
        this.deskripsi = deskripsi;
    }

    public UUID getId() { return id; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
}

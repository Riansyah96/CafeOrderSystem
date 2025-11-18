package com.cafe.model;

import java.util.Date;

/** Representasi struk pembayaran */
public class Struk {
    private String id;
    private Date createdAt;
    private String text;

    public Struk(String id, Date createdAt, String text) {
        this.id = id;
        this.createdAt = createdAt;
        this.text = text;
    }

    public String getText() { return text; }
}

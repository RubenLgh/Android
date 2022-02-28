package com.laghouati.projet_laghouati;

public class Piece {
    private String name;
    private int id;
    private String picture;

    public Piece(String name, String picture, int id) {
        this.name = name;
        this.picture = picture;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public int getId() { return id; }
}

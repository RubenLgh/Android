package com.laghouati.projet_laghouati;

public class Peripherique {
    private String nom;
    private int id;
    private String type;
    private String picture;
    private int status;

    public Peripherique(String nom, int id, String type, String picture,int status ) {
        this.nom = nom;
        this.id = id;
        this.type = type;
        this.picture = picture;
        this.status = status;
    }

    public String getNom() {
        return nom;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getPicture() {
        return picture;
    }

    public int getStatus() {
        return status;
    }
}

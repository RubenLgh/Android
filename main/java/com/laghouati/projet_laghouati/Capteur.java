package com.laghouati.projet_laghouati;

public class Capteur {

    private String nom;
    private int id;
    private String type;
    private String picture;

    public Capteur(String nom, int id, String type, String picture) {
        this.nom = nom;
        this.id = id;
        this.type = type;
        this.picture = picture;
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
}

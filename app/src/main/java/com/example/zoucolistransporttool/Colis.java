package com.example.zoucolistransporttool;

public class Colis {
    private int id_colis;
    private String description;
    private int fragile;
    private int id_utilisateur;
    private int id_utilisateur_1;


    public Colis(int id_colis, String description, int fragile, int id_utilisateur, int id_utilisateur_1) {
        this.id_colis = id_colis;
        this.description = description;
        this.fragile = fragile;
        this.id_utilisateur = id_utilisateur;
        this.id_utilisateur_1 = id_utilisateur_1;
    }

    public Colis(int id_colis) {
        this.id_colis = id_colis;
    }
    public int getId_colis() {
        return id_colis;
    }

    public String getDescription() {
        return description;
    }

    public int getFragile() {
        return fragile;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public int getId_utilisateur_1() {
        return id_utilisateur_1;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId_colis(int id_colis) {
        this.id_colis = id_colis;
    }

    public void setFragile(int fragile) {
        this.fragile = fragile;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public void setId_utilisateur_1(int id_utilisateur_1) {
        this.id_utilisateur_1 = id_utilisateur_1;
    }
}


package com.example.zoucolistransporttool;

import java.util.ArrayList;
import java.util.List;

public class Livraison {
    private int id_livraison;
    private int domicile;
    private String date_depart;
    private String date_prevue;
    private String date_arrivee;
    private int id_utilisateur;
    private int id_colis;
    private List<Colis> listColis = new ArrayList<>();

    public List<Colis> getListColis() {
        return listColis;
    }

    public void setListColis(List<Colis> listColis) {
        this.listColis = listColis;
    }

    public Livraison(int id_livraison, int id_colis, int id_utilisateur, String date_arrivee, String date_prevue, String date_depart, int domicile) {
        this.id_livraison = id_livraison;
        this.id_colis = id_colis;
        this.id_utilisateur = id_utilisateur;
        this.date_arrivee = date_arrivee;
        this.date_prevue = date_prevue;
        this.date_depart = date_depart;
        this.domicile = domicile;
    }

    public Livraison() {
    }

    public int getId_livraison() {
        return id_livraison;
    }

    public void setId_livraison(int id_livraison) {
        this.id_livraison = id_livraison;
    }

    public int getDomicile() {
        return domicile;
    }

    public void setDomicile(int domicile) {
        this.domicile = domicile;
    }

    public String getDate_depart() {
        return date_depart;
    }

    public void setDate_depart(String date_depart) {
        this.date_depart = date_depart;
    }

    public String getDate_prevue() {
        return date_prevue;
    }

    public void setDate_prevue(String date_prevue) {
        this.date_prevue = date_prevue;
    }

    public String getDate_arrivee() {
        return date_arrivee;
    }

    public void setDate_arrivee(String date_arrivee) {
        this.date_arrivee = date_arrivee;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public int getId_colis() {
        return id_colis;
    }

    public void setId_colis(int id_colis) {
        this.id_colis = id_colis;
    }
}



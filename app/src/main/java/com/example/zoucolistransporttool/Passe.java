package com.example.zoucolistransporttool;

public class Passe {
    private int id_utilisateur;
    private int id_livraison;
    private int numero;

    public Passe(int id_utilisateur, int id_livraison, int numero) {
        this.id_utilisateur = id_utilisateur;
        this.id_livraison = id_livraison;
        this.numero = numero;
    }

    public Passe() {
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public int getId_livraison() {
        return id_livraison;
    }

    public void setId_livraison(int id_livraison) {
        this.id_livraison = id_livraison;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}

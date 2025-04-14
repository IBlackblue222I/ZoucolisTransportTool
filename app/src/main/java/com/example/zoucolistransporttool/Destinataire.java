package com.example.zoucolistransporttool;

public class Destinataire{

    private int id_utilisateur;

    public Destinataire(int id_utilisateur) {
        //super(id_utilisateur, tel, adresse, nom, prenom);
        this.id_utilisateur = id_utilisateur;
    }
    public Destinataire() {
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }
}

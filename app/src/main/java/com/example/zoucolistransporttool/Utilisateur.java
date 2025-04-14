package com.example.zoucolistransporttool;

public class Utilisateur {
    private int id_utilisateur;
    private String prenom;
    private String nom;
    private String adresse;
    private String tel;

    public Utilisateur(int id_utilisateur, String tel, String adresse, String nom, String prenom) {
        this.id_utilisateur = id_utilisateur;
        this.tel = tel;
        this.adresse = adresse;
        this.nom = nom;
        this.prenom = prenom;
    }

    //USED FOR INHERITANCE ONLY
    public Utilisateur() {
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}

package com.example.zoucolistransporttool;

public class Connexion {
    private int id_profil;
    private int id_utilisateur;

    public Connexion() {
    }
    public Connexion(int id_profil, int id_utilisateur) {
        this.id_profil = id_profil;
        this.id_utilisateur = id_utilisateur;
    }
    public Connexion(int id_profil) {
        this.id_profil = id_profil;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public int getId_profil() {
        return id_profil;
    }

    public void setId_profil(int id_profil) {
        this.id_profil = id_profil;
    }
}

package com.example.zoucolistransporttool;

public class Profil {
    private int id_profil;
    private String mail;
    private String mdp;

    public Profil(int id_profil, String mail) {
        this.id_profil = id_profil;
        this.mail = mail;
    }
    public Profil(String mail, String mdp) {
        this.mail = mail;
        this.mdp = mdp;
    }
    public Profil(int id_profil, String mail, String mdp) {
        this.id_profil = id_profil;
        this.mdp = mdp;
        this.mail = mail;
    }

    public int getId_profil() {
        return id_profil;
    }

    public void setId_profil(int id_profil) {
        this.id_profil = id_profil;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}

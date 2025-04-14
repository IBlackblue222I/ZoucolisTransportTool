package com.example.zoucolistransporttool;

import java.util.List;


import com.example.zoucolistransporttool.Colis;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiService {
    //example
    @GET("produits/list/")
    Call<List<Colis>> getColis();

    //connect
    @POST("profil/verifier.php")
    Call<Profil> getProfilEtVerif(@Body Profil profil);

    @POST("connexion/lire_un_profil.php")
    Call<Connexion> getConnexionDeId(@Body Connexion connexion);

    @POST("transporteur/lire_un.php")
    Call<Transporteur> getUnTransporteur(@Body Transporteur transporteur);

    @GET("livraison/lire_enCours.php")
    Call<List<Livraison>> getLivraisonsEnCours();
}


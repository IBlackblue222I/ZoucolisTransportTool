package com.example.zoucolistransporttool;

import java.util.List;
import java.util.Map;


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

    @POST("livraison/lire_enCours.php")
    Call<List<Livraison>> getLivraisonsEnCours(@Body Map<String, String> dateMap);

    @POST("livraison/lire_un.php")
    Call<Livraison> getUneLivraison(@Body Livraison livraison);

    @POST("livraison/modifier.php")
    Call<Livraison> modifierUneLivraison(@Body Livraison livraison);

    @POST("colis/lire_un.php")
    Call<Colis> getUnColis(@Body Colis colis);

    @POST("utilisateur/lire_un.php")
    Call<Utilisateur> getUnUtilisateur(@Body Utilisateur utilisateur);

    @POST("passe/lire_parLivraison.php")
    Call<List<Passe>> getPointsRelaisdeLivraison(@Body Map<String, Integer> livraison);
}


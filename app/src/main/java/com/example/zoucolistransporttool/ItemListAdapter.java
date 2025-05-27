package com.example.zoucolistransporttool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private List<Livraison> LivraisonList;
    ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

    public ItemListAdapter(List<Livraison> LivraisonList) {
        this.LivraisonList = LivraisonList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Livraison Livraison = LivraisonList.get(position);
        holder.livraison_id.setText(String.valueOf(Livraison.getId_livraison()));
        holder.date_prevue.setText(Livraison.getDate_prevue());
        if(Livraison.getColis() != null){
            if(Livraison.getColis().getFragile() == 1) {
                holder.fragile.setText("FRAGILE");
            } else {holder.fragile.setText("normal");}
            if(Livraison.getDomicile()==1) {
                holder.adresse.setText(Livraison.getDestinataire().getAdresse());
            } else {
                Map<String, Integer> livraison_item = new HashMap<>();
                livraison_item.put("id_livraison", Livraison.getId_livraison());
                Call<List<Passe>> call_p = apiService.getPointsRelaisdeLivraison(livraison_item);
                call_p.enqueue(new Callback<List<Passe>>() {
                    @Override
                    public void onResponse(Call<List<Passe>> call, Response<List<Passe>> response) {
                        if(response.isSuccessful() && response.body() != null ){
                            List<Passe> passes = response.body();
                            int maxi = 0;
                            int id_point_relais = 0;
                            for (Passe unePasse : passes) {
                                if(unePasse.getNumero() > maxi){
                                    maxi = unePasse.getNumero();
                                    id_point_relais = unePasse.getId_utilisateur();
                                }
                            }
                            Utilisateur utilisateur = new Utilisateur(id_point_relais);
                            Call<Utilisateur> call_u = apiService.getUnUtilisateur(utilisateur);
                            call_u.enqueue(new Callback<Utilisateur>() {
                                @Override
                                public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                                    if(response.isSuccessful() && response.body() != null){
                                        Utilisateur user_point_relais = response.body();
                                        holder.adresse.setText(user_point_relais.getAdresse());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Utilisateur> call, Throwable t) {

                                }
                            });

                        }

                    }

                    @Override
                    public void onFailure(Call<List<Passe>> call, Throwable t) {

                    }
                });

            }
            holder.dest_nom.setText(Livraison.getDestinataire().getNom());
            holder.dest_prenom.setText(Livraison.getDestinataire().getPrenom());
        }
        boolean isExpanded = Livraison.isExpanded();
        holder.extraInfo.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            Livraison.setExpanded(!Livraison.isExpanded());
            notifyItemChanged(position);
        });

        holder.btn_recept.setOnClickListener(v->{
            Livraison livraison_a_valider = new Livraison(Livraison.getId_livraison());
            Call<Livraison> call_lv = apiService.getUneLivraison(livraison_a_valider);

            call_lv.enqueue(new Callback<com.example.zoucolistransporttool.Livraison>() {
                @Override
                public void onResponse(Call<Livraison> call, Response<Livraison> response) {

                    if(response.isSuccessful() && response.body() != null){

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String today = sdf.format(calendar.getTime()); // ex: "2025-04-20"
                        //Toast.makeText(v.getContext(), ""+today, Toast.LENGTH_LONG).show();

                        Livraison livraison_reponse = new Livraison();
                        livraison_reponse = response.body();
                        livraison_reponse.setId_livraison(livraison_a_valider.getId_livraison());
                        livraison_reponse.setDate_arrivee(today);
                        //Toast.makeText(v.getContext(), "where is the issue1", Toast.LENGTH_LONG).show();
                        Call<Livraison> call_modLivraison = apiService.modifierUneLivraison(livraison_reponse);
                        call_modLivraison.enqueue(new Callback<com.example.zoucolistransporttool.Livraison>() {
                            @Override
                            public void onResponse(Call<Livraison> call, Response<Livraison> response) {
                                //Toast.makeText(v.getContext(), "where is the issue2", Toast.LENGTH_LONG).show();
                                if(response.isSuccessful()){
                                    Toast.makeText(v.getContext(), "Validé ! Arrivé le "+today, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<com.example.zoucolistransporttool.Livraison> call, Throwable t) {

                            }
                        });



                    }
                }

                @Override
                public void onFailure(Call<com.example.zoucolistransporttool.Livraison> call, Throwable t) {

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return LivraisonList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView livraison_id, date_prevue, fragile, adresse, dest_nom, dest_prenom;
        View extraInfo;
        Button btn_recept;

        public ViewHolder(View itemView) {
            super(itemView);
            livraison_id = itemView.findViewById(R.id.livraison_id);
            date_prevue = itemView.findViewById(R.id.date_prevue);
            fragile = itemView.findViewById(R.id.fragile);
            adresse = itemView.findViewById(R.id.adresse);
            dest_nom = itemView.findViewById(R.id.dest_nom);
            dest_prenom = itemView.findViewById(R.id.dest_prenom);
            extraInfo = itemView.findViewById(R.id.extra_info);

            btn_recept = itemView.findViewById(R.id.btn_recept);
        }
    }
}

package com.example.zoucolistransporttool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private List<Livraison> LivraisonList;

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
    }

    @Override
    public int getItemCount() {
        return LivraisonList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView livraison_id, date_prevue;

        public ViewHolder(View itemView) {
            super(itemView);
            livraison_id = itemView.findViewById(R.id.livraison_id);
            date_prevue = itemView.findViewById(R.id.date_prevue);
        }
    }
}

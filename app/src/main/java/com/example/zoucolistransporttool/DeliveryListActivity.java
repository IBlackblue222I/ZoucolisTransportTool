package com.example.zoucolistransporttool;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryListActivity extends AppCompatActivity {
    ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
    private RecyclerView recyclerView;

    Button btnSelectDate;
    private Calendar selectedDate;
    private int id_transporteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.deliverylist), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        Intent intent = getIntent();
        id_transporteur = intent.getIntExtra("id_transporteur", 0);

        //if ID exists, show list
        if(id_transporteur != 0){

            recyclerView = findViewById(R.id.rv_deliveries);

            //get Livraison/jour INCLUANT colis pour performance

            btnSelectDate = findViewById(R.id.btn_select_date); // Match this with your XML
            selectedDate  = Calendar.getInstance(); // default: today

            btnSelectDate.setOnClickListener(v -> {
                int year = selectedDate.get(Calendar.YEAR);
                int month = selectedDate.get(Calendar.MONTH);
                int day = selectedDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(
                        this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            // Update your date
                            selectedDate.set(selectedYear, selectedMonth, selectedDay);
                            // You can now format and use the date:
                            String formatted = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    .format(selectedDate.getTime());
                            TextView tv_selectedDate = findViewById(R.id.tv_selectedDate);
                            tv_selectedDate.setText(formatted); // Show selected date
                            // You can also trigger your API call with this date here
                        },
                        year, month, day
                );
                datePicker.show();
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            Utilisateur transporteur = new Utilisateur(id_transporteur);

            Call<List<Livraison>> call_L = apiService.getLivraisonsEnCours(transporteur);
            call_L.enqueue(new Callback<List<Livraison>>() {
                @Override
                public void onResponse(Call<List<Livraison>> call, Response<List<Livraison>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Livraison> livraisonList = response.body();

                        int totalLivraisons = livraisonList.size();
                        AtomicInteger completedCalls = new AtomicInteger(0);

                        for (Livraison livraison : livraisonList) {
//                            System.out.println(livraison.getId_livraison());
//                            System.out.println(livraison.getDate_prevue());
//                            System.out.println(livraison.getId_utilisateur());
//                            System.out.println(livraison.getId_colis());
                            Colis livraison_colis = new Colis(livraison.getId_colis());
                            Call<Colis> call_colis_L = apiService.getUnColis(livraison_colis);
                            call_colis_L.enqueue(new Callback<Colis>() {
                                @Override
                                public void onResponse(Call<Colis> call2, Response<Colis> response2) {
                                    if(response2.isSuccessful() && response2.body() != null) {
                                        Colis colis_dta = new Colis(livraison.getId_colis(), response2.body().getDescription(), response2.body().getFragile(), response2.body().getId_utilisateur(), response2.body().getId_utilisateur_1());
                                        livraison.setColis(colis_dta);

                                        Utilisateur destinataire = new Utilisateur(livraison.getColis().getId_utilisateur());
                                        Call<Utilisateur> call_dest = apiService.getUnUtilisateur(destinataire);

                                        call_dest.enqueue(new Callback<Utilisateur>() {
                                            @Override
                                            public void onResponse(Call<Utilisateur> call3, Response<Utilisateur> response3) {
                                                if(response3.isSuccessful() && response3.body() != null) {
                                                    destinataire.setTel(response3.body().getTel());
                                                    destinataire.setAdresse(response3.body().getAdresse());
                                                    destinataire.setNom(response3.body().getNom());
                                                    destinataire.setPrenom(response3.body().getPrenom());

                                                    livraison.setDestinataire(destinataire);

                                                    if (completedCalls.incrementAndGet() == totalLivraisons) {
                                                        // All colis fetched
                                                        runOnUiThread(() -> {
                                                            ItemListAdapter adapter = new ItemListAdapter(livraisonList);
                                                            recyclerView.setAdapter(adapter);
                                                        });

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Utilisateur> call, Throwable t) {

                                            }
                                        });
                                    }


                                }

                                @Override
                                public void onFailure(Call<Colis> call, Throwable t) {

                                }
                            });




                        }

//                        ItemListAdapter adapter = new ItemListAdapter(livraisonList);
//
//                        recyclerView.setAdapter(adapter);

                    }
                }

                @Override
                public void onFailure(Call<List<Livraison>> call, Throwable t) {
                    Toast.makeText(DeliveryListActivity.this, "Impossible de récupérer la liste des livraisons", Toast.LENGTH_LONG).show();
                }
            });
        }

    }


}
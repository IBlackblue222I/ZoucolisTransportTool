package com.example.zoucolistransporttool;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryListActivity extends AppCompatActivity {
    ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

    private RecyclerView recyclerView;
    private Button btnSelectDate;
    private Button btnRefresh;
    private Button btnDisconnect;
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

        // Initialize UI elements
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnDisconnect = findViewById(R.id.btn_disconnect);
        recyclerView = findViewById(R.id.rv_deliveries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        selectedDate = Calendar.getInstance(); // default: today
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(selectedDate.getTime());
        btnSelectDate.setText(todayDate.toString());

        btnSelectDate.setOnClickListener(v -> {
            int year = selectedDate.get(Calendar.YEAR);
            int month = selectedDate.get(Calendar.MONTH);
            int day = selectedDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);
                        String formatted = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(selectedDate.getTime());
                        btnSelectDate.setText(formatted);
                        // Optional: Call API again for selected date
                    },
                    year, month, day
            );
            datePicker.show();
        });

        btnRefresh.setOnClickListener(v -> {
            callGetLivraisons(); // Refresh deliveries
        });

        btnDisconnect.setOnClickListener(v -> {
            // TODO: Implement logout logic
            Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
            finish(); // or navigate back to login
        });

        callGetLivraisons(); // Initial load
    }

    protected void callGetLivraisons() {
        Intent intent = getIntent();
        id_transporteur = intent.getIntExtra("id_transporteur", 0);

        if (id_transporteur != 0) {
            Utilisateur transporteur = new Utilisateur(id_transporteur);
            Map<String, String> dateMap = new HashMap<>();
            dateMap.put("id_utilisateur", String.valueOf(id_transporteur));
            dateMap.put("date_depart", btnSelectDate.getText().toString());

            Call<List<Livraison>> call_L = apiService.getLivraisonsEnCours(dateMap);
            call_L.enqueue(new Callback<List<Livraison>>() {
                @Override
                public void onResponse(Call<List<Livraison>> call, Response<List<Livraison>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Livraison> livraisonList = response.body();
                        int totalLivraisons = livraisonList.size();
                        AtomicInteger completedCalls = new AtomicInteger(0);

                        for (Livraison livraison : livraisonList) {
                            Colis livraison_colis = new Colis(livraison.getId_colis());
                            Call<Colis> call_colis_L = apiService.getUnColis(livraison_colis);

                            call_colis_L.enqueue(new Callback<Colis>() {
                                @Override
                                public void onResponse(Call<Colis> call2, Response<Colis> response2) {
                                    if (response2.isSuccessful() && response2.body() != null) {
                                        Colis colis_dta = new Colis(
                                                livraison.getId_colis(),
                                                response2.body().getDescription(),
                                                response2.body().getFragile(),
                                                response2.body().getId_utilisateur(),
                                                response2.body().getId_utilisateur_1()
                                        );
                                        livraison.setColis(colis_dta);

                                        Utilisateur destinataire = new Utilisateur(colis_dta.getId_utilisateur());
                                        Call<Utilisateur> call_dest = apiService.getUnUtilisateur(destinataire);

                                        call_dest.enqueue(new Callback<Utilisateur>() {
                                            @Override
                                            public void onResponse(Call<Utilisateur> call3, Response<Utilisateur> response3) {
                                                if (response3.isSuccessful() && response3.body() != null) {
                                                    destinataire.setTel(response3.body().getTel());
                                                    destinataire.setAdresse(response3.body().getAdresse());
                                                    destinataire.setNom(response3.body().getNom());
                                                    destinataire.setPrenom(response3.body().getPrenom());

                                                    livraison.setDestinataire(destinataire);
                                                    if (completedCalls.incrementAndGet() == totalLivraisons) {
                                                        runOnUiThread(() -> {
                                                            ItemListAdapter adapter = new ItemListAdapter(livraisonList);
                                                            recyclerView.setAdapter(adapter);
                                                        });
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Utilisateur> call, Throwable t) {
                                                // Handle user fetch failure
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<Colis> call, Throwable t) {
                                    // Handle colis fetch failure
                                }
                            });
                        }
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

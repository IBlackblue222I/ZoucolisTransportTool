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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryListActivity extends AppCompatActivity {
    ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
    private RecyclerView recyclerView;
    private ItemListAdapter itemListAdapter;

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

            Call<List<Livraison>> call_L = apiService.getLivraisonsEnCours();
            call_L.enqueue(new Callback<List<Livraison>>() {
                @Override
                public void onResponse(Call<List<Livraison>> call, Response<List<Livraison>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Livraison> livraisonList = response.body();
                        for (Livraison livraison : livraisonList) {
                            System.out.println(livraison.getId_livraison());
                            System.out.println(livraison.getDate_prevue());
                            System.out.println(livraison.getId_utilisateur());
                            System.out.println(livraison.getId_colis());
                        }

                        ItemListAdapter adapter = new ItemListAdapter(livraisonList);

                        recyclerView.setAdapter(adapter);
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
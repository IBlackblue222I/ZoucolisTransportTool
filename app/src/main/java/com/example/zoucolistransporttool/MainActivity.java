package com.example.zoucolistransporttool;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zoucolistransporttool.Colis;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
    EditText editEmail, editPassword;
    Button btnValidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link UI components
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        btnValidate = findViewById(R.id.btn_validate);

        // Handle click
        btnValidate.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString();

            // You can now validate or use the data
            //Toast.makeText(this, "Email: " + email + "\nPassword: " + password, Toast.LENGTH_SHORT).show();
            Profil profil = new Profil(email, password);
            //Toast.makeText(MainActivity.this, "" + email + " " + password, Toast.LENGTH_LONG).show();
            Call<Profil> call_p = apiService.getProfilEtVerif(profil);

            call_p.enqueue(new Callback<Profil>() {
                @Override
                public void onResponse(Call<Profil> c, Response<Profil> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        //Toast.makeText(MainActivity.this, "Welcome " + response.body().getMail(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this, ""+response.body().getId_profil(), Toast.LENGTH_LONG).show();
                        Connexion connexion = new Connexion(response.body().getId_profil());
                        Call<Connexion> call_c = apiService.getConnexionDeId(connexion);
                        call_c.enqueue(new Callback<Connexion>() {
                            @Override
                            public void onResponse(Call<Connexion> c, Response<Connexion> response2) {
                                if (response2.isSuccessful() && response2.body() != null) {
                                    Toast.makeText(MainActivity.this, "Recherche de l'utilisateur...", Toast.LENGTH_LONG).show();
                                    Connexion body = response2.body();
                                    if (body != null) {
                                        //Log.d("DEBUG_CONNEXION", "id_utilisateur: " + body.getId_utilisateur());
                                        //Toast.makeText(MainActivity.this, "id_utilisateur: " + body.getId_utilisateur(), Toast.LENGTH_LONG).show();

                                        Transporteur transporteur = new Transporteur(response2.body().getId_utilisateur());
                                        //Toast.makeText(MainActivity.this, "CRASH ?", Toast.LENGTH_LONG).show();
                                        Call<Transporteur> call = apiService.getUnTransporteur(transporteur);
                                        call.enqueue(new Callback<Transporteur>() {
                                            @Override
                                            public void onResponse(Call<Transporteur> c, Response<Transporteur> response3) {
                                                if (response3.isSuccessful() && response3.body() != null) {

                                                    //NEW ACTIVITY - SHOW DELIVERIES BASED ON DATE

                                                    Toast.makeText(MainActivity.this, "You are a transporter", Toast.LENGTH_LONG).show();
                                                    Transporteur transporteur = response3.body();
                                                    Intent intent = new Intent(MainActivity.this, DeliveryListActivity.class);
                                                    intent.putExtra("id_transporteur", transporteur.getId_utilisateur());
                                                    startActivity(intent);
                                                }else {
                                                    Toast.makeText(MainActivity.this, "You are not a transporter", Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Transporteur> call, Throwable t) {
                                                Toast.makeText(MainActivity.this, "Something went wrong (transporter): " + t.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Connexion> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Something went wrong (connexion): " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Unknown login", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Profil> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Something went wrong: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

}
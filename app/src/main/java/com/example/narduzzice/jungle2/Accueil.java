package com.example.narduzzice.jungle2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Accueil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
    }

    public void ConnexionActivity(View v) {
        startActivity(new Intent(Accueil.this, LoginActivity.class));
    }

    public void InscriptionActivity(View v) {
        startActivity(new Intent(Accueil.this, RegistrationActivity.class));
    }
    public void DecouvrirActivity(View v) {
        startActivity(new Intent(Accueil.this, Decouvrir.class));
    }
}
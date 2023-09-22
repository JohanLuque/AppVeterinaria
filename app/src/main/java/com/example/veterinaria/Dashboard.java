package com.example.veterinaria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {
    Button btAbrirCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        loadUI();
        btAbrirCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActivity(Dashboard.class);
            }
        });
    }
    private void loadUI(){
        btAbrirCliente = findViewById(R.id.btAbrirCliente);
    }
    private void abrirActivity(Class variable){
        startActivity(new Intent(getApplicationContext(), variable));
    }
}
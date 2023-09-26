package com.example.veterinaria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {
    Button btAbrirMascota, btVerMascota;
    int idcliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        loadUI();
        Bundle parametros = this.getIntent().getExtras();
        if(parametros != null){
            idcliente = parametros.getInt("idcliente");
        };
        btAbrirMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrarMascota.class);
                intent.putExtra("idcliente", idcliente);
                startActivity(intent);
            }
        });
        btVerMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Listar.class);
                intent.putExtra("idcliente", idcliente);
                startActivity(intent);
            }
        });
    }
    private void loadUI(){
        btAbrirMascota = findViewById(R.id.btAbrirMascota);
        btVerMascota = findViewById(R.id.btVerMascota);
    }
}
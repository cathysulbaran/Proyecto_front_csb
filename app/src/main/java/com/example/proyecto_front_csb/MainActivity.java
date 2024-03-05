package com.example.proyecto_front_csb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Button btIniciarRegistro, btIniciarConsulta, btIniciarVentas, btIniciarInforme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btIniciarRegistro = findViewById(R.id.btRegistro);
        btIniciarConsulta = findViewById(R.id.btConsulta);




        btIniciarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciar_RegistrarProducto();
            }
        });

        btIniciarConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciar_ConsultaArticulo();
            }
        });




    }


    public void Iniciar_RegistrarProducto(){

        Intent intent = new Intent(this, RegistrarProducto.class);
        startActivity(intent);
    }

    public void Iniciar_ConsultaArticulo(){

        Intent intent = new Intent(this, ConsultaArticulo.class);
        startActivity(intent);
    }
}
package com.example.proyecto_front_csb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConsultaArticulo extends AppCompatActivity {

    private Button btBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_articulo);

        btBuscar = findViewById(R.id.btBuscar);


        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciar_DetallesArticulo();
            }
        });
    }

    public void Iniciar_DetallesArticulo(){

        Intent intent = new Intent(this, DetallesArticulo.class);
        startActivity(intent);
    }

}
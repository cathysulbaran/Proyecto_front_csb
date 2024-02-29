package com.example.proyecto_front_csb;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Ventas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);


        Button siguiente = findViewById(R.id.bt_siguiente);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ventas.this, ListadoProductos.class);
                startActivity(intent);
            }
        });


        Button atras = findViewById(R.id.bt_atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ventas.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
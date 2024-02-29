package com.example.proyecto_front_csb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetallesArticulo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_articulo);

        TextView textNombre = findViewById(R.id.textNombreValor);
        TextView textMarca = findViewById(R.id.textMarcaValor);
        TextView textPrecio = findViewById(R.id.textPrecioValor);

        textNombre.setText("Holaaaaaaaaaaaaaa");
        textMarca.setText("0000000000");
        textPrecio.setText("1512231");



    }
}
package com.example.proyecto_front_csb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.proyecto_front_csb.model.DataBase;

public class DetallesArticulo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_articulo);

        TextView textEan = findViewById(R.id.textEanValor);
        TextView textNombre = findViewById(R.id.textNombreValor);
        TextView textMarca = findViewById(R.id.textMarcaValor);
        TextView textPrecio = findViewById(R.id.textPrecioValor);
        TextView textStockDisponibleValor = findViewById(R.id.textStockDisponibleValor);
        TextView textFechaEntradaValor = findViewById(R.id.textFechaEntradaValor);


    }
}
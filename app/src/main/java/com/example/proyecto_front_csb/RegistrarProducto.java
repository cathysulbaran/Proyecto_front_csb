package com.example.proyecto_front_csb;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;

public class RegistrarProducto extends AppCompatActivity {
    private EditText edt_ean;
    private EditText edt_nombre;
    private EditText edt_marca;
    private EditText edt_stockDisp;
    private EditText edt_fecha;
    private EditText edt_fichaTenica;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);

        edt_ean = findViewById(R.id.edtEAN);
        edt_nombre = findViewById(R.id.edtNombre);
        edt_marca = findViewById(R.id.edtMarca);
        edt_stockDisp = findViewById(R.id.edtStockDisponible);
        edt_fecha = findViewById(R.id.edtFecha);
        edt_fichaTenica = findViewById(R.id.edtFichaTecnica);

    }
}
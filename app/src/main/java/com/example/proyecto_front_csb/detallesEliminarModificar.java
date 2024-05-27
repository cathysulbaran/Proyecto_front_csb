package com.example.proyecto_front_csb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyecto_front_csb.model.DataBase;
import com.example.proyecto_front_csb.model.Productos;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class detallesEliminarModificar extends AppCompatActivity {
    private EditText etEan, etNombre, etMarca, etPrecio, etFecha, etUnidades;
    private Button btModificar, btGuardar, btCancelar, btEliminar;
    private ImageView btAtras;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_eliminar_modificar);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String ean = intent.getStringExtra("ean");
        String nombre = intent.getStringExtra("nombre");
        String fichaTecnica = intent.getStringExtra("fichaTecnica");
        String marca = intent.getStringExtra("marca");
        double precio = intent.getDoubleExtra("precio", 0);
        int unidades = intent.getIntExtra("unidades",0);
        String entradaMercancia = intent.getStringExtra("entradaMercancia");


        etEan = findViewById(R.id.etEan);
        etNombre = findViewById(R.id.etNombre);
        etMarca = findViewById(R.id.etMarca);
        etUnidades = findViewById(R.id.etUnidades);
        etPrecio = findViewById(R.id.etPrecio);
        etFecha = findViewById(R.id.etFecha);
        btModificar = findViewById(R.id.btModificar);
        btEliminar = findViewById(R.id.btEliminar);
        btGuardar = findViewById(R.id.btGuardar);
        btAtras = findViewById(R.id.btAtras);
        btCancelar = findViewById(R.id.btCancelar);

        etEan.setText(ean);
        etNombre.setText(nombre);
        etMarca.setText(marca);
        etUnidades.setText(Integer.toString(unidades));
        String precioStr = String.valueOf(precio);
        etPrecio.setText(precioStr);
        etFecha.setText(entradaMercancia);

        btAtras.setOnClickListener(v -> atras());
        btEliminar.setOnClickListener(v -> eliminar(ean));
        btModificar.setOnClickListener(v -> modificar(ean));
        btGuardar.setOnClickListener(v -> guardar(ean, fichaTecnica, unidades));
        btCancelar.setOnClickListener(v -> cancelar(ean, nombre, marca, precio, entradaMercancia));
    }

    public void atras(){
        Intent intent = new Intent(this, EliminarModificar.class);
        startActivity(intent);
    }
    public void eliminar(String ean){
        DocumentReference productoRef = db.collection("Productos").document(ean);
        productoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(detallesEliminarModificar.this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(detallesEliminarModificar.this, EliminarModificar.class);
                startActivity(intent);
            }
        });
    }

    public void modificar(String ean){
        int colorClaro = ContextCompat.getColor(this, R.color.boton_claro);
        ColorStateList colorStateClaro = ColorStateList.valueOf(colorClaro);
        int colorOscuro = ContextCompat.getColor(this, R.color.boton_oscuro);
        ColorStateList colorStateOscuro = ColorStateList.valueOf(colorOscuro);

        btModificar.setEnabled(false);
        btModificar.setBackgroundTintList(colorStateClaro);
        btEliminar.setEnabled(false);
        btEliminar.setBackgroundTintList(colorStateClaro);
        btGuardar.setEnabled(true);
        btGuardar.setBackgroundTintList(colorStateOscuro);

        btAtras.setEnabled(false);
        btAtras.setVisibility(View.INVISIBLE);
        btCancelar.setEnabled(true);
        btCancelar.setVisibility(View.VISIBLE);

        etNombre.setEnabled(true);
        etMarca.setEnabled(true);
        etPrecio.setEnabled(true);
        etFecha.setEnabled(true);
        etUnidades.setEnabled(true);
    }

    public void guardar(String ean, String fichaTecnica, int unidades){
        String nombre = etNombre.getText().toString();
        String marca = etMarca.getText().toString();
        String precioStr = etPrecio.getText().toString();
        double precio = Double.parseDouble(precioStr);
        String unidadesStr = etUnidades.getText().toString();
        int unidadesFinal = Integer.parseInt(unidadesStr);
        String entradaMercancia = etFecha.getText().toString();

        Productos producto = new Productos(ean, nombre, fichaTecnica, marca, precio, unidadesFinal, entradaMercancia);
        DataBase db = new DataBase();
        db.modificarProductos(this, producto);

        int colorClaro = ContextCompat.getColor(this, R.color.boton_claro);
        ColorStateList colorStateClaro = ColorStateList.valueOf(colorClaro);
        int colorOscuro = ContextCompat.getColor(this, R.color.boton_oscuro);
        ColorStateList colorStateOscuro = ColorStateList.valueOf(colorOscuro);

        etNombre.setEnabled(false);
        etMarca.setEnabled(false);
        etPrecio.setEnabled(false);
        etFecha.setEnabled(false);
        etUnidades.setEnabled(false);

        btModificar.setEnabled(true);
        btModificar.setBackgroundTintList(colorStateOscuro);
        btEliminar.setEnabled(true);
        btEliminar.setBackgroundTintList(colorStateOscuro);
        btGuardar.setEnabled(false);
        btGuardar.setBackgroundTintList(colorStateClaro);
        btCancelar.setEnabled(false);
        btCancelar.setVisibility(View.INVISIBLE);
        btAtras.setEnabled(true);
        btAtras.setVisibility(View.VISIBLE);

    }

    public void cancelar(String ean, String nombre, String marca, double precio, String entradaMercancia){
        int colorClaro = ContextCompat.getColor(this, R.color.boton_claro);
        ColorStateList colorStateClaro = ColorStateList.valueOf(colorClaro);
        int colorOscuro = ContextCompat.getColor(this, R.color.boton_oscuro);
        ColorStateList colorStateOscuro = ColorStateList.valueOf(colorOscuro);

        etEan.setText(ean);
        etNombre.setText(nombre);
        etMarca.setText(marca);
        String precioStr = String.valueOf(precio);
        etPrecio.setText(precioStr);
        etFecha.setText(entradaMercancia);

        etNombre.setEnabled(false);
        etMarca.setEnabled(false);
        etPrecio.setEnabled(false);
        etFecha.setEnabled(false);

        btModificar.setEnabled(true);
        btModificar.setBackgroundTintList(colorStateOscuro);
        btEliminar.setEnabled(true);
        btEliminar.setBackgroundTintList(colorStateOscuro);
        btGuardar.setEnabled(false);
        btGuardar.setBackgroundTintList(colorStateClaro);
        btCancelar.setEnabled(false);
        btCancelar.setVisibility(View.INVISIBLE);
        btAtras.setEnabled(true);
        btAtras.setVisibility(View.VISIBLE);

    }
}
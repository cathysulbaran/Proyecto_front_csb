package com.example.proyecto_front_csb;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_front_csb.databinding.ActivityConsultaArticuloBinding;
import com.example.proyecto_front_csb.model.DataBase;
import com.example.proyecto_front_csb.model.Productos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Ventas extends AppCompatActivity {

    private EditText ean;
    private Button buscar;
    private Button volver;

    private RecyclerView recyclerView;
    private ProductosAdapter productoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        ean = findViewById(R.id.edtEAN); // Cambiar el id del EditText en el layout XML
        buscar = findViewById(R.id.btBuscar);
        volver = findViewById(R.id.btVolver);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eanFinal = ean.getText().toString();
                if (eanFinal.isEmpty()) {
                    ean.setError("Debes introducir el EAN");
                } else if (eanFinal.length() > 13) {
                    ean.setError("El EAN no puede tener más de 13 dígitos");
                } else {
                    consultaArticulo(eanFinal);
                }
            }
        });

        volver.setOnClickListener(v -> volver());
    }

    public void consultaArticulo(String ean) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productoRef = db.collection("Productos");

        productoRef.document(ean).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Productos producto = document.toObject(Productos.class);
                        List<Productos> productos = new ArrayList<>();
                        productos.add(producto);
                        productoAdapter = new ProductosAdapter(productos);
                        recyclerView.setAdapter(productoAdapter);
                    } else {
                        Toast.makeText(Ventas.this, "No se encontró el producto", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Ventas.this, "Error al buscar el producto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void volver() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

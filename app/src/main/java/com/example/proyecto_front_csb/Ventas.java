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
import android.widget.ImageView;
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
    private ImageView volver, verCarrito, buscar;

    private RecyclerView recyclerView;
    private Adaptador_ventas Adaptador_ventas;
    private List<Productos> productosSeleccionados; // Lista para almacenar productos seleccionados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        ean = findViewById(R.id.edtEAN);
        buscar = findViewById(R.id.btBuscar);
        volver = findViewById(R.id.btVolver);
        verCarrito = findViewById(R.id.btVerCarrito); // Inicializar el botón para ver el carrito
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productosSeleccionados = new ArrayList<>(); // Inicializar la lista de productos seleccionados

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

        verCarrito.setOnClickListener(v -> verCarrito());
    }

    private void agregarAlCarrito(Productos producto) {
        productosSeleccionados.add(producto);
        Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
    }
    private void AgregarCarritoConDialog(Productos producto) {
        // Verificar si hay stock disponible
        if (producto.getUnidades() > 0) {
            // Crea un diálogo para mostrar los detalles del artículo
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Detalles del Artículo");
            builder.setMessage("Nombre: " + producto.getNombre() + "\n" +
                    "Precio: " + producto.getPrecio() + "\n" +
                    "Stock Disponible: " + producto.getUnidades());

            builder.setPositiveButton("Agregar al Carrito", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    agregarAlCarrito(producto);
                    Adaptador_ventas = new Adaptador_ventas(productosSeleccionados);
                    recyclerView.setAdapter(Adaptador_ventas);
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else {
            Toast.makeText(this, "No hay suficiente stock disponible", Toast.LENGTH_SHORT).show();
        }
    }


    private void verCarrito() {
        Intent intent = new Intent(this, CarritoActivity.class);
        intent.putParcelableArrayListExtra("productosSeleccionados", (ArrayList<Productos>) productosSeleccionados);
        startActivity(intent);
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

                        // Verificar si hay suficiente stock disponible
                        if (producto.getUnidades() > 0) {
                            // Reducir el stock disponible
                            producto.setUnidades(producto.getUnidades() - 1);
                            // Agregar el producto al carrito
                            AgregarCarritoConDialog(producto);
                            // Actualizar la interfaz de usuario
                            Adaptador_ventas = new Adaptador_ventas(productosSeleccionados);
                            recyclerView.setAdapter(Adaptador_ventas);
                        } else {
                            Toast.makeText(Ventas.this, "No hay suficiente stock disponible", Toast.LENGTH_SHORT).show();
                        }
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


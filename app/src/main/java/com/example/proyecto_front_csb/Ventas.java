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
import com.example.proyecto_front_csb.databinding.ActivityVentasBinding;
import com.example.proyecto_front_csb.model.DataBase;
import com.example.proyecto_front_csb.model.Productos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.TotpMultiFactorAssertion;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Ventas extends AppCompatActivity {

    private EditText ean;
    private ImageView volver, verCarrito, buscar, btEAN;
    private RecyclerView recyclerView;
    private Adaptador_ventas Adaptador_ventas;
    ActivityVentasBinding binding;

    private BarcodeScannerHelper barcodeScannerHelper;
    private ArrayList<Productos> productosSeleccionados; // Lista para almacenar productos seleccionados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        binding = ActivityVentasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ean = findViewById(R.id.edtEAN);
        buscar = findViewById(R.id.btBuscar);
        volver = findViewById(R.id.btVolver);
        verCarrito = findViewById(R.id.btVerCarrito); // Inicializar el botón para ver el carrito
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btEAN = findViewById(R.id.btEAN);

        barcodeScannerHelper = new BarcodeScannerHelper(this);

        btEAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeScannerHelper.startScanner();
            }
        });

        productosSeleccionados = new ArrayList<>(); // Inicializar la lista de productos seleccionados
        Intent intent = getIntent();
        if(intent.hasExtra("productosSeleccionados")){
            productosSeleccionados = (ArrayList<Productos>) getIntent().getSerializableExtra("productosSeleccionados");
            Adaptador_ventas = new Adaptador_ventas(productosSeleccionados, Ventas.this);
            recyclerView.setAdapter(Adaptador_ventas);
        }

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eanFinal = ean.getText().toString();
                Toast.makeText(Ventas.this, "Cantidad: "+productosSeleccionados.size(), Toast.LENGTH_SHORT).show();
                if (eanFinal.isEmpty()) {
                    ean.setError("Debes introducir el EAN");
                } else if (eanFinal.length() > 13) {
                    ean.setError("El EAN no puede tener más de 13 dígitos");
                } else {
                    agregarAlRecicler(eanFinal);
                    //consultaArticulo(eanFinal);

                }
            }
        });

        volver.setOnClickListener(v -> volver());

        verCarrito.setOnClickListener(v -> verCarrito());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        barcodeScannerHelper.handleScanResult(data, new BarcodeScannerHelper.OnScanResultListener() {
            @Override
            public void onScanResult(String contents) {
                ean.setText(contents);
            }
        });
    }
    private boolean buscarProductosRepetidos(String ean, List<Productos> productos){
        boolean eanExiste = false;
        for(Productos producto : productos){
            if(producto.getEan().equals(ean)){
                eanExiste = true;
            }
        }
        return eanExiste;
    }
    private void agregarAlRecicler(String ean){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("Productos");
        DocumentReference dr = db.collection("Productos").document(ean);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    if(ds.exists()){
                        String fichaTecnica = ds.getString("FichaTecnica");
                        String marca = ds.getString("Marca");
                        String nombre = ds.getString("Nombre");
                        double precio = ds.getDouble("Precio");
                        int unidades = ds.getLong("Unidades").intValue();
                        String entrada = ds.getString("entradaMercancia");

                        if(unidades >0){
                            //Creamos un dialog para confirmar la compra del producto
                            AlertDialog.Builder builder = new AlertDialog.Builder(Ventas.this);
                            builder.setTitle("Detalles del Artículo");
                            builder.setMessage("Nombre: " + nombre + "\n" +
                                    "Precio: " + precio + "\n" +
                                    "Stock Disponible: " + unidades);
                            builder.setPositiveButton("Agregar al carrito", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Comprobamos que los productos no esten repetidos para que a la hora de importarlo al carrito no aparezcan dos veces
                                    if(!buscarProductosRepetidos(ean, productosSeleccionados)){
                                        Productos producto = new Productos(ean, nombre, fichaTecnica, marca, precio, 1, unidades, entrada);
                                        productosSeleccionados.add(producto);

                                    }else{
                                        for(Productos p : productosSeleccionados ){
                                            if(p.getEan().equals(ean)){
                                                p.setUnidades(p.getUnidades()+1);
                                            }
                                        }
                                    }
                                    Adaptador_ventas = new Adaptador_ventas(productosSeleccionados, Ventas.this);
                                    recyclerView.setAdapter(Adaptador_ventas);


                                }
                            });

                            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            //Mostramos el dialog en pantalla
                            builder.create().show();

                        }else{
                            Toast.makeText(Ventas.this, "No quedan unidades disponibles para realizar la compra", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(Ventas.this, "El producto no esta registrado en la base de datos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    private void verCarrito() {
        Intent intent = new Intent(this, CarritoActivity.class);
        intent.putExtra("productosSeleccionados", productosSeleccionados);
        startActivity(intent);
    }
    public void volver() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}


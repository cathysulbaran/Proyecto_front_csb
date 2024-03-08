package com.example.proyecto_front_csb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.proyecto_front_csb.model.Productos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EliminarModificar extends AppCompatActivity {

    private EditText nombre;
    private Button buscar;
    private Button volver;

    private RecyclerView recyclerView;
    private ProductosAdapter productoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_modificar);

        nombre = findViewById(R.id.edtNombreArticulo);
        buscar = findViewById(R.id.btBuscar);
        volver = findViewById(R.id.btVolver);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreFinal = nombre.getText().toString();
                if(nombreFinal.isEmpty()){
                    nombre.setError("Has de rellenar el campo en el buscador");
                }else{
                    consultaArticulo(nombreFinal);
                }
            }
        });

        volver.setOnClickListener(v -> voler());
    }

    public void consultaArticulo(String nombre){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productoRef = db.collection("Productos");
        Query productoEspecifico = productoRef.whereEqualTo("Nombre", nombre);

        productoEspecifico.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Productos> productos = new ArrayList<>();
                if(task.isSuccessful()){
                    if(!task.getResult().isEmpty()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            String ean = document.getId();
                            String nombre = document.getString("Nombre");
                            String fichaTecnica = document.getString("FichaTecnica");
                            String marca = document.getString("Marca");
                            double precio = document.getDouble("Precio");
                            double unidadesDouble = document.getDouble("Unidades");
                            int unidades = Double.valueOf(unidadesDouble).intValue();
                            String entradaMercancia = document.getString("entradaMercancia");

                            Productos producto = new Productos(ean,nombre,fichaTecnica,marca,precio,unidades,entradaMercancia);
                            productos.add(producto);
                        }
                    }else{
                        Query usuarioSimilares = productoRef.orderBy("Nombre").startAt(nombre).endBefore(nombre.substring(0, nombre.length()-1)+(char) (nombre.charAt(nombre.length()-1)+1));
                        usuarioSimilares.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        String ean = document.getId();
                                        String nombre = document.getString("Nombre");
                                        String fichaTecnica = document.getString("FichaTecnica");
                                        String marca = document.getString("Marca");
                                        double precio = document.getDouble("Precio");
                                        double unidadesDouble = document.getDouble("Unidades");
                                        int unidades = Double.valueOf(unidadesDouble).intValue();
                                        String entradaMercancia = document.getString("entradaMercancia");

                                        Productos producto = new Productos(ean,nombre,fichaTecnica,marca,precio,unidades,entradaMercancia);
                                        productos.add(producto);
                                    }
                                }else{
                                    System.out.println("No se encontraron campos validos");
                                }
                            }
                        });
                    }
                    productoAdapter = new ProductosAdapter(productos);
                    productoAdapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String ean = productos.get(recyclerView.getChildAdapterPosition(v)).getEan();
                            String nombre = productos.get(recyclerView.getChildAdapterPosition(v)).getNombre();
                            String fichaTecnica = productos.get(recyclerView.getChildAdapterPosition(v)).getFichaTecnica();
                            String marca = productos.get(recyclerView.getChildAdapterPosition(v)).getMarca();
                            String entradaMercancia = productos.get(recyclerView.getChildAdapterPosition(v)).getEntradaMercancia();
                            double precio = productos.get(recyclerView.getChildAdapterPosition(v)).getPrecio();
                            int unidades = productos.get(recyclerView.getChildAdapterPosition(v)).getUnidades();
                            Intent intent = new Intent(EliminarModificar.this, detallesEliminarModificar.class);
                            intent.putExtra("ean", ean);
                            intent.putExtra("nombre", nombre);
                            intent.putExtra("fichaTecnica", fichaTecnica);
                            intent.putExtra("marca", marca);
                            intent.putExtra("precio", precio);
                            intent.putExtra("unidades", unidades);
                            intent.putExtra("entradaMercancia", entradaMercancia);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(productoAdapter);
                }else {
                    System.out.println("Fallo la conexion");
                }
            }
        });

    }

    public void voler(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
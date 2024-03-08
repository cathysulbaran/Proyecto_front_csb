package com.example.proyecto_front_csb.model;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.proyecto_front_csb.DetallesArticulo;
import com.example.proyecto_front_csb.MainActivity;
import com.example.proyecto_front_csb.ProductosAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class DataBase {
    private FirebaseFirestore db;
    public DataBase() {
        db = FirebaseFirestore.getInstance();
    }
    public void insertarProductos(Context context, Productos producto){
            Map<String, Object> map = new HashMap<>();
            String nombre = producto.getNombre();
            double precio = producto.getPrecio();
            String fichaTecnica = producto.getFichaTecnica();
            String marca = producto.getMarca();
            int unidades = producto.getUnidades();
            String fecha = producto.getEntradaMercancia();
            String Ean = producto.getEan();;

            map.put("Nombre",nombre);
            map.put("Precio",precio);
            map.put("FichaTecnica", fichaTecnica);
            map.put("Marca", marca);
            map.put("Unidades", unidades);
            map.put("entradaMercancia", fecha);

            db.collection("Productos").document(Ean).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    Toast.makeText(context,"El producto ya existe",Toast.LENGTH_SHORT).show();
                                }else{
                                    db.collection("Productos").document(Ean).set(map);
                                    Toast.makeText(context,"Producto creado correctamente",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(context, "Error al verificar la existencia del producto", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //En caso de que fall el insert
                            Toast.makeText(context, "Error al insertar el producto a la base de datos", Toast.LENGTH_SHORT).show();
                        }
                    });
    }
    public void leerProductosPorEan(Context context, String ean, final TextView eanTextView, final TextView nombreTextView, final TextView marcaTextView, final TextView precioTextView, final TextView stockTextView, final TextView fechaTextView){
        eanTextView.setText(ean);
        db.collection("Productos").document(ean).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                String nombre = document.getString("Nombre");
                                String marca = document.getString("Marca");
                                String fichaTecnica = document.getString("FichaTecnica");
                                double precioDouble = document.getDouble("Precio");
                                String precio = Double.toString(precioDouble);
                                double unidadesDouble = document.getDouble("Unidades");
                                String unidades = Double.toString(unidadesDouble);
                                String fecha = document.getString("entradaMercancia");
                                nombreTextView.setText(nombre);
                                marcaTextView.setText(marca);
                                precioTextView.setText(precio);
                                stockTextView.setText(unidades);
                                fechaTextView.setText(fecha);
                            }
                        }else {
                            Toast.makeText(context, "Error al visualizar el producto", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error al visualizar el producto", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Fragmento de codigo que se usara tanto para la activitie de consulta de todos los articulos como en
    //caso de filto vacio en la busqueda por filtrado
    public void consultarTodosArticulos(){
        db.collection("Productos").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                             if(task.isSuccessful()){
                                 List<Productos> productos = new ArrayList<>();
                                 for(QueryDocumentSnapshot document : task.getResult()){
                                     String ean, nombre, fichaTecnica, marca, entradaMercancia, precioStr, unidadesStr;

                                     ean = document.getId();
                                     nombre = document.getString("Nombre");
                                     marca = document.getString("Marca");
                                     fichaTecnica = document.getString("FichaTecnica");
                                     entradaMercancia = document.getString("entradaMercancia");
                                     precioStr = document.getString("Precio");
                                     unidadesStr = document.getString("Unidades");
                                     double precio = Double.parseDouble(precioStr);
                                     int unidades = Integer.parseInt(unidadesStr);
                                     Productos producto = new Productos(ean, nombre, fichaTecnica, marca, precio, unidades, entradaMercancia);
                                     productos.add(producto);
                                 }
                             }
                    }
                });
    }

    public void modificarProductos(Context context, Productos producto){
        Map<String, Object> map = new HashMap<>();
        map.put("Nombre", producto.getNombre());
        map.put("FichaTecnica", producto.getFichaTecnica());
        map.put("Marca", producto.getMarca());
        map.put("Precio", producto.getPrecio());
        map.put("Unidades", producto.getUnidades());
        map.put("entradaMercancia", producto.getEntradaMercancia());

        db.collection("Productos").document(producto.getEan()).update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "El producto ha sido actualizado correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error al actualizar el producto en la base de datos", Toast.LENGTH_SHORT).show();
                    }
                });
    }






}

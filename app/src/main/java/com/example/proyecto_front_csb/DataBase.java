package com.example.proyecto_front_csb;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    public void insertarProductos(Context context, String Ean, String nombre, double precio, String fichaTecnica, String marca, int unidades, String fecha){
            Map<String, Object> map = new HashMap<>();
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
    //Por aqui estoy
    /*public List<String> obtenerMarcas(){
        Set<String> marcas = new HashSet<>();
        db.collection("Productos").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            for(DocumentSnapshot document : task.getResult()){
                                String marca = document.getString("Marca");
                                if(marca!=null){
                                    marcas.add(marca);
                                }
                            }
                        }
                    }
                });
        List <String> marcasFinal = new ArrayList<>(marcas);
        for(String marca : marcasFinal){
            System.out.println(marca);
        }
        return marcasFinal;
    }
    public int leerCantidad(String Ean) {
        final int[] cantidad = {0};

        db.collection("Productos").document(Ean).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                cantidad[0] = document.getLong("Unidades").intValue();
                                System.out.println(cantidad[0]);
                            } else {
                                //El documento no existe
                                System.out.println("no exite");
                            }
                        } else {
                            //Error al obtener el documento
                            System.out.println("Error");
                        }

                    }
                });
        return cantidad[0];
    }

    public void modificarDatos(){

    }

    public void eliminarProducto(String Ean, int cantidadVendida){
        int cantidad = leerCantidad(Ean);
        int cantidadFinal = cantidad - cantidadVendida;
        Map<String, Object> map = new HashMap<>();
        map.put("Unidades", cantidadFinal);

        DocumentReference docRef = db.collection("Productos").document(Ean);
        docRef.set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //En caso de que funcione el insert correctamente
                        System.out.println("Insertado con exito");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //En caso de que fall el insert
                        System.out.println("error");
                    }
                });
    }*/
}

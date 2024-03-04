package com.example.proyecto_front_csb.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Productos {
    private String ean;
    private String nombre;
    private String fichaTecnica;
    private String marca;
    private double precio;
    private int unidades;
    private String entradaMercancia;

    private FirebaseFirestore db;

    public Productos(String  ean, String nombre, String fichaTecnica, String marca, double precio, int unidades, String entradaMercancia) {
        this.ean = ean;
        this.nombre = nombre;
        this.fichaTecnica = fichaTecnica;
        this.marca = marca;
        this.precio = precio;
        this.unidades = unidades;
        this.entradaMercancia = entradaMercancia;
        db = FirebaseFirestore.getInstance();
    }

    public Productos(){
        db = FirebaseFirestore.getInstance();
    }
    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFichaTecnica() {
        return fichaTecnica;
    }

    public void setFichaTecnica(String fichaTecnica) {
        this.fichaTecnica = fichaTecnica;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public String getEntradaMercancia() {
        return entradaMercancia;
    }

    public void setEntradaMercancia(String entradaMercancia) {
        this.entradaMercancia = entradaMercancia;
    }
}

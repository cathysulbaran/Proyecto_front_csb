package com.example.proyecto_front_csb.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Productos implements Serializable {
    private String EAN;
    private String Nombre;
    private String FichaTecnica;
    private String Marca;
    private double Precio;
    private int Unidades;

    private int unidadesTotales;
    private String entradaMercancia;
    private boolean isSelected;


    public Productos(String  ean, String nombre, String fichaTecnica, String marca, double precio, int unidades, int unidadesTotales, String entradaMercancia) {
        this.EAN = ean;
        this.Nombre = nombre;
        this.FichaTecnica = fichaTecnica;
        this.Marca = marca;
        this.Precio = precio;
        this.Unidades = unidades;
        this.unidadesTotales = unidadesTotales;
        this.entradaMercancia = entradaMercancia;
        this.isSelected = false;
    }

    public Productos(String EAN, String nombre, String fichaTecnica, String marca, double precio, int unidadesTotales, String entradaMercancia) {
        this.EAN = EAN;
        Nombre = nombre;
        FichaTecnica = fichaTecnica;
        Marca = marca;
        Precio = precio;
        this.unidadesTotales = unidadesTotales;
        this.entradaMercancia = entradaMercancia;
    }

    public Productos(String nombre, double precio, int unidades) {

        this.Nombre = nombre;
        this.Precio = precio;
        this.Unidades = unidades;
        this.isSelected = false;
    }

    public Productos() {
        // Aquí puedes inicializar valores predeterminados si es necesario
    }
    public String getEan() {
        return EAN;
    }

    public void setEan(String ean) {
        this.EAN = ean;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getFichaTecnica() {
        return FichaTecnica;
    }

    public void setFichaTecnica(String fichaTecnica) {
        this.FichaTecnica = fichaTecnica;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        this.Marca = marca;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        this.Precio = precio;
    }

    public int getUnidades() {
        return Unidades;
    }

    public void setUnidades(int unidades) {
        this.Unidades = unidades;
    }

    public String getEntradaMercancia() {
        return entradaMercancia;
    }

    public void setEntradaMercancia(String entradaMercancia) {
        this.entradaMercancia = entradaMercancia;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getUnidadesTotales() {
        return unidadesTotales;
    }

    public void setUnidadesTotales(int unidadesTotales) {
        this.unidadesTotales = unidadesTotales;
    }

    // Método necesario para Parcelable
    protected Productos(Parcel in) {
        Nombre = in.readString();
        Precio = in.readDouble();
        // Leer otros campos si existen
    }

    public static Productos DocumentSnapshot(QueryDocumentSnapshot document) {

        String nombre = document.getString("Nombre");
        double precio = document.getDouble("Precio");
        double unidadesDouble = document.getDouble("Unidades");
        int unidades = (int) unidadesDouble; // Convertir a entero

        return new Productos(nombre, precio, unidades);
    }
}

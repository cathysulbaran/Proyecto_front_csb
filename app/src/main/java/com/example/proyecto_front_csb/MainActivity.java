package com.example.proyecto_front_csb;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ImageView btIniciarRegistro, btIniciarConsulta, btIniciarVentas, btIniciarInforme, btEliminarModificar, btCerrar, btnGenerarInforme;

    private static final int REQUEST_CODE_CREATE_FILE = 123;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);

        btEliminarModificar = findViewById(R.id.btEliminarModificar);
        btIniciarRegistro = findViewById(R.id.btRegistro);
        btIniciarConsulta = findViewById(R.id.btConsulta);
        btCerrar = findViewById(R.id.btCerrar);
        btnGenerarInforme = findViewById(R.id.btGenerarInforme);
        btIniciarVentas = findViewById(R.id.btVentas);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Cerrar Sesión")
                        .setMessage("¿Deseas cerrar la sesión por completo?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Si el usuario confirma, sale de la aplicación
                                cerrarSession();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Si el usuario cancela, cierra el diálogo
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);


        //Variable de sesion establecida para que no se borre hasta que pulsemos cerrar sesion
        SharedPreferences sharedPreferences = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        boolean esAdmin = sharedPreferences.getBoolean("esAdmin", false);

        if(!esAdmin){

        }
        btIniciarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciar_RegistrarProducto();
            }
        });

        btIniciarConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciar_ConsultaArticulo();
            }
        });
        btEliminarModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciar_EliminarModificar();
            }
        });

        btCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciar_Cerrar();
            }
        });

        btnGenerarInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarInforme();
            }
        });

        btIniciarVentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciar_Ventas();
            }
        });

    }




    public void Iniciar_RegistrarProducto(){

        Intent intent = new Intent(this, RegistrarProducto.class);
        startActivity(intent);
    }

    public void Iniciar_ConsultaArticulo(){

        Intent intent = new Intent(this, ConsultaArticulo.class);
        startActivity(intent);
    }

    public void Iniciar_EliminarModificar(){
        Intent intent = new Intent(this, EliminarModificar.class);
        startActivity(intent);
    }

    public void Iniciar_Cerrar(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private void generarInforme() {
        InformeProductos informeProductos = new InformeProductos(MainActivity.this);
        informeProductos.generarInformeProductos();
    }

    private void Iniciar_Ventas() {
        Intent intent = new Intent(this, Ventas.class);
        startActivity(intent);
    }
    private void cerrarSession(){
        FirebaseAuth sesion = FirebaseAuth.getInstance();
        sesion.signOut();
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
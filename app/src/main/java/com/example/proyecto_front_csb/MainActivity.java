package com.example.proyecto_front_csb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Button btIniciarRegistro, btIniciarConsulta, btIniciarVentas, btIniciarInforme, btEliminarModificar, btCerrar, btnGenerarInforme;

    private static final int REQUEST_CODE_CREATE_FILE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btEliminarModificar = findViewById(R.id.btEliminarModificar);
        btIniciarRegistro = findViewById(R.id.btRegistro);
        btIniciarConsulta = findViewById(R.id.btConsulta);
        btCerrar = findViewById(R.id.btCerrar);
        btnGenerarInforme = findViewById(R.id.btGenerarInforme);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Usuarios").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                boolean esAdmin = documentSnapshot.getBoolean("Administrador");
                Toast.makeText(MainActivity.this, "Administrador: " + esAdmin, Toast.LENGTH_SHORT).show();
                if(esAdmin){

                }else {

                }
            }
        });
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
}
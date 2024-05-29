package com.example.proyecto_front_csb;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private CardView btIniciarConsulta, btIniciarRegistro, btIniciarVentas, btEliminarModificar, btRegistrarUsuarios, btnGenerarInforme;

    private static final int REQUEST_CODE_CREATE_FILE = 123;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);



        btEliminarModificar = findViewById(R.id.btEliminarModificar);
        btIniciarRegistro = findViewById(R.id.btRegistro);
        btIniciarConsulta = findViewById(R.id.btConsultaArticulo);
        btRegistrarUsuarios = findViewById(R.id.btRegistroUsuarios);
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


        SharedPreferences sharedPreferences = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        boolean esAdmin = sharedPreferences.getBoolean("esAdmin", false);

        if(!esAdmin){
            btIniciarRegistro.setVisibility(View.INVISIBLE);
            btIniciarRegistro.setEnabled(false);
            btRegistrarUsuarios.setVisibility(View.INVISIBLE);
            btRegistrarUsuarios.setEnabled(false);
            btEliminarModificar.setVisibility(View.INVISIBLE);
            btEliminarModificar.setEnabled(false);
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

        btRegistrarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuarios();
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

    public void registrarUsuarios(){
        Intent intent = new Intent(this, Registro.class);
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
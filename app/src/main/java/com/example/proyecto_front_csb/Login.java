package com.example.proyecto_front_csb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    private EditText edt_username;
    private EditText edt_contrasena;
    private Button bt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_username = findViewById(R.id.edtUsuario);
        edt_contrasena = findViewById(R.id.edtContrasena);
        bt_login = findViewById(R.id.btLogin);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciar_Main();
            }
        });

    }
    //no tiene la verificacion de usuario
    public void Iniciar_Main(){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
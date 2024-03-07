package com.example.proyecto_front_csb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText edt_username;
    private EditText edt_contrasena;
    private Button bt_login;

    private TextView tv_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_username = findViewById(R.id.edtUsuario);
        edt_contrasena = findViewById(R.id.edtContrasena);
        bt_login = findViewById(R.id.btLogin);
        tv_registro = findViewById(R.id.tv_Registro);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciar_Main();
            }
        });

        tv_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, registro.class);
                startActivity(intent);
            }
        });

    }
    //no tiene la verificacion de usuario
    public void Iniciar_Main(){
       FirebaseAuth.getInstance().signInWithEmailAndPassword(edt_username.getText().toString(), edt_contrasena.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(Login.this, "Los datos de inicio de sesion no son correctos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
    }
    //PARA PODER INICIAR SIN USUARIO
    public void Iniciar_MainSinUsuario(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
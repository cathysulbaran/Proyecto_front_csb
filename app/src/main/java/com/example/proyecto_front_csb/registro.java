package com.example.proyecto_front_csb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registro extends AppCompatActivity {

    private EditText edt_username;
    private EditText edt_contrasena;
    private EditText edt_correo;
    private Button bt_crear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edt_username = findViewById(R.id.edtNombre);
        edt_contrasena = findViewById(R.id.edtContrasena);
        edt_correo = findViewById(R.id.edtEmail);
        bt_crear = findViewById(R.id.btCrear);

        bt_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crear_Usuario();
            }
        });
    }

    public void Crear_Usuario(){

        String email = edt_username.getText().toString();
        String password = edt_contrasena.getText().toString();
        String correo = edt_correo.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(correo)) {
            // Message if fields are empty
            Toast.makeText(registro.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            // Message if password is less than 6 characters
            Toast.makeText(registro.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
        } else {
            // Create user with FirebaseAuth
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(edt_correo.getText().toString(), edt_contrasena.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Success message
                                Toast.makeText(registro.this, "Cuenta creada correctamente.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(registro.this, Login.class);
                                startActivity(intent);
                            } else {
                                // Error message for account creation failure
                                Toast.makeText(registro.this, "La cuenta no se ha podido crear correctamente.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


}
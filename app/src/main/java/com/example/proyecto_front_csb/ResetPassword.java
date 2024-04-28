package com.example.proyecto_front_csb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText edtEmail;
    private Button btRestablecer;
    private Button btAtras;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        edtEmail = findViewById(R.id.edtEmail);
        btRestablecer = findViewById(R.id.btnRestablecer);
        btAtras = findViewById(R.id.btnAtras);
        auth = FirebaseAuth.getInstance();
        btRestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                if(!email.isEmpty()){
                    resetPassword(email);
                }else{
                    Toast.makeText(ResetPassword.this, "No se puede resetear la contraseña con un email vacio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPassword.this, Login.class);
                startActivity(intent);
            }
        });

    }

    private void resetPassword(String email){
        auth.setLanguageCode("es");
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPassword.this, "Se ha enviado un correo electronico para poder modificar tu contraseña", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ResetPassword.this, "El email que has enviado o no esta registrado o no se ha validado correctamente. Intentalo de nuevo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
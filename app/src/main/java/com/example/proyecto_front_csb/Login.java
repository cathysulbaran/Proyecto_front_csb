package com.example.proyecto_front_csb;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    private EditText edt_username;
    private EditText edt_contrasena;
    private Button bt_login;

    private TextView tv_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Funcionalidad para no redirigirse a ninguna activity sin iniciar sesion pulsando a atras
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(Login.this)
                        .setTitle("Salir de la aplicación")
                        .setMessage("¿Seguro que deseas salir de la aplicación?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Si el usuario confirma, sale de la aplicación
                                finishAffinity();
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
                Intent intent = new Intent(Login.this, ResetPassword.class);
                startActivity(intent);
            }
        });



    }
    public void Iniciar_Main() {
        String email = edt_username.getText().toString();
        String password = edt_contrasena.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            //Mensaje de error si los campos estan vacios
            Toast.makeText(Login.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            //Verificamos si el usuario y contraseña son validos
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String idUsuario = user.getUid();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference userRef = db.collection("Usuarios").document(idUsuario);
                                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot document = task.getResult();
                                        boolean esAdmin = document.getBoolean("esAdmin");
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        SharedPreferences sharedPreferences = getSharedPreferences("usuario", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("esAdmin", esAdmin);
                                        editor.apply();
                                        startActivity(intent);

                                    }
                                });


                            } else {
                                Toast.makeText(Login.this, "Los datos de inicio de sesion no son correctos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    //PARA PODER INICIAR SIN USUARIO
    public void Iniciar_MainSinUsuario(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
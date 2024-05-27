package com.example.proyecto_front_csb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Registro extends AppCompatActivity {

    private EditText edt_username;
    private EditText edt_contrasena;
    private EditText edt_correo;
    private Button bt_crear;
    private ImageView bt_atras;
    private Spinner spPrivilegios;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edt_username = findViewById(R.id.edtNombre);
        edt_contrasena = findViewById(R.id.edtContrasena);
        edt_correo = findViewById(R.id.edtEmail);
        bt_crear = findViewById(R.id.btCrear);
        bt_atras = findViewById(R.id.btAtras);
        spPrivilegios = findViewById(R.id.spPrivilegios);

        String[] datosSpinner = new String[]{"Administrador", "Estandard"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datosSpinner);
        spPrivilegios.setAdapter(adapter);

        bt_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crear_Usuario();
            }
        });

        bt_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Inicio_atras();
            }
        });
    }

    public void Crear_Usuario(){

        String email = edt_username.getText().toString();
        String password = edt_contrasena.getText().toString();
        String correo = edt_correo.getText().toString();
        String valorSpinner = spPrivilegios.getSelectedItem().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(correo)) {
            // Message if fields are empty
            Toast.makeText(Registro.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            // Message if password is less than 6 characters
            Toast.makeText(Registro.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseAuth registro = FirebaseAuth.getInstance();
            // Create user with FirebaseAuth
            registro.createUserWithEmailAndPassword(edt_correo.getText().toString(), edt_contrasena.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Success message
                                FirebaseUser user = registro.getCurrentUser();
                                if(user!=null){
                                    String idUsuario = user.getUid();
                                    //FirebaseFirestore.getInstance().collection("Usuarios").document(user.getUid())
                                    Toast.makeText(Registro.this, "Cuenta creada correctamente.", Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(Registro.this, "Error al crear la cuenta de usuario.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // Error message for account creation failure
                                Toast.makeText(Registro.this, "La cuenta no se ha podido crear correctamente.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void Inicio_atras(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
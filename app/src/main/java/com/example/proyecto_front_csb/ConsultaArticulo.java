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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConsultaArticulo extends AppCompatActivity {

    private Button btBuscar;
    private EditText edtEANConsulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_articulo);

        btBuscar = findViewById(R.id.btBuscar);
        edtEANConsulta = findViewById(R.id.edtEANConsulta);

        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciar_DetallesArticulo();
            }
        });
    }

    public void Iniciar_DetallesArticulo(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String ean = edtEANConsulta.getText().toString();
        db.collection("Productos").document(ean).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Intent intent = new Intent(ConsultaArticulo.this, DetallesArticulo.class);
                                intent.putExtra("ean",edtEANConsulta.getText().toString());
                                startActivity(intent);
                            }else{
                                Toast.makeText(ConsultaArticulo.this, "El ean introducido no es valido", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ConsultaArticulo.this, "Error al conectarse a la base de datos", Toast.LENGTH_SHORT).show();
                    }
                });


    }

}
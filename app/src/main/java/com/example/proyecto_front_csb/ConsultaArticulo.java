package com.example.proyecto_front_csb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_front_csb.model.DataBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConsultaArticulo extends AppCompatActivity {

    private Button btBuscar, btVolver;
    private EditText edtEANConsulta;
    private TableLayout table_articulos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_articulo);

        btBuscar = findViewById(R.id.btBuscar);
        btVolver = findViewById(R.id.btVolver);
        edtEANConsulta = findViewById(R.id.edtEANConsulta);

        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciar_DetallesArticulo();
            }
        });
        btVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciar_Main();
            }
        });
    }

    public void Iniciar_DetallesArticulo(){
        table_articulos = findViewById(R.id.TableArticulos);

        DataBase db2 = new DataBase();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String ean = edtEANConsulta.getText().toString();
        if (ean.length() !=13) {
            Toast.makeText(ConsultaArticulo.this, "El EAN debe tener exactamente 13 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("Productos").document(ean).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                table_articulos.setVisibility(View.VISIBLE);
                                TextView textEan = findViewById(R.id.textEanValor);
                                TextView textNombre = findViewById(R.id.textNombreValor);
                                TextView textMarca = findViewById(R.id.textMarcaValor);
                                TextView textPrecio = findViewById(R.id.textPrecioValor);
                                TextView textFichaTecnica = findViewById(R.id.textFichaTecnicaValor);
                                TextView textStockDisponibleValor = findViewById(R.id.textStockDisponibleValor);
                                TextView textFechaEntradaValor = findViewById(R.id.textFechaEntradaValor);

                                db2.leerProductosPorEan(ConsultaArticulo.this, ean, textEan, textNombre,textMarca,textPrecio,textFichaTecnica, textStockDisponibleValor,textFechaEntradaValor);

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

    public void Iniciar_Main(){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
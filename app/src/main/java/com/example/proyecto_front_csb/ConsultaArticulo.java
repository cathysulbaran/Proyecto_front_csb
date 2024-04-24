package com.example.proyecto_front_csb;

import androidx.activity.result.ActivityResultLauncher;
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

import com.example.proyecto_front_csb.databinding.ActivityConsultaArticuloBinding;
import com.example.proyecto_front_csb.model.DataBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ConsultaArticulo extends AppCompatActivity {

    ActivityConsultaArticuloBinding binding;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result ->{

        if (result.getContents() == null){

            Toast.makeText(this, "CANCELADO", Toast.LENGTH_SHORT).show();
        } else {

            binding.edtEANConsulta.setText(result.getContents());
        }
    });

    private Button btBuscar, btVolver, btEAN;
    private EditText edtEANConsulta;
    private TableLayout table_articulos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_articulo);

        binding = ActivityConsultaArticuloBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btBuscar = findViewById(R.id.btBuscar);
        btVolver = findViewById(R.id.btVolver);
        edtEANConsulta = findViewById(R.id.edtEANConsulta);
        btEAN = findViewById(R.id.btEAN);

        binding.btEAN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick ( View view){

                escaner();
            }
        });

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

    public void escaner(){

        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
        options.setPrompt("ESCANEAR CODIGO");
        options.setCameraId(0);
        options.setOrientationLocked(false);
        options.setBeepEnabled(false);
        options.setCaptureActivity(CaptureActivityPortraint.class);
        options.setBarcodeImageEnabled(false);

        barcodeLauncher.launch(options);
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
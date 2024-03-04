package com.example.proyecto_front_csb;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto_front_csb.model.Productos;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class RegistrarProducto extends AppCompatActivity {
    private EditText edt_ean;
    private EditText edt_nombre;
    private EditText edt_marca;
    private EditText edt_stockDisp;
    private EditText edt_fecha;
    private EditText edt_fichaTenica;

    private EditText edt_precio;

    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);

        edt_ean = findViewById(R.id.edtEAN);
        edt_nombre = findViewById(R.id.edtNombre);
        edt_marca = findViewById(R.id.edtMarca);
        edt_stockDisp = findViewById(R.id.edtStockDisponible);
        edt_fecha = findViewById(R.id.edtFecha);
        edt_fichaTenica = findViewById(R.id.edtFichaTecnica);
        edt_precio = findViewById(R.id.edtPrecio);
        db = FirebaseFirestore.getInstance();

        findViewById(R.id.btRegistrar).setOnClickListener(v -> guardarProducto());

    }

    private void guardarProducto() {
        String ean = edt_ean.getText().toString();
        String nombre = edt_nombre.getText().toString();
        String marca = edt_marca.getText().toString();
        String fichaTecnica = edt_fichaTenica.getText().toString();
        String precioStr = edt_precio.getText().toString();
        String unidadesStr = edt_stockDisp.getText().toString();
        String entradaMercancia = edt_fecha.getText().toString();

        // Verificar si algún campo está vacío

        if (ean.isEmpty() || nombre.isEmpty() || marca.isEmpty() ||
                fichaTecnica.isEmpty() || precioStr.isEmpty() ||
                unidadesStr.isEmpty() || entradaMercancia.isEmpty()) {
            Toast.makeText(RegistrarProducto.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Double precio = Double.valueOf(precioStr);
        Integer unidades = Integer.valueOf(unidadesStr);

        // Verificar si el campo EAN tiene exactamente 13 caracteres
        if (ean.length() != 13) {
            Toast.makeText(RegistrarProducto.this, "El EAN debe tener exactamente 13 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un objeto Producto con los datos obtenidos
        Productos producto = new Productos(ean, nombre, fichaTecnica, marca, precio, unidades, entradaMercancia);

        // Guardar el producto en Firestore
        db.collection("Productos")
                .add(producto)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegistrarProducto.this, "Producto guardado correctamente", Toast.LENGTH_SHORT).show();
                        // Limpiar los campos después de guardar el producto
                        limpiarCampos();
                    } else {
                        Toast.makeText(RegistrarProducto.this, "Error al guardar el producto", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void limpiarCampos() {
        edt_ean.setText("");
        edt_nombre.setText("");
        edt_marca.setText("");
        edt_stockDisp.setText("");
        edt_fecha.setText("");
        edt_fichaTenica.setText("");
        edt_precio.setText("");
    }
}

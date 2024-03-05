package com.example.proyecto_front_csb;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

        // Verificar si la fecha tiene un formato válido
        if (!entradaMercancia.matches("\\d{2}/\\d{2}/\\d{4}")) {
            Toast.makeText(RegistrarProducto.this, "Formato de fecha incorrecto. Utilice el formato dd/MM/yyyy", Toast.LENGTH_SHORT).show();
            return;
        }

            // Extraer el día, mes y año de la fecha
        String[] partesFecha = entradaMercancia.split("/");
        int dia = Integer.parseInt(partesFecha[0]);
        int mes = Integer.parseInt(partesFecha[1]);
        int anio = Integer.parseInt(partesFecha[2]);

            // Verificar si el mes es válido (de 1 a 12)
        if (mes < 1 || mes > 12) {
            Toast.makeText(RegistrarProducto.this, "El mes ingresado no es válido", Toast.LENGTH_SHORT).show();
            return;
        }

            // Verificar si el día es válido para el mes ingresado
        int diasEnMes = obtenerDiasEnMes(mes, anio);
        if (dia < 1 || dia > diasEnMes) {
            Toast.makeText(RegistrarProducto.this, "El día ingresado no es válido para el mes seleccionado", Toast.LENGTH_SHORT).show();
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

                        // Crear un cuadro de diálogo de confirmación
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarProducto.this);
                        builder.setTitle("Producto registrado correctamente");
                        builder.setMessage("Resumen:\n" +
                                "- Ean: " + ean + "\n" +
                                "- Nombre: " + nombre + "\n" +
                                "- Cantidad: " + unidades + "\n" +
                                "\n¿Quiere registrar otro producto?");

                        // Agregar botones al cuadro de diálogo
                        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Si el usuario elige registrar otro producto, no se hace nada
                                dialog.dismiss(); // Cerrar el cuadro de diálogo
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Si el usuario elige no registrar otro producto, volver al menú principal
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Limpiar la pila de actividades
                                startActivity(intent);
                                finish(); // Finalizar la actividad actual
                            }
                        });

                    // Mostrar el cuadro de diálogo
                        builder.show();
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

    // Función para obtener el número de días en un mes específico
    private int obtenerDiasEnMes(int mes, int anio) {
        int[] diasEnMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (mes == 2 && esBisiesto(anio)) {
            return 29; // Febrero en año bisiesto
        }
        return diasEnMes[mes - 1];
    }

    // Función para verificar si un año es bisiesto
    private boolean esBisiesto(int anio) {
        return (anio % 4 == 0 && anio % 100 != 0) || (anio % 400 == 0);
    }

}

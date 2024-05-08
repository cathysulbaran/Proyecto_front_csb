package com.example.proyecto_front_csb;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.proyecto_front_csb.model.Productos;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class CarritoActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerViewProductos;
    private Adaptador_ventas Adaptador_ventas;
    private TextView txtResumen;
    private Button btConfirmarCompra;
    private ArrayList<Productos> productosSeleccionados = new ArrayList<>();

    EditText usuario, telefono, direccion;
    private Document pdfDocument;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        usuario = findViewById(R.id.edtUsuario);
        telefono = findViewById(R.id.edtTelefonoUsuario);
        direccion = findViewById(R.id.edtDireccion);


        recyclerViewProductos = findViewById(R.id.recyclerViewProductos);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));

        txtResumen = findViewById(R.id.txtResumen);

        // Obtener la lista de productos seleccionados del intent
        productosSeleccionados = getIntent().getParcelableArrayListExtra("productosSeleccionados");

        // Configurar el adaptador para el RecyclerView
        Adaptador_ventas = new Adaptador_ventas(productosSeleccionados);
        recyclerViewProductos.setAdapter(Adaptador_ventas);

        // Mostrar el resumen de los productos seleccionados
        mostrarResumen(productosSeleccionados);

        // Supongamos que tienes un botón para confirmar la compra
        btConfirmarCompra = findViewById(R.id.btGenerarCompra);
        btConfirmarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int totalProductos = productosSeleccionados.size(); // Obtener el número total de productos
                final List<Productos> productosObtenidos = new ArrayList<>(); // Lista para almacenar los productos obtenidos

                // Iterar sobre los productos seleccionados
                for (Productos producto : productosSeleccionados) {
                    String ean = producto.getEan();
                    if (ean != null && !ean.isEmpty()) {
                        FirebaseFirestore.getInstance().collection("Productos")
                                .document(ean)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                // Convertir el documento en un objeto Productos
                                                Productos producto = document.toObject(Productos.class);
                                                // Agregar el producto a la lista de productos obtenidos
                                                productosObtenidos.add(producto);
                                            }
                                        }

                                        // Verificar si se han obtenido todos los productos
                                        if (productosObtenidos.size() == totalProductos) {
                                            // Una vez que se han obtenido todos los productos, generar el PDF
                                            generarPDF(productosObtenidos);
                                        }
                                    }
                                });
                    }
                }
            }
        });


    }


    private void mostrarResumen(ArrayList<Productos> productosSeleccionados) {
        StringBuilder resumen = new StringBuilder();
        double total = 0;

        for (Productos producto : productosSeleccionados) {
            total += producto.getPrecio();
        }

        resumen.append("Total: ").append(total);

        txtResumen.setText(resumen.toString());
    }


    public void generarPDF(List<Productos> productos) {
        // Especifica la ruta de guardado del archivo PDF
        String fileName = "Informe_Productos.pdf";
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(directory, fileName);

        try {
            // Crear el documento PDF en memoria
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document pdfDocument = new Document(pdf);

            // Agregar contenido al documento
            pdfDocument.add(new Paragraph("Listado de Productos\n\n"));

            // Agregar los detalles de cada producto al documento
            for (Productos producto : productos) {
                String ean = producto.getEan(); // Obtenemos el EAN del producto
                String nombre = producto.getNombre(); // Obtenemos el nombre del producto
                double precio = producto.getPrecio(); // Obtenemos el precio del producto
                int unidades = producto.getUnidades(); // Obtenemos la cantidad de unidades del producto

                pdfDocument.add(new Paragraph("EAN: " + ean));
                pdfDocument.add(new Paragraph("Nombre: " + nombre));
                pdfDocument.add(new Paragraph("Precio: " + precio));
                pdfDocument.add(new Paragraph("Unidades: " + unidades));
                pdfDocument.add(new Paragraph("\n")); // Separador entre productos
            }

            // Cerrar el documento
            pdfDocument.close();

            // Mostrar un mensaje de éxito
            Toast.makeText(this, "Informe generado correctamente en: " + file.getParentFile(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar el informe de productos", Toast.LENGTH_SHORT).show();
        }
    }




    private void obtenerDatosUsuario() {

        String nombreUsuario = usuario.getText().toString();
        String telefonoUsuario = telefono.getText().toString();
        String direccionUsuario = direccion.getText().toString();

        // Mostrar los datos del usuario en el informe de compra
        pdfDocument.add(new Paragraph("Datos del Usuario:"));
        pdfDocument.add(new Paragraph("Nombre: " + nombreUsuario));
        pdfDocument.add(new Paragraph("Teléfono: " + telefonoUsuario));
        pdfDocument.add(new Paragraph("Dirección: " + direccionUsuario));
    }


}

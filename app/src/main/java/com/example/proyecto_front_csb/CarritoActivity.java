package com.example.proyecto_front_csb;

import static com.example.proyecto_front_csb.model.Productos.DocumentSnapshot;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.example.proyecto_front_csb.model.Productos;

import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
        ArrayList<Productos> productosSeleccionados = getIntent().getParcelableArrayListExtra("productosSeleccionados");

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
                ArrayList<Productos> productos = new ArrayList<>();
                final int[] documentosCompletos = {0}; // Contador para el número de documentos obtenidos

                for (Productos producto : Adaptador_ventas.getProductosSeleccionados()) {
                    // Suponiendo que tienes acceso a la referencia de la colección de Firestore donde están los productos
                    // Utiliza el método get() para obtener los objetos Productos para cada producto
                    // Añade estos productos a la lista
                    db.document(producto.getEan()).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Productos productoObtenido = documentSnapshot.toObject(Productos.class);
                                    productos.add(productoObtenido);
                                    documentosCompletos[0]++; // Incrementa el contador de documentos obtenidos

                                    // Cuando todos los documentos hayan sido agregados, genera el informe
                                    if (documentosCompletos[0] == Adaptador_ventas.getProductosSeleccionados().size()) {
                                        generarInformeCompra(productos);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Manejar errores de lectura de Firestore aquí
                                }
                            });
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

    private void generarInformeCompra(ArrayList<Productos> productos) {
        // Especifica la ruta de guardado del archivo PDF
        String fileName = "Compra_informe.pdf";
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(directory, fileName);

        try {
            // Crear el documento PDF en memoria
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document pdfDocument = new Document(pdf);

            // Agregar contenido al documento
            pdfDocument.add(new Paragraph("Informe de Compra\n\n"));

            obtenerDatosUsuario();

            // Agregar los detalles de cada producto al documento
            for (Productos producto : productos) {
                // Utiliza el ID del documento como el campo "ean"
                String nombre = producto.getNombre();
                String fichaTecnica = producto.getFichaTecnica();
                String marca = producto.getMarca();
                double precio = producto.getPrecio();
                int unidades = producto.getUnidades();

                // Agregar los detalles del producto al documento
                pdfDocument.add(new Paragraph("Nombre: " + nombre));
                pdfDocument.add(new Paragraph("Marca: " + marca));
                pdfDocument.add(new Paragraph("Precio: " + precio));
                pdfDocument.add(new Paragraph("Ficha Técnica: " + fichaTecnica));
                pdfDocument.add(new Paragraph("Unidades: " + unidades));
                pdfDocument.add(new Paragraph("\n")); // Separador entre productos
            }

            // Cerrar el documento
            pdfDocument.close();

            // Mostrar un mensaje de éxito
            Toast.makeText(this, "Informe generado correctamente en: " + file.getParentFile(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar el informe de compra", Toast.LENGTH_SHORT).show();
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

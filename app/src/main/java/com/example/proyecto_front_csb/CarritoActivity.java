package com.example.proyecto_front_csb;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
import java.util.List;

import com.example.proyecto_front_csb.model.Productos;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class CarritoActivity extends AppCompatActivity {

    private Context context;
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


        context = this;

        usuario = findViewById(R.id.edtUsuario);
        telefono = findViewById(R.id.edtTelefonoUsuario);
        direccion = findViewById(R.id.edtDireccion);


        recyclerViewProductos = findViewById(R.id.recyclerViewProductos);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));

        txtResumen = findViewById(R.id.txtResumen);

        // Obtener la lista de productos seleccionados del intent
        productosSeleccionados = (ArrayList<Productos>) getIntent().getSerializableExtra("productosSeleccionados");

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
                generar();
            }
        });
    }

    public void generar(){

        generarInformePDF(productosSeleccionados);
    }

    private void generarInformePDF(List<Productos> productosSeleccionados) {
        // Especifica la ruta de guardado del archivo PDF
        String fileName = "Informe_Productos.pdf";
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(directory, fileName);

        try {
            // Crear el documento PDF en memoria
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document pdfDocument = new Document(pdf);

            obtenerDatosUsuario(pdfDocument);
            // Agregar contenido al documento
            pdfDocument.add(new Paragraph("Informe de Productos\n\n"));

            // Agregar los detalles de cada producto al documento
            for (Productos producto : productosSeleccionados) {
                pdfDocument.add(new Paragraph("EAN: " + producto.getEan()));
                pdfDocument.add(new Paragraph("Nombre: " + producto.getNombre()));
                pdfDocument.add(new Paragraph("Precio: " + producto.getPrecio()));
                pdfDocument.add(new Paragraph("Stock Disponible: " + producto.getUnidades()));
                pdfDocument.add(new Paragraph("\n")); // Separador entre productos
            }


            // Cerrar el documento
            pdfDocument.close();

            // Mostrar un mensaje de éxito
            Toast.makeText(context, "Informe generado correctamente en: " + file.getParentFile(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al guardar el informe de productos", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarResumen(ArrayList<Productos> productosSeleccionados) {
        StringBuilder resumen = new StringBuilder();
        double total = 0;

        for (Productos producto : productosSeleccionados) {
            int cantidad = producto.getUnidades();
            double precio = producto.getPrecio();
            double resultado = precio*cantidad;
            total = total+resultado;
        }

        resumen.append("Total: ").append(total);

        txtResumen.setText(resumen.toString());
    }

    private void obtenerDatosUsuario(Document pdfDocument) {

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

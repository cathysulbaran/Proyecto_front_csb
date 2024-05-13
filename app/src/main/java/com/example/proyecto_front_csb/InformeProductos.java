package com.example.proyecto_front_csb;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.example.proyecto_front_csb.model.Productos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InformeProductos {
    private static final String TAG = "InformeProductos";
    private Context context;
    private FirebaseFirestore db;


    public InformeProductos(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }



    public void generarInformeProductos() {
        // Consultar Firestore para obtener todos los productos
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Productos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Productos> productosList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Productos producto = document.toObject(Productos.class);
                        productosList.add(producto);
                    }
                    // Generar un informe PDF con los detalles de todos los productos
                    generarInformePDF(productosList);
                } else {
                    Log.e(TAG, "Error al obtener productos", task.getException());
                    Toast.makeText(context, "Error al obtener productos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void generarInformePDF(List<Productos> productosList) {
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

            // Agregar contenido al documento
            pdfDocument.add(new Paragraph("Informe de Productos\n\n"));

            // Agregar los detalles de cada producto al documento
            for (Productos producto : productosList) {
                pdfDocument.add(new Paragraph("EAN: " + producto.getEan()));
                pdfDocument.add(new Paragraph("Nombre: " + producto.getNombre()));
                pdfDocument.add(new Paragraph("Marca: " + producto.getMarca()));
                pdfDocument.add(new Paragraph("Precio: " + producto.getPrecio()));
                pdfDocument.add(new Paragraph("Ficha Técnica: " + producto.getFichaTecnica()));
                pdfDocument.add(new Paragraph("Stock Disponible: " + producto.getUnidades()));
                pdfDocument.add(new Paragraph("Fecha de Entrada: " + producto.getEntradaMercancia()));
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



}
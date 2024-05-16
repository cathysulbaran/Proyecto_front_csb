package com.example.proyecto_front_csb;

import static android.content.ContentValues.TAG;

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
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InformeProductos {
    private Context context;

    public InformeProductos(Context context) {
        this.context = context;
    }

    public void generarInformeProductos() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Productos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Productos> productosList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Productos producto = new Productos();

                        producto.setEan(document.getId());

                        // Asignar otros campos del producto
                        producto.setNombre(document.getString("Nombre"));
                        producto.setMarca(document.getString("Marca"));
                        // Convertir el valor de tipo String a int y asignarlo a unidades
                        int unidades = document.getLong("Unidades").intValue();
                        producto.setUnidades(unidades);
                        producto.setEntradaMercancia(document.getString("entradaMercancia"));
                        producto.setFichaTecnica(document.getString("FichaTecnica"));
                        producto.setPrecio(document.getDouble("Precio"));

                        // Agregar el producto a la lista
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


    public void generarInformePDF(List<Productos> productosList) {
        // Especifica la ruta de guardado del archivo PDF
        String fileName = "Stock.pdf";
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(directory, fileName);

        try {
            // Crear el documento PDF en memoria
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(PageSize.A4.rotate());
            Document pdfDocument = new Document(pdf);

            // Agregar encabezado con el nombre de la empresa
            pdfDocument.add(new Paragraph("Informe de Stock").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            pdfDocument.add(new Paragraph("\n\n")); // Espacio después del encabezado

            // Agregar tabla con los detalles de cada producto
            float[] columnWidths = {100f, 200f, 20f, 80f, 100f, 20f, 200f};
            Table table = new Table(columnWidths);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // Agregar encabezados de la tabla
            table.addCell(new Cell().add(new Paragraph("EAN").setBold()));
            table.addCell(new Cell().add(new Paragraph("Nombre").setBold()));
            table.addCell(new Cell().add(new Paragraph("Unidades").setBold()));
            table.addCell(new Cell().add(new Paragraph("Fecha de entrada").setBold()));
            table.addCell(new Cell().add(new Paragraph("Marca").setBold()));
            table.addCell(new Cell().add(new Paragraph("Precio").setBold()));
            table.addCell(new Cell().add(new Paragraph("Ficha técnica").setBold()));

            // Agregar los detalles de cada producto a la tabla
            // Agregar los detalles de cada producto a la tabla
            for (Productos producto : productosList) {
                // Verifica si el EAN es nulo o no válido
                String ean = producto.getEan();
                if (ean != null && !ean.isEmpty()) {
                    table.addCell(new Cell().add(new Paragraph(ean)));
                } else {
                    table.addCell(new Cell().add(new Paragraph("EAN no disponible")));
                }

                // Verifica si el nombre es nulo o no válido
                String nombre = producto.getNombre();
                if (nombre != null && !nombre.isEmpty()) {
                    table.addCell(new Cell().add(new Paragraph(nombre)));
                } else {
                    table.addCell(new Cell().add(new Paragraph("Nombre no disponible")));
                }
                int unidades = producto.getUnidades();
                table.addCell(new Cell().add(new Paragraph(String.valueOf(unidades))));
                // Verifica si la fecha de entrada es nula o no válida
                String entradaMercancia = producto.getEntradaMercancia();
                if (entradaMercancia != null && !entradaMercancia.isEmpty()) {
                    table.addCell(new Cell().add(new Paragraph(entradaMercancia)));
                } else {
                    table.addCell(new Cell().add(new Paragraph("Fecha de entrada no disponible")));
                }

                // Verifica si la marca es nula o no válida
                String marca = producto.getMarca();
                if (marca != null && !marca.isEmpty()) {
                    table.addCell(new Cell().add(new Paragraph(marca)));
                } else {
                    table.addCell(new Cell().add(new Paragraph("Marca no disponible")));
                }

                // Verifica si el precio es nulo o no válido
                Double precio = producto.getPrecio();
                if (precio != null) {
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(precio))));
                } else {
                    table.addCell(new Cell().add(new Paragraph("Precio no disponible")));
                }

                // Verifica si la ficha técnica es nula o no válida
                String fichaTecnica = producto.getFichaTecnica();
                if (fichaTecnica != null && !fichaTecnica.isEmpty()) {
                    table.addCell(new Cell().add(new Paragraph(fichaTecnica)));
                } else {
                    table.addCell(new Cell().add(new Paragraph("Ficha técnica no disponible")));
                }
            }



            // Agregar la tabla al documento
            pdfDocument.add(table);

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

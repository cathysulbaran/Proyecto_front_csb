package com.example.proyecto_front_csb;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import com.example.proyecto_front_csb.model.Productos;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

public class CarritoActivity extends AppCompatActivity {

    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerViewProductos;
    private Adaptador_ventas Adaptador_ventas;
    private TextView txtResumen;
    private Button btConfirmarCompra;

    private ImageView btAtras;
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
        btAtras = findViewById(R.id.btVolver);


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

        btAtras.setOnClickListener(v -> volver());

    }

    public void generar(){

        generarInformePDF(productosSeleccionados);
    }


    public void generarInformePDF(List<Productos> productosSeleccionados) {

        String usuarioTexto = usuario.getText().toString().trim();
        String telefonoTexto = telefono.getText().toString().trim();
        String direccionTexto = direccion.getText().toString().trim();

        // Verificar que los campos obligatorios no estén vacíos
        if (usuarioTexto.isEmpty() || telefonoTexto.isEmpty() || direccionTexto.isEmpty()) {
            Toast.makeText(context, "El nombre, el teléfono y la dirección son obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar que el número de teléfono tenga 9 dígitos
        if (telefonoTexto.length() != 9) {
            Toast.makeText(context, "El número de teléfono debe tener 9 dígitos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Especifica el nombre del archivo PDF generado
        String fileName = "Ticket de venta.pdf";
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(directory, fileName);

        try {
            // Crear el documento PDF
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            PdfDocument pdf = new PdfDocument(writer);
            Document pdfDocument = new Document(pdf, PageSize.A4);

            // Obtener el objeto ImageData directamente desde el recurso raw
            InputStream inputStream = getResources().openRawResource(R.raw.banner);

            // Leer la imagen como un array de bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            byte[] imageData = outputStream.toByteArray();

            // Crear objeto ImageData para la imagen del encabezado
            ImageData imageDataEncabezado = ImageDataFactory.create(imageData);

            // Agregar encabezado (imagen) al documento (parte superior)
            com.itextpdf.layout.element.Image imagenEncabezado = new com.itextpdf.layout.element.Image(imageDataEncabezado);

            // Ajustar el tamaño de la imagen manualmente
            float desiredWidth = PageSize.A4.getWidth() - pdfDocument.getLeftMargin() - pdfDocument.getRightMargin();
            float desiredHeight = 100; // Ajusta este valor según necesites
            imagenEncabezado.scaleToFit(desiredWidth, desiredHeight);
            imagenEncabezado.setHorizontalAlignment(HorizontalAlignment.CENTER);
            pdfDocument.add(imagenEncabezado);

            // Agregar encabezado al documento (parte superior)
            pdfDocument.add(new Paragraph("INVETORYGENIE").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            pdfDocument.add(new Paragraph("Dirección: Calle Principal, Ciudad, País").setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            pdfDocument.add(new Paragraph("Teléfono: +123456789").setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            pdfDocument.add(new Paragraph("Correo electrónico: invetorygenie@empresa.com").setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            pdfDocument.add(new Paragraph("\n")); // Espacio entre el encabezado y el contenido principal

            // Crear tabla para los detalles de los productos
            float[] columnWidths = {100f, 200f, 100f, 100f};
            Table table = new Table(columnWidths);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // Agregar encabezados de la tabla
            table.addCell(new Cell().add(new Paragraph("EAN")));
            table.addCell(new Cell().add(new Paragraph("Nombre")));
            table.addCell(new Cell().add(new Paragraph("Precio")));
            table.addCell(new Cell().add(new Paragraph("Cantidad")));

            // Agregar los detalles de cada producto a la tabla
            for (Productos producto : productosSeleccionados) {
                table.addCell(new Cell().add(new Paragraph(producto.getEan())));
                table.addCell(new Cell().add(new Paragraph(producto.getNombre())));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(producto.getPrecio()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(producto.getUnidades()))));
            }

            // Agregar la tabla al documento
            pdfDocument.add(table);

            // Calcular el total
            float total = calcularTotal(productosSeleccionados);
            float totalConIVA = total + (total * 0.21f); // Suponiendo un IVA del 21%

            // Agregar sección de información de pago (abajo a la izquierda)
            pdfDocument.add(new Paragraph("\n"));
            Paragraph informacionPago = new Paragraph();
            informacionPago.add("Información de pago:").setBold().add("\n");
            informacionPago.add("Método de pago: Tarjeta de crédito").add("\n");
            informacionPago.add("Número de tarjeta: **** **** **** 1234").add("\n");
            informacionPago.add("Fecha de pago: 01/01/2023");
            pdfDocument.add(informacionPago);

            // Agregar espacio para mover el total a la parte inferior de la página
            pdfDocument.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));

            // Agregar sección del total antes del IVA (abajo a la derecha)
            Paragraph totalAntesIVA = new Paragraph();
            totalAntesIVA.add("Total antes de IVA: €" + total).setTextAlignment(TextAlignment.RIGHT);
            pdfDocument.add(totalAntesIVA);

            // Agregar sección del IVA (abajo a la derecha)
            Paragraph iva = new Paragraph();
            iva.add("IVA (21%): €" + (total * 0.21f)).setTextAlignment(TextAlignment.RIGHT);
            pdfDocument.add(iva);

            // Formatear el total a dos decimales y con el símbolo del euro
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setCurrencySymbol("€");
            DecimalFormat decimalFormat = new DecimalFormat("#.##", symbols);
            String totalFormateado = decimalFormat.format(totalConIVA);

            // Agregar sección del total final (abajo a la derecha)
            Paragraph totalPago = new Paragraph();
            totalPago.add("Total a pagar: " + totalFormateado).setBold().setTextAlignment(TextAlignment.RIGHT);
            pdfDocument.add(totalPago);

            // Cerrar el documento
            pdfDocument.close();

            // Mostrar un mensaje de éxito
            Toast.makeText(context, "Informe generado correctamente en: " + file.getParentFile(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al guardar el informe de productos", Toast.LENGTH_SHORT).show();
        }
    }



    // Método para calcular el total de la compra
    private float calcularTotal(List<Productos> productos) {
        float total = 0;
        for (Productos producto : productos) {
            total += producto.getPrecio() * producto.getUnidades();
        }
        return total;
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



    public void volver() {
        Intent intent = new Intent(this, Ventas.class);
        startActivity(intent);
    }
}

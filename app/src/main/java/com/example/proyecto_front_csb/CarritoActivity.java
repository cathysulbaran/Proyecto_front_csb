package com.example.proyecto_front_csb;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.example.proyecto_front_csb.model.Productos;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_front_csb.model.Productos;
import java.util.ArrayList;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProductos;
    private Adaptador_ventas Adaptador_ventas;
    private TextView txtResumen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

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
}

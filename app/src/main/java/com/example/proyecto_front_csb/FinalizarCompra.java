package com.example.proyecto_front_csb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinalizarCompra extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_compra);


        Button atras = findViewById(R.id.bt_atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinalizarCompra.this, ListadoProductos.class);
                startActivity(intent);
            }
        });

        Button finalizar = findViewById(R.id.bt_finalizar);
        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un diálogo personalizado
                Dialog dialog = new Dialog(FinalizarCompra.this);
                dialog.setContentView(R.layout.activity_dialog_message);

                // Configurar el mensaje
                TextView textMessage = dialog.findViewById(R.id.text_message);
                textMessage.setText("Venta realizada correctamente");

                // Configurar el botón OK
                Button buttonOK = dialog.findViewById(R.id.button_ok);
                buttonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Cerrar el diálogo
                        dialog.dismiss();
                    }
                });

                // Mostrar el diálogo
                dialog.show();
            }
        });
    }
}

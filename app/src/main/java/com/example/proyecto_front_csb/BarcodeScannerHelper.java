package com.example.proyecto_front_csb;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.proyecto_front_csb.databinding.ActivityConsultaArticuloBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class BarcodeScannerHelper {

    private Activity activity;

    public BarcodeScannerHelper(Activity activity) {
        this.activity = activity;
    }

    public void startScanner() {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("ESCANEAR CODIGO");
        integrator.initiateScan();
    }

    public void handleScanResult(Intent data, OnScanResultListener listener) {
        Log.d("BarcodeScannerHelper", "Handling scan result...");
        IntentResult result = IntentIntegrator.parseActivityResult(Activity.RESULT_OK, data);
        if (result != null) {
            Log.d("BarcodeScannerHelper", "Result not null");
            if (result.getContents() == null) {
                Log.d("BarcodeScannerHelper", "Contents null");
                Toast.makeText(activity, "Cancelado", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("BarcodeScannerHelper", "Contents: " + result.getContents());
                if (listener != null) {
                    listener.onScanResult(result.getContents());
                }
            }
        } else {
            Log.e("BarcodeScannerHelper", "Error processing scan result");
            Toast.makeText(activity, "Error al procesar el resultado del escaneo", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnScanResultListener {
        void onScanResult(String contents);
    }
}

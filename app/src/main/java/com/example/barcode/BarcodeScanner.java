package com.example.barcode;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.List;
public class BarcodeScanner extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;
    private Button scanButton;
    private TextView resultTextView;

    private CompoundBarcodeView compoundBarcodeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        compoundBarcodeView =  findViewById(R.id.barcode_view);
        scanButton = findViewById(R.id.scan_button);
        resultTextView = findViewById(R.id.result_text_view);
        scanButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(BarcodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startScanner();
            } else {
                ActivityCompat.requestPermissions(BarcodeScanner.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        });

        SeekBar zoomSeekBar = findViewById(R.id.zoom_seek_bar);

        zoomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
            // الدوال التالية يمكنك تركها فارغة لأنها ليست ضرورية لهذا السياق
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    private void startScanner() {
        scanButton.setEnabled(false);
        resultTextView.setText("");
        ScanOptions options = new ScanOptions();
        options.setBeepEnabled(false);
        options.setCameraId(0);
        options.setBarcodeImageEnabled(true);
        compoundBarcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                String barcodeValue = result.getText();
                Toast.makeText(BarcodeScanner.this, "Scanned barcode: " + barcodeValue, Toast.LENGTH_SHORT).show();
                // Display the scanned barcode value in the resultTextView.
                resultTextView.setText(barcodeValue);
                // Stop the scanner.
                compoundBarcodeView.pause();
                scanButton.setEnabled(true);
            }
            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
                // Unused method, can be left empty.
            }
        });
        // Animate the red line across the screen
        compoundBarcodeView.resume();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            compoundBarcodeView.resume();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        compoundBarcodeView.pause();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner();
            } else {
                Toast.makeText(this, "مطلوب اذن الكيمره", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
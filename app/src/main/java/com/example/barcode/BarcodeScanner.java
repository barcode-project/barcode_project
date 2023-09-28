package com.example.barcode;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import java.util.List;
public class BarcodeScanner extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;
    private Button scanButton;
    private TextView resultTextView;
    private View redLineView;
    private CompoundBarcodeView compoundBarcodeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        compoundBarcodeView = (CompoundBarcodeView) findViewById(R.id.barcode_view);
        scanButton = findViewById(R.id.scan_button);
        resultTextView = findViewById(R.id.result_text_view);
        redLineView = findViewById(R.id.red_line_view);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(BarcodeScanner.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startScanner();
                } else {
                    ActivityCompat.requestPermissions(BarcodeScanner.this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        });
    }
    private void startScanner() {
        scanButton.setEnabled(false);
        resultTextView.setText("");
        redLineView.setVisibility(View.VISIBLE);
        compoundBarcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                String barcodeValue = result.getText();
                Toast.makeText(BarcodeScanner.this, "Scanned barcode: " + barcodeValue, Toast.LENGTH_SHORT).show();

                // Display the scanned barcode value in the resultTextView.
                resultTextView.setText(barcodeValue);

                // Stop the scanner.
                compoundBarcodeView.pause();
                redLineView.setVisibility(View.GONE);

                scanButton.setEnabled(true);
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
                // Unused method, can be left empty.
            }
        });
        // Animate the red line across the screen
        redLineView.animate().translationXBy(1000).setDuration(3000).start();

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
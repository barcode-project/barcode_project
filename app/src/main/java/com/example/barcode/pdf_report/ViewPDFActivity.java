package com.example.barcode.pdf_report;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.barcode.R;
import com.example.barcode.utils.BaseActivity;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ViewPDFActivity extends BaseActivity {


    private PDFView pdfView;
    ImageView open_pdf, print_pdf,share_pdf;
    private File file;
    private Context primaryBaseActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_p_d_f);
        open_pdf = findViewById(R.id.open_pdf);
        share_pdf = findViewById(R.id.share_pdf);
        print_pdf = findViewById(R.id.print_pdf);
        pdfView = findViewById(R.id.pdfView);
open_pdf.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Toast.makeText(getApplicationContext(),R.string.open_with_external_pdf_reader,Toast.LENGTH_SHORT).show();
        openWithExternalPdfApp();

    }
});
share_pdf.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(), R.string.share, Toast.LENGTH_SHORT).show();
        sharePdfFile();
    }
});
print_pdf.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        printPDf();
    }
});

        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        Log.d("location", bundle.toString());

        String dest = this.getExternalFilesDir(null) + "/";

        file = new File(dest, "order_receipt.pdf");

        pdfView.fromFile(file)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .load();


    }


    public void sharePdfFile() {
        Uri uri = FileProvider.getUriForFile(ViewPDFActivity.this, ViewPDFActivity.this.getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }

    public void openWithExternalPdfApp() {
        Uri uri = FileProvider.getUriForFile(ViewPDFActivity.this, ViewPDFActivity.this.getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "No PDF viewer app found", Toast.LENGTH_SHORT).show();
        }
    }




    public void printPDf() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PrintManager printManager = (PrintManager) primaryBaseActivity.getSystemService(Context.PRINT_SERVICE);
            String jobName = this.getString(R.string.app_name) + "Order Receipt";


            PrintDocumentAdapter pda = new PrintDocumentAdapter() {

                @Override
                public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
                    InputStream input = null;
                    OutputStream output = null;

                    try {

                        File folder = new File(Environment.getExternalStorageDirectory().toString(), "PDF");
                        if (!folder.exists())
                            folder.mkdir();


                        File file = new File(folder, "order_receipt.pdf");
                        input = new FileInputStream(file);
                        output = new FileOutputStream(destination.getFileDescriptor());
                        byte[] buf = new byte[1024];
                        int bytesRead;

                        while ((bytesRead = input.read(buf)) > 0) {
                            output.write(buf, 0, bytesRead);
                        }

                        callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

                    } catch (Exception ee) {
                        //Catch exception
                    } finally {
                        try {
                            if (input != null) {
                                input.close();
                            }
                            if (output != null) {
                                output.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

                    if (cancellationSignal.isCanceled()) {
                        callback.onLayoutCancelled();
                        return;
                    }


                    PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("Name of file").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();

                    callback.onLayoutFinished(pdi, true);
                }
            };
            if (printManager != null) {


                PrintAttributes.Builder builder = new PrintAttributes.Builder();
                //set default printing page size, you can change printing page size here
                builder.setMediaSize(PrintAttributes.MediaSize.PRC_6);
                printManager.print(jobName, pda, builder.build());
            }
        }
    }


    protected void attachBaseContext(Context base) {
        primaryBaseActivity = base;
        super.attachBaseContext(base);
    }

}


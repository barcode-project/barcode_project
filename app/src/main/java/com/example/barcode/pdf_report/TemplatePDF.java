package com.example.barcode.pdf_report;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.barcode.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TemplatePDF {

    private Context context;
    private File pdfFile;
    private Document document;
    PdfWriter pdfWriter;
    private Paragraph paragraph;
    //here you can change fonts,fonts size and fonts color



//    private Font fTitle=new Font(Font.FontFamily.HELVETICA,6,Font.NORMAL,BaseColor.BLACK);
   private Font fTitle=new Font(Font.getFamily(String.valueOf(R.font.tajawal)));
    private Font fSubTitle=new Font(Font.getFamily(String.valueOf(R.font.boldiphone)),4, Font.BOLDITALIC,BaseColor.BLUE);

//    private Font fSubTitle=new Font(Font.FontFamily.TIMES_ROMAN,4,Font.ITALIC,BaseColor.BLACK);
    private Font fText=new Font(Font.FontFamily.TIMES_ROMAN,4,Font.ITALIC,BaseColor.BLACK);
    private Font fHighText=new Font(Font.FontFamily.TIMES_ROMAN,4,Font.ITALIC, BaseColor.BLACK);
    private Font fRowText=new Font(Font.FontFamily.TIMES_ROMAN,4,Font.ITALIC, BaseColor.BLACK);
    public TemplatePDF(Context context)
    {
        this.context=context;
    }

    public void openDocument()
    {
        createFile();
        try{

            //adjust your page size here
            Rectangle pageSize = new Rectangle(164.41f, 500.41f); //14400 //for 58 mm pos printer
            //document=new Document(PageSize.A4);
            document=new Document(pageSize);
            pdfWriter=PdfWriter.getInstance(document,new FileOutputStream(pdfFile));
            document.open();
        }catch (Exception e)
        {
            Log.e("createFile",e.toString());
        }
    }

    private void createFile()
    {
       // File folder = new File(Environment.getExternalStorageDirectory().toString(), "PDF");

        String dest = context.getExternalFilesDir(null) + "/";
        File folder = new File(dest);

        if (!folder.exists())
            folder.mkdir();

        //your file name
        pdfFile = new File(folder, "order_receipt.pdf");


    }

    public void closeDocument()
    {
        document.close();
    }

    public void addMetaData(String title, String subject, String author)
    {
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);
        
    }

    public void addTitle(String title, String subTitle, String date)
    {


        try {


            paragraph = new Paragraph();
            addChildP(new Paragraph(title, fTitle));
            addChildP(new Paragraph(subTitle, fSubTitle));
            addChildP(new Paragraph("Order Date:" + date, fHighText));
           // paragraph.setSpacingAfter(30);
            document.add(paragraph);
        }
        catch (Exception e)
        {
            Log.e("addTitle",e.toString());
        }
    }




    public void addImage(Bitmap bm) {

        try {

            Bitmap bmp = bm;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);

            byte[] byteArray = stream.toByteArray();
            // PdfImage img = new PdfImage(arg0, arg1, arg2)

            // Converting byte array into image Image
            Image img = Image.getInstance(byteArray);

            img.setAlignment(Image.ALIGN_BOTTOM);
            img.setAlignment(Image.ALIGN_CENTER);
            img.scaleAbsolute(80f, 20f);
            //img.setAbsolutePosition(imageStartX, imageStartY); // Adding Image

            document.add(img);
        } catch (Exception e) {
            Log.e("addParagraph", e.toString());
        }


    }






    public void addChildP(Paragraph childParagraph)
    {

        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }

    public void addParagraph(String text)
    {

        try{

        paragraph=new Paragraph(text,fText);
      //  paragraph.setSpacingAfter(1);
       // paragraph.setSpacingBefore(1);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        }
        catch (Exception e)
        {
            Log.e("addParagraph",e.toString());
        }



    }



    public void addRightParagraph(String text)
    {

        try{

            paragraph=new Paragraph(text,fText);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
        }
        catch (Exception e)
        {
            Log.e("addParagraph",e.toString());
        }



    }

    public void createTable(String[] header, ArrayList<String[]> clients)
    {

        try {


            paragraph = new Paragraph();
            paragraph.setFont(fText);
            PdfPTable pdfPTable = new PdfPTable(header.length);
            pdfPTable.setWidthPercentage(100);
            pdfPTable.setSpacingBefore(5);

            PdfPCell pdfPCell;

            int indexC = 0;
            while (indexC < header.length) {
                pdfPCell = new PdfPCell(new Phrase(header[indexC++], fSubTitle));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                pdfPCell.setBackgroundColor(BaseColor.WHITE);
                pdfPCell.setBorderColor(BaseColor.GRAY);
                pdfPTable.addCell(pdfPCell);
            }

            for (int indexR = 0; indexR < clients.size(); indexR++) {
                String[] row = clients.get(indexR);

                for (indexC = 0; indexC < header.length; indexC++) {
                    pdfPCell = new PdfPCell(new Phrase(row[indexC],fRowText));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                   // pdfPCell.setFixedHeight(40);
                    pdfPCell.setBorder(Rectangle.NO_BORDER);
                    pdfPTable.addCell(pdfPCell);
                }
            }

            paragraph.add(pdfPTable);
            document.add(paragraph);

        }catch (Exception e)
        {
            Log.e("createTable",e.toString());
        }
    }

    public void viewPDF()
    {
        Intent intent=new Intent(context,ViewPDFActivity.class);
        intent.putExtra("path",pdfFile.getAbsolutePath());
        Log.d("Path",pdfFile.getAbsolutePath());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }






}

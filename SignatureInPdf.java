package ru.ruselprom.signs;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SignatureInPdf {
    //    private static final String BASE_FONT = "C:\\ptc\\Windchill_11.1cps05\\eclipse\\fonts\\GOST type A.ttf";
    private static final String BASE_FONT = "D:\\ptc\\Windchill_11.1\\eclipse\\fonts\\GOST type A.ttf";

    public static void main(String[] args) {
        ArrayList<String> roles = new ArrayList<>(10);
        ArrayList<String> users = new ArrayList<>(10);
        ArrayList<String> date = new ArrayList<>(10);
//        String filePath = "D:\\Ruselprom\\Projects\\pdf with signs\\РСШН.685421.002 СБ Катушка статора_r42.pdf";
        String filePath = "D:\\ptc\\Windchill_11.1\\eclipse\\filePdf\\РСШН.685421.002.pdf";
        InputStream pdf = Representation.getPdfByOidOfDrw("VR:wt.epm.EPMDocument:79810893");
    }

    public static void signTheDocument(InputStream pdf, ArrayList<String> roles, ArrayList<String> users, ArrayList<String> date, String filePath) {
        try {
            PdfReader reader = null;
            if (pdf == null) {
                throw new NullValueException("pdf stream is null");
            } else {
                reader = new PdfReader(pdf);
                PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("D:\\Ruselprom\\Projects\\pdf with signs\\РСШН result.pdf"));
            }

        } catch (IOException | DocumentException | NullValueException e) {
            e.printStackTrace();
        }
    }

    private static void getPositionY(int i) {

    }

    private static void getPositionRole() {

    }
}

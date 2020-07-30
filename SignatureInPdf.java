package ru.ruselprom.signs;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import ru.ruselprom.signs.data.PdfData;
import ru.ruselprom.signs.exceptions.NullValueException;


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
        PdfData pdfData = Representation.getPdfDataByOidOfDrw("VR:wt.epm.EPMDocument:79810893");
        InputStream pdfStream = pdfData.getPdfStream();
    }

    public static void signTheDocument(InputStream pdf, ArrayList<String> roles, ArrayList<String> users, ArrayList<String> date, String filePath) {
        try {
            PdfReader reader = null;
            if (pdf == null) {
                throw new ru.ruselprom.signs.exceptions.NullValueException("pdf stream is null");
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

package com.tester;

        import com.lowagie.text.DocumentException;
        import com.lowagie.text.Image;
        import com.lowagie.text.Rectangle;
        import com.lowagie.text.pdf.BaseFont;
        import com.lowagie.text.pdf.PdfContentByte;
        import com.lowagie.text.pdf.PdfReader;
        import com.lowagie.text.pdf.PdfStamper;


        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.util.ArrayList;

public class SignatureInPdf {
    private static final String BASE_FONT = "C:\\ptc\\Windchill_11.1cps05\\eclipse\\fonts\\GOST type A.ttf";
    private static final String SINGS_PATH = "C:\\ptc\\Windchill_11.1cps05\\Windchill\\codebase\\netmarkets\\images\\sings\\";

    public static void main(String[] args) {
        ArrayList<String> roles = new ArrayList<>(10);
        ArrayList<String> users = new ArrayList<>(10);
        ArrayList<String> date = new ArrayList<>(10);
        String filePath = "D:\\Ruselprom\\Projects\\pdf with signs\\РСШН.685421.002 СБ Катушка статора_r42.pdf";

        roles.add("Утвердить");
        roles.add("Согласовать");
        roles.add("Согласовать");
        roles.add("Назначить согласующих технологов");
        roles.add("Нормоконтроль");
        roles.add("Проверить");

        users.add("Кащенков А.В.");
        users.add("Фомичёв А.В.");
        users.add("Филиппова Т.В.");
        users.add("Евгеньев В.В.");
        users.add("Грачев Н.И.");
        users.add("Пархомчук В.А.");

        date.add("01.01.20");
        date.add("02.02.20");
        date.add("03.03.20");
        date.add("04.04.20");
        date.add("05.05.20");
        date.add("06.06.20");

        signTheDocument(filePath, roles, users, date);
    }

    public static void signTheDocument(String filePath, ArrayList<String> roles, ArrayList<String> users, ArrayList<String> date) {
        try {
            PdfReader reader = new PdfReader(new FileInputStream(filePath));
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("D:\\Ruselprom\\Projects\\pdf with signs\\РСШН.pdf"));

            PdfContentByte stream = stamper.getOverContent(1);
            stream.beginText();
            BaseFont bf = BaseFont.createFont(BASE_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Rectangle pageSize = reader.getPageSize(1);

            float x, y;
            int userPosition;
            for (int i = 0; i < 5; i++) {
                userPosition = getPositionRole(roles, i);
                stream.setFontAndSize(bf, 12.0F);
                x = pageSize.getRight() - 486;
                y = pageSize.getBottom() + 87 - getDeltaY(i);
                stream.setTextMatrix(x, y);
                stream.showText(users.get(userPosition));

                Image waterMarkImage = Image.getInstance(SINGS_PATH + users.get(userPosition) + ".png");
                x = pageSize.getRight() - 415;
                waterMarkImage.scaleAbsolute(25, 25);
                waterMarkImage.setAbsolutePosition(x, (y - 10));
                stream.addImage(waterMarkImage);

                stream.setFontAndSize(bf, 8.0F);
                x = pageSize.getRight() - 380;
                stream.setTextMatrix(x, y);
                stream.showText(date.get(userPosition));
            }
            stream.endText();
            stamper.setFullCompression();
            stamper.close();
            reader.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private static int getDeltaY(int i) {
        final int[] y = new int[]{0, 14, 27, 56, 70};
        return y[i];
    }

    private static int getPositionRole(ArrayList<String> roles, int i) {
        final String[] allRoles = new String[]{
                "Назначить согласующих технологов",
                "Проверить",
                "Согласовать",
                "Нормоконтроль",
                "Утвердить"
        };

        return roles.indexOf(allRoles[i]);
    }
}

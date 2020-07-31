package ru.ruselprom.signs;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import ru.ruselprom.signs.data.PdfData;
import ru.ruselprom.signs.data.UserData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class SignatureFactory {
    private static final String BASE_FONT = "C:\\ptc\\Windchill_11.1cps05\\eclipse\\fonts\\GOST type A.ttf";
    private static final String SINGS_PATH = "C:\\ptc\\Windchill_11.1cps05\\Windchill\\codebase\\netmarkets\\images\\sings\\";
    private List<String> roles;
    private List<String> users;
    private List<String> dates;

    public SignatureFactory(UserData userData) {
        this.roles = userData.getRoles();
        this.users = userData.getUsers();
        this.dates = userData.getDates();
    }

    public String signPdfDocument(PdfData pdfData) throws IOException, DocumentException {
        PdfReader reader = null;
        PdfStamper stamper = null;
        try (FileOutputStream fileOutputStream =
                     new FileOutputStream(pdfData.getPdfPath() + File.separator + pdfData.getPdfName())) {
            reader = new PdfReader(pdfData.getPdfStream());
            stamper = new PdfStamper(reader, fileOutputStream);

            PdfContentByte stream = stamper.getOverContent(1);
            stream.beginText();
            BaseFont bf = BaseFont.createFont(BASE_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Rectangle pageSize = reader.getPageSize(1);

            float x;
            float y;
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
                stream.showText(dates.get(userPosition));
            }
            stream.endText();
            stamper.setFullCompression();
            return "success";
        } catch (IOException | DocumentException e) {
            return e.toString();
        } finally {
            if (stamper != null) {
                stamper.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    private int getDeltaY(int i) {
        final int[] y = new int[]{0, 14, 27, 56, 70};
        return y[i];
    }

    private int getPositionRole(List<String> roles, int i) {
        final String[] allRoles = new String[]{
                PromotionNoticeProcess.DEV_ROLE,
                "Проверить",
                "Согласовать",
                "Нормоконтроль",
                "Утвердить"
        };

        return roles.indexOf(allRoles[i]);
    }
}

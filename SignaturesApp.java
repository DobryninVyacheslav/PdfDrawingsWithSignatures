package ru.ruselprom.signs;

import com.lowagie.text.DocumentException;
import ru.ruselprom.signs.data.PdfData;
import ru.ruselprom.signs.data.UserData;
import ru.ruselprom.signs.exceptions.NullValueException;
import ru.ruselprom.signs.exceptions.SignaturesAppRuntimeException;


import java.io.IOException;

public class SignaturesApp {
    public String start(String oid, String filePath) {
        try {
            WncDrawing drawing = new WncDrawing(oid);
            Representation representation = new Representation(drawing);
            PdfData pdfData = representation.getDrwPdfData();
            pdfData.setPdfPath(filePath);
            PromotionNoticeProcess process = new PromotionNoticeProcess();
            UserData userData = process.getUserDataByOidOfDrw(oid);
            SignatureFactory signatureInPdf = new SignatureFactory(userData);
            return signatureInPdf.signPdfDocument(pdfData);
        } catch (DocumentException | NullValueException | IOException | SignaturesAppRuntimeException e) {
            return e.toString();
        } catch (ArrayIndexOutOfBoundsException e) {
            StringBuilder stringBuilder = new StringBuilder();
            StackTraceElement[] stackTrace = e.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                stringBuilder.append(stackTraceElement).append("\n");
            }
            return stringBuilder.toString();
        }
    }
}

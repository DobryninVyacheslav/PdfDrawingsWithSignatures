package ru.ruselprom.signs;

import com.lowagie.text.DocumentException;
import ru.ruselprom.signs.data.PdfData;
import ru.ruselprom.signs.data.UserData;
import ru.ruselprom.signs.exceptions.SignaturesAppRuntimeException;

import java.io.IOException;

public class SignaturesApp {
    public String start(String oid, String filePath) {
        try {
            WncDrawing drawing = new WncDrawing(oid);
            Representation representation = new Representation(drawing);
            PromotionNoticeProcess process = new PromotionNoticeProcess();
            UserData userData = process.getUserDataByOidOfDrw(oid);
            PdfData pdfData = representation.getDrwPdfData();
            pdfData.setPdfPath(filePath);
            SignatureFactory signatureInPdf = new SignatureFactory(userData);
            return signatureInPdf.signPdfDocument(pdfData);
        } catch (DocumentException e) {
            return e.toString();
        } catch (IOException e) {
            return e.toString();
        } catch (SignaturesAppRuntimeException e) {
            return e.toString();
        }
    }
}

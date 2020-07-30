package ru.ruselprom.signs;

import com.lowagie.text.DocumentException;
import ru.ruselprom.signs.data.PdfData;
import ru.ruselprom.signs.data.UserData;

import java.io.IOException;

public class SignsApp {
    public String start(String oid, String filePath) {
        try {
            PromotionNoticeProcess process = new PromotionNoticeProcess();
            UserData userData = process.getUserDataByOidOfDrw(oid);
            PdfData pdfData = Representation.getPdfDataByOidOfDrw(oid);
            pdfData.setPdfPath(filePath);
            SignatureInPdf signatureInPdf = new SignatureInPdf(userData);
            return signatureInPdf.signTheDocument(pdfData);
        } catch (DocumentException e) {
            return e.toString();
        } catch (IOException e) {
            return e.toString();
        }
    }
}

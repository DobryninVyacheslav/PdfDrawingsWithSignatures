package ru.ruselprom.signs;

import ru.ruselprom.signs.data.PdfData;
import ru.ruselprom.signs.exceptions.NullValueException;
import ru.ruselprom.signs.exceptions.SignaturesAppRuntimeException;
import wt.content.*;
import wt.fc.Persistable;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.util.WTException;

import java.beans.PropertyVetoException;

public class Representation {

    private WncDrawing drawing;
    private Persistable representation;

    public Representation(WncDrawing drawing) {
        this.drawing = drawing;
        this.representation = getRepresentationByOid(drawing.getRepresentationOid());
    }

    public PdfData getDrwPdfData() {
        try {
            ApplicationData pdfAppData = getPdfAppData();
            Streamed streamed = (Streamed) pdfAppData.getStreamData().getObject();
            PdfData pdfData = new PdfData();
            pdfData.setPdfName(drawing.getPdfFileName());
            pdfData.setPdfStream(streamed.retrieveStream());
            return pdfData;
        } catch (PropertyVetoException | WTException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ApplicationData getPdfAppData() throws WTException, PropertyVetoException {
        FormatContentHolder holder = (FormatContentHolder)ContentHelper
                .service.getContents((ContentHolder) representation);
        QueryResult result = ContentHelper.service
                .getContentsByRole(holder, ContentRoleType.SECONDARY, false);
        ApplicationData data = null;
        while (result.hasMoreElements()) {
            data = (ApplicationData) result.nextElement();
            if (data.getBusinessType().equalsIgnoreCase("pdf")) {
                break;
            }
        }
        if (data == null) {
            throw new SignaturesAppRuntimeException("pdf file not found!");
        }
        return data;
    }

    private Persistable getRepresentationByOid(String oid) {
        try {
            return new ReferenceFactory().getReference(oid).getObject();
        } catch (WTException e) {
            throw new SignaturesAppRuntimeException("Error while trying to get representation", e);
        }
    }
}

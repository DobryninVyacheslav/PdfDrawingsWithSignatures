package ru.ruselprom.signs;

import ru.ruselprom.signs.data.PdfData;
import ru.ruselprom.signs.exceptions.NullValueException;
import wt.content.*;
import wt.epm.EPMDocument;
import wt.fc.Persistable;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.util.WTException;
import wt.wvs.WVSLoggerHelper;

import java.beans.PropertyVetoException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Representation {
    private static Persistable persDrw;

    private Representation() {}

    public static PdfData getPdfDataByOidOfDrw(String oid) {
        try {
            persDrw = getObjectByOid(oid);
            ApplicationData pdfAppData = getPdfAppData(getRelatedObjName());
            Streamed streamed = (Streamed) pdfAppData.getStreamData().getObject();

            PdfData pdfData = new PdfData();
            pdfData.setPdfName(getPdfFileName());
            pdfData.setPdfStream(streamed.retrieveStream());
            return pdfData;
        } catch (PropertyVetoException | WTException | NullPointerException | ru.ruselprom.signs.exceptions.NullValueException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getPdfFileName() {
        EPMDocument epmDoc = (EPMDocument) persDrw;
        String number = epmDoc.getNumber();

        Pattern pattern = Pattern.compile("\\D{2,3}$|\\D\\d$");
        Matcher matcher = pattern.matcher(number);

        String suffix = "";
        if (matcher.find()) {
            suffix = number.substring(matcher.start(),matcher.end()) + " ";
            number = number.substring(0, matcher.start());
        }
        StringBuilder pdfFileName = new StringBuilder();
        pdfFileName.append(number).append(" ").append(suffix)
                .append(epmDoc.getName().trim()).append("_r")
                .append(String.format("%02d", epmDoc.getVersionIdentifier().getValue()))
                .append(".pdf");
        return pdfFileName.toString();
    }

    private static String getRelatedObjName() {
        String objName = WVSLoggerHelper.getRelatedObjects(persDrw, false);
        return objName.substring(objName.indexOf('(') + 1, objName.indexOf(')'));
    }

    private static ApplicationData getPdfAppData(String oid) throws WTException, PropertyVetoException, ru.ruselprom.signs.exceptions.NullValueException {
        Persistable persRepresentation = getObjectByOid(oid);
        FormatContentHolder holder = (FormatContentHolder)
                ContentHelper.service.getContents((ContentHolder) persRepresentation);
        QueryResult result = ContentHelper.service.getContentsByRole(holder, ContentRoleType.SECONDARY, false);
        ApplicationData data = null;
        while (result.hasMoreElements()) {
            data = (ApplicationData) result.nextElement();
            if (data.getBusinessType().equalsIgnoreCase("pdf")) {
                break;
            }
        }
        if (data == null) {
            throw new NullValueException("null in ApplicationData");
        }
        return data;
    }

    private static Persistable getObjectByOid(String oid) throws WTException {
        return new ReferenceFactory().getReference(oid).getObject();
    }
}

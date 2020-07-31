package ru.ruselprom.signs;

import ru.ruselprom.signs.exceptions.SignaturesAppRuntimeException;
import wt.epm.EPMDocument;
import wt.fc.Persistable;
import wt.fc.ReferenceFactory;
import wt.util.WTException;
import wt.wvs.WVSLoggerHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WncDrawing {

    private Persistable drawing;

    public WncDrawing(String oid) {
        this.drawing = getDrawingByOid(oid);
    }

    public String getRepresentationOid() {
        String objName = WVSLoggerHelper.getRelatedObjects(drawing, false);
        return objName.substring(objName.indexOf('(') + 1, objName.indexOf(')'));
    }

    public String getPdfFileName() {
        EPMDocument epmDoc = (EPMDocument) drawing;
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

    private Persistable getDrawingByOid(String oid) {
        try {
            return new ReferenceFactory().getReference(oid).getObject();
        } catch (WTException e) {
            throw new SignaturesAppRuntimeException("Error while trying to get drawing", e);
        }
    }
}

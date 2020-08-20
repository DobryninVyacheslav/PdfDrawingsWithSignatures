package ru.ruselprom.signs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.ruselprom.signs.exceptions.SignaturesAppRuntimeException;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentType;
import wt.fc.ReferenceFactory;
import wt.util.WTException;
import wt.wvs.WVSLoggerHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WncDrawing {

    private EPMDocument drawing;

    @Autowired
    public WncDrawing(@Qualifier("oid") String oid) {
        this.drawing = getDrawingByOid(oid);
        checkDocType();
        checkLifeCycleState();
    }

    private void checkLifeCycleState() {
        String state = drawing.getLifeCycleState().getDisplay();
        if (!state.equalsIgnoreCase("APPROVED") &&
            !state.equalsIgnoreCase("RELEASED") &&
            !state.equalsIgnoreCase("CANCELED")) {
            throw new SignaturesAppRuntimeException("Чертеж(ы) не подписан(ы)!");
        }
    }

    private void checkDocType() {
        EPMDocumentType currentType = drawing.getDocType();
        EPMDocumentType drawingType = EPMDocumentType.toEPMDocumentType("CADDRAWING");
        if (currentType != drawingType) {
            throw new SignaturesAppRuntimeException("Неправильный тип документа. Требуется чертеж!");
        }
    }

    public String getRepresentationOid() {
        String objName = WVSLoggerHelper.getRelatedObjects(drawing, false);
        return objName.substring(objName.indexOf('(') + 1, objName.indexOf(')'));
    }

    public String getPdfFileName() {
        String number = drawing.getNumber();

        Pattern pattern = Pattern.compile("\\D{2,3}$|\\D\\d$");
        Matcher matcher = pattern.matcher(number);

        String suffix = "";
        if (matcher.find()) {
            suffix = number.substring(matcher.start(),matcher.end()) + " ";
            number = number.substring(0, matcher.start());
        }
        StringBuilder pdfFileName = new StringBuilder();
        pdfFileName.append(number).append(" ").append(suffix)
                .append(drawing.getName().trim()).append("_r")
                .append(String.format("%02d", Integer.parseInt(drawing.getVersionIdentifier().getValue())))
                .append(".pdf");
        return pdfFileName.toString();
    }

    private EPMDocument getDrawingByOid(String oid) {
        try {
            return (EPMDocument) new ReferenceFactory().getReference(oid).getObject();
        } catch (WTException e) {
            throw new SignaturesAppRuntimeException("Error while trying to get drawing", e);
        }
    }
}

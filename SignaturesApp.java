package ru.ruselprom.signs;

import com.lowagie.text.DocumentException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.ruselprom.signs.data.InputData;
import ru.ruselprom.signs.data.PdfData;
import ru.ruselprom.signs.data.UserData;
import ru.ruselprom.signs.exceptions.NullValueException;
import ru.ruselprom.signs.exceptions.SignaturesAppRuntimeException;


import java.io.IOException;

public class SignaturesApp {
    public String start(String oid, String filePath) {
        InputData.setData(oid, filePath);
        try (AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext(SpringConfig.class)) {
            SignatureFactory signatureFactory = context.getBean("signatureFactory", SignatureFactory.class);
            return signatureFactory.signPdfDocument();
        } catch (DocumentException | IOException | SignaturesAppRuntimeException e) {
            return e.toString();
        } catch (ArrayIndexOutOfBoundsException e) {
            StringBuilder stringBuilder = new StringBuilder();
            StackTraceElement[] stackTrace = e.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                stringBuilder.append(stackTraceElement).append("\n");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }
}

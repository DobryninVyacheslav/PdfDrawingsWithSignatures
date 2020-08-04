package ru.ruselprom.signs;

import wt.content.ApplicationData;
import wt.content.Streamed;
import wt.org.OrganizationServicesHelper;
import wt.org.WTUser;
import wt.org.electronicIdentity.ElectronicSignature;
import wt.util.WTException;

import java.beans.PropertyVetoException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SignatureImage {

    private SignatureImage() {
        throw new IllegalStateException("Utility class");
    }

    public static byte[] getByUser(WTUser user) {
        try {
            ElectronicSignature signature = OrganizationServicesHelper.manager.getUserSignature(user);
            if (signature != null) {
                ApplicationData applicationData = OrganizationServicesHelper.manager.getUserSignatureApplicationData(signature);
                if (applicationData != null) {
                    Streamed streamed = (Streamed) applicationData.getStreamData().getObject();
                    try (InputStream is = streamed.retrieveStream()) {
                        return toByteArray(is);
                    }
                }
            }
            return new byte[0];
        } catch (WTException | PropertyVetoException | IOException e) {
            return new byte[0];
        }
    }


    private static byte[] toByteArray(InputStream is) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] b = new byte[4096];
            int n;
            while((n = is.read(b)) != -1) {
                output.write(b, 0, n);
            }
            return output.toByteArray();
        }
    }
}

package ru.ruselprom.signs;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import org.apache.commons.lang3.StringUtils;
import ru.ruselprom.signs.data.PdfData;
import ru.ruselprom.signs.data.UserData;
import ru.ruselprom.signs.exceptions.SignaturesAppRuntimeException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SignatureFactory {
    private static final String SINGS_PATH = "C:\\ptc\\Windchill_11.1cps05\\Windchill\\codebase\\netmarkets\\images\\sings\\";
    private String baseFont;
    private List<RoleData> roleDataList;
    private List<String> roles;
    private List<String> users;
    private List<String> dates;

    public SignatureFactory(UserData userData) {
        roleDataList = Arrays.asList(new RoleData(PromotionNoticeProcess.DEV_ROLE,0),
                                     new RoleData("Проверить",14),
                                     new RoleData("Согласовать",27),
                                     new RoleData("Нормоконтроль",56),
                                     new RoleData("Утвердить",70));
        this.roles = userData.getRoles();
        this.users = userData.getUsers();
        this.dates = userData.getDates();
        this.baseFont = FontPath.get();
        checkUserData();
    }

    private void checkUserData() {
        if (roles == null || users == null || dates == null) {
            throw new SignaturesAppRuntimeException("userData is null");
        }
        if (roles.size() != users.size() || dates.size() != users.size()) {
            throw new SignaturesAppRuntimeException("userData is fail");
        }
    }

    public String signPdfDocument(PdfData pdfData) throws IOException, DocumentException {
        PdfReader reader = null;
        PdfStamper stamper = null;
        try {
            reader = new PdfReader(pdfData.getPdfStream());
            stamper = new PdfStamper(reader,
                    new FileOutputStream(pdfData.getPdfPath() + File.separator + pdfData.getPdfName()));

            PdfContentByte stream = stamper.getOverContent(1);
            stream.beginText();
            BaseFont bf = BaseFont.createFont(baseFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Rectangle pageSize = reader.getPageSize(1);

            float x;
            float y;
            StringBuilder addedRoles = new StringBuilder();
            for (int i = 0; i < users.size(); i++) {
                RoleData roleData = getRoleData(roles.get(i));
                if (roleData == null) {
                    continue;
                }
                if (addedRoles.toString().contains(roleData.roleName)) {
                    if (roleData.roleName.equals("Согласовать") &&
                            StringUtils.countMatches(addedRoles, "Согласовать") == 1) {
                        roleData.deltaY = roleData.deltaY + 14;
                    } else {
                        continue;
                    }
                }
                addedRoles.append(roleData.roleName);
                stream.setFontAndSize(bf, 12.0F);
                x = pageSize.getRight() - 486;
                y = pageSize.getBottom() + 87 - roleData.deltaY;
                stream.setTextMatrix(x, y);
                stream.showText(users.get(i));

                Image waterMarkImage = Image.getInstance(SINGS_PATH + users.get(i) + ".png");
                x = pageSize.getRight() - 415;
                waterMarkImage.scaleAbsolute(25, 25);
                waterMarkImage.setAbsolutePosition(x, (y - 10));
                stream.addImage(waterMarkImage);

                stream.setFontAndSize(bf, 8.0F);
                x = pageSize.getRight() - 380;
                stream.setTextMatrix(x, y);
                stream.showText(dates.get(i));
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

    private RoleData getRoleData(String role) {
        for (RoleData roleData : roleDataList) {
            if (role.contains(roleData.roleName)) {
                return roleData;
            }
        }
        return null;
    }

    private class RoleData {
        private String roleName;
        private int deltaY;

        public RoleData(String roleName, int deltaY) {
            this.roleName = roleName;
            this.deltaY = deltaY;
        }
    }
}

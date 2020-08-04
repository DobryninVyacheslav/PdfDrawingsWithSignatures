package ru.ruselprom.signs;

import ru.ruselprom.signs.exceptions.SignaturesAppRuntimeException;

import java.io.IOException;
import java.io.File;
import java.net.URLDecoder;

public class FontPath {
    public static final String FONT_FILE_NAME = "ГОСТ_тип_А.ttf";

    public static String get() {
        try {
            File currentClass = new File(URLDecoder.decode(FontPath.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath(), "UTF-8"));
            return currentClass.getAbsolutePath() + "\\ru\\ruselprom\\fonts\\" + FONT_FILE_NAME;
        } catch (IOException e) {
            throw new SignaturesAppRuntimeException("Font file not found");
        }
    }
}

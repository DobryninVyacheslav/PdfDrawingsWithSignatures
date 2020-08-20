package ru.ruselprom.signs.data;

public class InputData {
    private static String oid;
    private static String filePath;

    private InputData() {}

    public static void setData(String oid, String filePath) {
        InputData.oid = oid;
        InputData.filePath = filePath;
    }

    public static String getOid() {
        return oid;
    }
    public static String getFilePath() {
        return filePath;
    }
}

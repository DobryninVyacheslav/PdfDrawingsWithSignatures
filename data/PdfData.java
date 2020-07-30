package ru.ruselprom.signs.data;

import java.io.InputStream;

public class PdfData {
    private String pdfName;
    private InputStream pdfStream;

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public InputStream getPdfStream() {
        return pdfStream;
    }

    public void setPdfStream(InputStream pdfStream) {
        this.pdfStream = pdfStream;
    }
}

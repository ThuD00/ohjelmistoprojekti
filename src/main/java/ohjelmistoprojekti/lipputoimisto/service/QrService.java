package ohjelmistoprojekti.lipputoimisto.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QrService {
    QRCodeWriter qrWriter = new QRCodeWriter();
    public byte[] generateQrCode(String lippuKoodi) {

        try {
            BitMatrix matrix = qrWriter.encode(lippuKoodi, BarcodeFormat.QR_CODE, 420, 420);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix,"PNG", out);
            return out.toByteArray();
        } catch (WriterException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Virhe generoidessa QR-Koodia: " + e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "IOException: " + e);
        }
    }
}

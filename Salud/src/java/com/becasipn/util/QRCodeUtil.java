/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.util;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Patricia Benitez
 */
public class QRCodeUtil {

    public static String escribeQR(String txt, String folio) {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = writer.encode(txt, BarcodeFormat.QR_CODE, 200, 200);
            String img = UtilFile.getPathQR() + "/QR" + folio + ".gif";
            //System.out.println(img);
            MatrixToImageWriter.writeToFile(bitMatrix, "gif", new File(img));

            return img;
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage escribeQRCredencial(String txt) {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        BufferedImage imagenQR = null;

        try {
            bitMatrix = writer.encode(txt, BarcodeFormat.QR_CODE, 200, 200);
            imagenQR = MatrixToImageWriter.toBufferedImage(bitMatrix);

            return imagenQR;
        } catch (WriterException e) {
            return null;
        }
    }
}

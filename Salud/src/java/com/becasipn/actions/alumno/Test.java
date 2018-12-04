package com.becasipn.actions.alumno;

import com.becasipn.util.UtilFile;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author User-03
 */
public class Test {

    public static void main(String[] args) {

        
        
        File finput = new File("C:\\Temp\\test.pdf");

        String filePath = "C:\\Temp\\";
        try {
            File f = new File(filePath + "ESE.pdf");
            InputStream input = new ByteArrayInputStream(UtilFile.getBytesFromFile(finput));
            saveFile(f, input);

        } catch (Exception ex) {

        }

    }

    public static void saveFile(File f, InputStream istream) throws IOException {
        InputStream inputStream = istream;
        OutputStream out = null;
        try {
            out = new FileOutputStream(f);
        } catch (Exception e) {
            //f.createNewFile();
            try {
                // A partir del objeto File creamos el fichero físicamente
                if (f.createNewFile()) {
                    System.out.println("El fichero se ha creado correctamente");
                } else {
                    System.out.println("No ha podido ser creado el fichero");
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            out = new FileOutputStream(f);
        }
        byte buf[] = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        inputStream.close();

    }
}

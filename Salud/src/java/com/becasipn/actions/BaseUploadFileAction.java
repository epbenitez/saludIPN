package com.becasipn.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class BaseUploadFileAction extends BaseAction {

    private File upload;
    private String uploadFileName;
    private String uploadContentType;

    /**
     * Inicializa el objeto <code>BaseUploadFileAction</code>.
     */
    public BaseUploadFileAction() {
    }

    /**
     * Guarda el InputStream de una imagen en un Archivo.
     *
     * @param f Archivo de la imagen a guardar.
     * @param image Contenido del archivo.
     * @throws IOException
     * @see File
     * @see InputStream
     */
    public void saveImage(File f, InputStream image) throws IOException {
        InputStream inputStream = image;
        OutputStream out = null;
        try {
            out = new FileOutputStream(f);
        } catch (Exception e) {
            String dirName = f.getPath().split(f.getName())[0];
            //System.out.println(dirName);
            boolean success = (new File(dirName)).mkdirs();
            f.createNewFile();
            out = new FileOutputStream(f);
        }
        byte buf[] = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        inputStream.close();
        LOG.debug("File " + f.getName() + " created.");
    }

    /**
     * Obtiene el valor de la variable upload.
     *
     * @return el valor de la variable upload.
     */
    public File getUpload() {
        return upload;
    }

    /**
     * Establece el valor de la variable upload.
     *
     * @param upload nuevo valor de la variable upload.
     */
    public void setUpload(File upload) {
        this.upload = upload;
    }

    /**
     * Obtiene el valor de la variable uploadFileName.
     *
     * @return el valor de la variable uploadFileName.
     */
    public String getUploadFileName() {
        return uploadFileName;
    }

    /**
     * Establece el valor de la variable uploadFileName.
     *
     * @param uploadFileName nuevo valor de la variable uploadFileName.
     */
    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    /**
     * Obtiene el valor de la variable uploadContentType.
     *
     * @return el valor de la variable uploadContentType.
     */
    public String getUploadContentType() {
        return uploadContentType;
    }

    /**
     * Establece el valor de la variable uploadContentType.
     *
     * @param uploadContentType nuevo valor de la variable uploadContentType.
     */
    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    /**
     * Muestra el formulario de carga de un archivo.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción.
     */
    public String showUpload() {
        LOG.debug("Loading showUpload Action");
        return INPUT;
    }
}

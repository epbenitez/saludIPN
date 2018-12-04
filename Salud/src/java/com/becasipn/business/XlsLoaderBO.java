package com.becasipn.business;

import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.service.Service;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Definición de acciones que hagan uso de la carga de archivos XLS o XLSX
 *
 */
public abstract class XlsLoaderBO extends BaseBO {

    public XlsLoaderBO(Service service) {
        super(service);
    }

    public XlsLoaderBO() {
    }

    public Workbook readXlsFile(FileInputStream xls) throws IOException {
        Workbook wb = new HSSFWorkbook(xls);
        return wb;
    }

    public Workbook readXlsFile(FileInputStream xls, String password) throws IOException, GeneralSecurityException {
        org.apache.poi.hssf.record.crypto.Biff8EncryptionKey.setCurrentUserPassword(password);
        Biff8EncryptionKey.setCurrentUserPassword(password);
        POIFSFileSystem fs = new POIFSFileSystem(xls);
        HSSFWorkbook hwb = new HSSFWorkbook(fs.getRoot(), true);
        Biff8EncryptionKey.setCurrentUserPassword(null);
        return hwb;
    }

    public Workbook readXlsxFile(FileInputStream xlsx) throws IOException {
        Workbook wb = new XSSFWorkbook(xlsx);
        return wb;
    }

    public Workbook readXlsxFile(FileInputStream xlsx, String password) throws IOException, GeneralSecurityException {
        org.apache.poi.hssf.record.crypto.Biff8EncryptionKey.setCurrentUserPassword(password);
        POIFSFileSystem fs = new POIFSFileSystem(xlsx);
        EncryptionInfo info = new EncryptionInfo(fs);
        Decryptor d = Decryptor.getInstance(info);
        d.verifyPassword(Decryptor.DEFAULT_PASSWORD);
        Workbook wb = new XSSFWorkbook(d.getDataStream(fs));
        return wb;
    }

    public abstract <T> List<T> processFile(Workbook wb, String lote, Date fecha) throws Exception;

    public abstract <T> List<T> processFile(Workbook wb) throws Exception;

    public abstract <T> List<T> processFile(Workbook wb, BigDecimal periodo) throws Exception;
    
    public abstract <T> List<T> processFile(Workbook wb, BigDecimal periodo, Integer accion) throws Exception;
    
    public abstract <T> List<T> processFile(Workbook wb, UnidadAcademica unidadAcademica) throws Exception;
}
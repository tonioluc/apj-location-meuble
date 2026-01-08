package rapport;

import bean.AdminGen;
import bean.CGenUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilitaire.Utilitaire;
import vente.VenteDetailsLib;
import vente.VenteLib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

public class ImpressionPdf {

    String nomJasper = "";

    String BASE_DIR = System.getProperty("jboss.server.base.dir") + "/deployments/dossier.war/asynclocation.war";
    String reportPath = System.getProperty("jboss.server.base.dir") + "/deployments/asynclocation.war/report/";

    public String getNomJasper() {
        return nomJasper;
    }

    public void setNomJasper(String nomJasper) {
        this.nomJasper = nomJasper;
    }

    public String vente_liste(String daty1, String daty2, String nomPdf, Connection c) throws IOException, JRException, Exception {
        Map<String, Object> param = new HashMap<>();
        List<VenteLib> dataSource = new ArrayList<>();
        String awhere = "";

        VenteLib v = new VenteLib();
        v.setNomTable("VENTE_CPL");
        if ((daty1 != null && !daty1.trim().isEmpty()) || (daty2 != null && !daty2.trim().isEmpty())) {
            if (daty1 != null && !daty1.trim().isEmpty()) {
                awhere += " and daty >= to_date('" + daty1 + "', 'YYYY-MM-DD') ";
                param.put("datymin", daty1);
            }
            if (daty2 != null && !daty2.trim().isEmpty()) {
                awhere += " and daty <= to_date('" + daty2 + "', 'YYYY-MM-DD') ";
                param.put("datymax", daty2);
            }
        }

        VenteLib[] enc_mere = (VenteLib[]) CGenUtil.rechercher(v, null, null, c, awhere);
        dataSource.addAll(Arrays.asList(enc_mere));

        setNomJasper("facture_vente_mere");
        String jasperFile = reportPath + getNomJasper() + ".jasper";
        nomPdf = pdfUrl(nomPdf, daty2);
        nomPdf += ".pdf";
        String outputPath = this.BASE_DIR+"/"+nomPdf;
        File out = new File(this.BASE_DIR);
        if(!out.exists()){
            out.mkdirs();
        }

        File reportFile = new File(jasperFile);
        if (!reportFile.exists()) {
            throw new IOException("Fichier Jasper introuvable : " + jasperFile);
        }

        JRBeanCollectionDataSource jrDataSource = new JRBeanCollectionDataSource(dataSource);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile, param, jrDataSource);

        JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

        System.out.println("PDF généré avec succès : " + outputPath);

        return outputPath;
    }

    public String vente_liste_mere_fille(String daty1, String daty2, String nomPdf, Connection c) throws IOException, JRException, Exception {
        Map param = new HashMap();
        List dataSource = new ArrayList();
        String awhere="";
        VenteLib v = new VenteLib();
        v.setNomTable("VENTE_CPL");
        if ((daty1 != null && !daty1.trim().isEmpty()) || (daty2 != null && !daty2.trim().isEmpty())) {
            if (daty1 != null && !daty1.trim().isEmpty()) {
                awhere += " and daty >= to_date('" + daty1 + "', 'YYYY-MM-DD') ";
                param.put("datymin", daty1);
            }
            if (daty2 != null && !daty2.trim().isEmpty()) {
                awhere += " and daty <= to_date('" + daty2 + "', 'YYYY-MM-DD') ";
                param.put("datymax", daty2);
            }
        }
        VenteLib[] enc_mere = (VenteLib[]) CGenUtil.rechercher(v, null, null, c, awhere);
        Map<String, List<VenteDetailsLib>> listedetailsMap = new HashMap<>();
        for (VenteLib venteMere : enc_mere) {
            VenteDetailsLib vf = new VenteDetailsLib();
            vf.setNomTable("VENTE_DETAILS_CPL");
            vf.setIdVente(venteMere.getId());
            VenteDetailsLib[] enc_fille = (VenteDetailsLib[]) CGenUtil.rechercher(vf, null, null, null, "");
            if (enc_fille != null) {
                listedetailsMap.put(venteMere.getId(), Arrays.asList(enc_fille));
            } else {
                listedetailsMap.put(venteMere.getId(), new ArrayList<>());
            }
        }
        param.put("listedetails", listedetailsMap);
        List<VenteLib> dataSourceMere = Arrays.asList(enc_mere);
        setNomJasper("facture_vente_mere_fille");

        String jasperFile = reportPath + getNomJasper() + ".jasper";
        nomPdf = pdfUrl(nomPdf, daty2);
        nomPdf += ".pdf";
        String outputPath = this.BASE_DIR + "/" + nomPdf;

        File out = new File(this.BASE_DIR);
        if (!out.exists()) {
            out.mkdirs();
        }

        File reportFile = new File(jasperFile);
        if (!reportFile.exists()) {
            throw new IOException("Fichier Jasper introuvable : " + jasperFile);
        }

        JRBeanCollectionDataSource jrDataSource = new JRBeanCollectionDataSource(dataSourceMere);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile, param, jrDataSource);

        JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

        System.out.println("PDF mère/fille généré avec succès : " + outputPath);

        return outputPath;
    }

    public String vente_liste_excel(String daty1, String daty2, String nomExcel, Connection c) throws IOException, JRException, Exception {
        Map param = new HashMap();
        List dataSource = new ArrayList();
        String awhere = "";

        VenteLib v = new VenteLib();
        v.setNomTable("VENTE_CPL");
        if ((daty1 != null && !daty1.trim().isEmpty()) || (daty2 != null && !daty2.trim().isEmpty())) {
            if (daty1 != null && !daty1.trim().isEmpty()) {
                awhere += " and daty >= to_date('" + daty1 + "', 'YYYY-MM-DD') ";
                param.put("datymin", daty1);
            }
            if (daty2 != null && !daty2.trim().isEmpty()) {
                awhere += " and daty <= to_date('" + daty2 + "', 'YYYY-MM-DD') ";
                param.put("datymax", daty2);
            }
        }

        VenteLib[] enc_mere = (VenteLib[]) CGenUtil.rechercher(v, null, null, c, awhere);

        String[] libEntete = {
                "ID", "DESIGNATION", "CLIENT", "DEVISE", "DATE",
                "MONTANT TTC", "MONTANT REVIENT", "MARGE BRUTE",
                "MONTANT PAYE", "MONTANT RESTE", "ETAT"
        };


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ventes");


        CellStyle headerStyle = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        headerStyle.setFont(boldFont);
        headerStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headerStyle.setBorderRight(CellStyle.BORDER_THIN);


        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderTop(CellStyle.BORDER_THIN);
        dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
        dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
        dataStyle.setBorderRight(CellStyle.BORDER_THIN);


        CellStyle totalStyle = workbook.createCellStyle();
        totalStyle.setFont(boldFont);
        totalStyle.setBorderTop(CellStyle.BORDER_THIN);
        totalStyle.setBorderBottom(CellStyle.BORDER_THIN);
        totalStyle.setBorderLeft(CellStyle.BORDER_THIN);
        totalStyle.setBorderRight(CellStyle.BORDER_THIN);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < libEntete.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(libEntete[i]);
            cell.setCellStyle(headerStyle);
        }


        int rowNum = 1;
        for (VenteLib vente : enc_mere) {
            Row row = sheet.createRow(rowNum++);

            Cell c0 = row.createCell(0); c0.setCellValue(vente.getId()); c0.setCellStyle(dataStyle);
            Cell c1 = row.createCell(1); c1.setCellValue(vente.getDesignation()); c1.setCellStyle(dataStyle);
            Cell c2 = row.createCell(2); c2.setCellValue(vente.getIdClientLib()); c2.setCellStyle(dataStyle);
            Cell c3 = row.createCell(3); c3.setCellValue(vente.getIdDevise()); c3.setCellStyle(dataStyle);
            Cell c4 = row.createCell(4); c4.setCellValue(vente.getDaty() != null ? vente.getDaty().toString() : ""); c4.setCellStyle(dataStyle);
            Cell c5 = row.createCell(5); c5.setCellValue(vente.getMontantttc()); c5.setCellStyle(dataStyle);
            Cell c6 = row.createCell(6); c6.setCellValue(vente.getMontantRevient()); c6.setCellStyle(dataStyle);
            Cell c7 = row.createCell(7); c7.setCellValue(vente.getMargeBrute()); c7.setCellStyle(dataStyle);
            Cell c8 = row.createCell(8); c8.setCellValue(vente.getMontantpaye()); c8.setCellStyle(dataStyle);
            Cell c9 = row.createCell(9); c9.setCellValue(vente.getMontantreste()); c9.setCellStyle(dataStyle);
            Cell c10 = row.createCell(10); c10.setCellValue(vente.getEtatLib()); c10.setCellStyle(dataStyle);
        }


        double totalmontantttc = AdminGen.calculSommeDouble(enc_mere, "montantttc");
        double totalmontantRevient = AdminGen.calculSommeDouble(enc_mere, "montantRevient");
        double totalmargeBrute = AdminGen.calculSommeDouble(enc_mere, "margeBrute");
        double totalmontantpaye = AdminGen.calculSommeDouble(enc_mere, "montantpaye");
        double totalmontantreste = AdminGen.calculSommeDouble(enc_mere, "montantreste");

        Row totalRow = sheet.createRow(rowNum++);
        Cell totalLabelCell = totalRow.createCell(4);
        totalLabelCell.setCellValue("Total");
        totalLabelCell.setCellStyle(totalStyle);

        Cell totalTTC = totalRow.createCell(5);
        totalTTC.setCellValue(totalmontantttc);
        totalTTC.setCellStyle(totalStyle);

        Cell totalRevient = totalRow.createCell(6);
        totalRevient.setCellValue(totalmontantRevient);
        totalRevient.setCellStyle(totalStyle);

        Cell totalMarge = totalRow.createCell(7);
        totalMarge.setCellValue(totalmargeBrute);
        totalMarge.setCellStyle(totalStyle);

        Cell totalPaye = totalRow.createCell(8);
        totalPaye.setCellValue(totalmontantpaye);
        totalPaye.setCellStyle(totalStyle);

        Cell totalReste = totalRow.createCell(9);
        totalReste.setCellValue(totalmontantreste);
        totalReste.setCellStyle(totalStyle);


        for (int i = 0; i < libEntete.length; i++) {
            sheet.autoSizeColumn(i);
        }

        nomExcel = pdfUrl(nomExcel, daty2);
        nomExcel += ".xlsx";
        String outputPath = this.BASE_DIR + "/" + nomExcel;

        File out = new File(this.BASE_DIR);
        if (!out.exists()) {
            out.mkdirs();
        }

        try (FileOutputStream fileOut = new FileOutputStream(outputPath)) {
            workbook.write(fileOut);
        }

        workbook.close();
        return outputPath;
    }

    public static String pdfUrl(String nom, String daty) throws Exception{
        String heure = Utilitaire.heureCourante();
        String[] heures = heure.split(":");
        heure = heures[0]+""+heures[1];

        String fileId = daty.toString().replace("-","");
        String dir = nom+"-"+fileId+"-"+heure;

        return dir;
    }
}

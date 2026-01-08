/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package station.export;


import net.sf.jasperreports.engine.JRException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utilitaire.Utilitaire;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import vente.*;
import java.util.*;
import bean.*;
@WebServlet(name = "ExportExcel", urlPatterns = {"/ExportExcel"})
public class ExportExcel extends HttpServlet {

  

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, Exception, IOException, JRException {
        String action = request.getParameter("action");
         if(action.equalsIgnoreCase("vente_liste")){
            vente_liste(request, response);
         }
      
    }
  
    private void vente_liste(HttpServletRequest request, HttpServletResponse response) throws IOException, JRException, Exception {
        Connection c = null;
        Map param = new HashMap();
        List dataSource = new ArrayList();
    
        // Récupération des paramètres
        String id = request.getParameter("id");
        String designation = request.getParameter("designation");
        String idClientLib = request.getParameter("idClientLib");
        String daty1 = request.getParameter("daty1");
        String daty2 = request.getParameter("daty2");
        String awhere = "";
    
        VenteLib v = new VenteLib();
        v.setNomTable("VENTE_CPL");
        v.setId(id);
        v.setDesignation(designation);
        v.setIdClientLib(idClientLib);
    
        if ((daty1 != null && !daty1.trim().isEmpty()) || (daty2 != null && !daty2.trim().isEmpty())) {
            if (daty1 != null && !daty1.trim().isEmpty()) {
                awhere += " and daty >= to_date('" + daty1 + "', 'dd/mm/yyyy') ";
                param.put("datymin", daty1);
            }
            if (daty2 != null && !daty2.trim().isEmpty()) {
                awhere += " and daty <= to_date('" + daty2 + "', 'dd/mm/yyyy') ";
                param.put("datymax", daty2);
            }
        }
    
        VenteLib[] enc_mere = (VenteLib[]) CGenUtil.rechercher(v, null, null, null, awhere);

        String[] libEntete = {
            "ID", "DESIGNATION", "CLIENT", "DEVISE", "DATE",
            "MONTANT TTC", 
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
            Cell c6 = row.createCell(6); c6.setCellValue(vente.getMontantpaye()); c6.setCellStyle(dataStyle);
            Cell c7 = row.createCell(7); c7.setCellValue(vente.getMontantreste()); c7.setCellStyle(dataStyle);
            Cell c8 = row.createCell(8); c8.setCellValue(vente.getEtatLib()); c8.setCellStyle(dataStyle);
        }
    

        double totalmontantttc = AdminGen.calculSommeDouble(enc_mere, "montantttc");
        double totalmontantRevient = AdminGen.calculSommeDouble(enc_mere, "montantRevient");
        double totalmargeBrute = AdminGen.calculSommeDouble(enc_mere, "margeBrute");
        double totalmontantpaye = AdminGen.calculSommeDouble(enc_mere, "montantpaye");
        double totalmontantreste = AdminGen.calculSommeDouble(enc_mere, "montantreste");
    
        // Ligne Total
        Row totalRow = sheet.createRow(rowNum++);
        Cell totalLabelCell = totalRow.createCell(4);
        totalLabelCell.setCellValue("Total");
        totalLabelCell.setCellStyle(totalStyle);
    
        Cell totalTTC = totalRow.createCell(5);
        totalTTC.setCellValue(totalmontantttc);
        totalTTC.setCellStyle(totalStyle);
    
        /*Cell totalRevient = totalRow.createCell(6);
        totalRevient.setCellValue(totalmontantRevient);
        totalRevient.setCellStyle(totalStyle);
    
        Cell totalMarge = totalRow.createCell(7);
        totalMarge.setCellValue(totalmargeBrute);
        totalMarge.setCellStyle(totalStyle);*/
    
        Cell totalPaye = totalRow.createCell(6);
        totalPaye.setCellValue(totalmontantpaye);
        totalPaye.setCellStyle(totalStyle);
    
        Cell totalReste = totalRow.createCell(7);
        totalReste.setCellValue(totalmontantreste);
        totalReste.setCellStyle(totalStyle);
    

        for (int i = 0; i < libEntete.length; i++) {
            sheet.autoSizeColumn(i);
        }
    

        String fileName = "liste_ventes.xlsx";
    

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.flush();
        out.close();
    }
    
    
    
 
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ExportExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ExportExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
   
   
     


    
   
}

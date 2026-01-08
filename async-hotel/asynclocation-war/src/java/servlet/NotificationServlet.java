/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import bean.CGenUtil;
import utils.Notification;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.AWTException;
import user.UserEJB;


@WebServlet(name = "Notification", urlPatterns = {"/Notification"})
public class NotificationServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Notification</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Notification at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
        if(request.getParameter("notifSys")!=null){
            try{
            Notification.notifSys(request.getParameter("notifSys"));   
            }catch(AWTException ex){
                //throw ex;
            }                    
            return ;
        }

        UserEJB u = (UserEJB)request.getSession().getAttribute("u");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
 
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "86400");
             
        //Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
        Gson gson = new GsonBuilder().create();

        /*
         * String json = getJsonBody(request.getReader());
         * 
         * JsonObject object = new JsonParsere().parse(json).getAsJsonObject();
         */
        
        //UserEJB u = null;
        //u = (UserEJB) session.getAttribute("u");
        //String classefille = object.get("nb").getAsString();      
        
        Notification[]mesnotifs = null;
        Notification[] top5 =null;
        try{
            Notification notif = new Notification();
            notif.setNomTable("notificationlibnonlu");
            String where = " and receiver = '%s' order by etat asc, daty desc, heure desc";
            where = String.format(where, u.getUser().getRefuser());
            System.out.println("request===> select * from notificationlibnonlu where 1>0 "+where );
            
            mesnotifs = (Notification[])CGenUtil.rechercher(notif, null, null, where); 
            int n = Math.min(mesnotifs.length, 5);
            top5 = new Notification[n];
            for (int i = 0; i < n; i++) {
                top5[i] = mesnotifs[i];
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                throw ex;
            } catch (Exception ex1) {
                Logger.getLogger(NotificationServlet.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
        String countryObj = gson.toJson(top5);        
        //myObj.add("countryInfo", countryObj);
        out.println(countryObj);
 
        out.close();        
            
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

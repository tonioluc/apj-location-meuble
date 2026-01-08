package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.CGenUtil;
import utils.ConstanteAsync;
import utils.ConstanteStation;
import produits.Ingredients;

@WebServlet("/DownloadProduit")
public class DownloadProduit extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try{
            String id = (String) request.getParameter("id");
            Ingredients[] pris = (Ingredients[]) CGenUtil.rechercher(new Ingredients(),null,null, " AND id='"+id+"'");
            if(pris.length == 0){
                throw new Exception("Aucune image trouvee");
            }

            String filePath = ConstanteAsync.path_BarreCode_Image+pris[0].getFilepath();

            File file = new File(filePath);
            response.setContentType("image/png");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + "produit-" +pris[0].getId()+".png\"");
            response.setContentLength((int)file.length());
            try (FileInputStream fileInputStream = new FileInputStream(file);
                 OutputStream outputStream = response.getOutputStream()) {
                 
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

package servlet;

import bean.CGenUtil;
import com.google.gson.Gson;
import produits.Ingredients;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/ProduitServlet")
public class ProduitServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Créer une liste d’objets
        try {
            String [] ids = request.getParameterValues("ids");
            String awhere = "";
            if (ids != null && ids.length > 0) {
                awhere = " AND ID IN ('" + String.join("','", ids) + "')";
                awhere += " ORDER BY CASE ID ";
                for (int i = 0; i < ids.length; i++) {
                    awhere += "WHEN '" + ids[i] + "' THEN " + i + " ";
                }
                awhere += "END";
            }

            Ingredients [] ingredients = (Ingredients[]) CGenUtil.rechercher(new Ingredients(),null,null,null,awhere);
            String [] results = new String[ingredients.length];
            for (int i = 0; i < ingredients.length; i++) {
                results[i] = ingredients[i].getLibelle();
            }
            String json = new Gson().toJson(results);

            // Envoyer la réponse
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

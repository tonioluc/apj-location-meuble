package servlet.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import utils.ApiResponse;

import bean.CGenUtil;
import bean.TypeObjet;
import user.UserEJB;
import user.UserEJBClient;
import historique.MapUtilisateur;

/**
 * API REST pour l'authentification des utilisateurs.
 * 
 * Endpoint: POST /api/login
 * 
 * Body JSON:
 * {
 *   "identifiant": "username",
 *   "passe": "password",
 *   "interim": "optionnel",
 *   "service": "optionnel"
 * }
 * 
 * Réponse succès:
 * {
 *   "success": true,
 *   "message": "Connexion réussie",
 *   "data": {
 *     "userId": "...",
 *     "username": "...",
 *     "role": "...",
 *     "direction": "...",
 *     "homePage": "..."
 *   }
 * }
 * 
 * Réponse erreur:
 * {
 *   "success": false,
 *   "message": "Message d'erreur",
 *   "data": null
 * }
 */
@WebServlet("/api/login")
public class LoginApiServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginApiServlet.class.getName());
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Permettre les requêtes CORS pour Postman et autres clients
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        PrintWriter out = response.getWriter();

        try {
            // Récupérer les paramètres (supporte form-data et JSON body)
            String identifiant = null;
            String passe = null;
            String interim = null;
            String service = null;

            // Vérifier si c'est du JSON
            String contentType = request.getContentType();
            if (contentType != null && contentType.contains("application/json")) {
                // Parser le JSON body
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = request.getReader().readLine()) != null) {
                    sb.append(line);
                }
                
                @SuppressWarnings("unchecked")
                Map<String, String> requestBody = gson.fromJson(sb.toString(), Map.class);
                
                if (requestBody != null) {
                    identifiant = requestBody.get("identifiant");
                    passe = requestBody.get("passe");
                    interim = requestBody.get("interim");
                    service = requestBody.get("service");
                }
            } else {
                // Form-data ou query parameters
                identifiant = request.getParameter("identifiant");
                passe = request.getParameter("passe");
                interim = request.getParameter("interim");
                service = request.getParameter("service");
            }

            // Validation des champs obligatoires
            if (identifiant == null || identifiant.trim().isEmpty()) {
                ApiResponse<Void> res = ApiResponse.error("Le champ 'identifiant' est obligatoire");
                res.write(response, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (passe == null || passe.trim().isEmpty()) {
                ApiResponse<Void> res = ApiResponse.error("Le champ 'passe' est obligatoire");
                res.write(response, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // Appeler le service de login
            UserEJB userEJB = UserEJBClient.lookupUserEJBBeanLocal();
            userEJB.testLogin(identifiant, passe, interim, service);

            // Récupérer les informations utilisateur
            MapUtilisateur utilisateur = userEJB.getUser();
            // Configuration[] configurations = userEJB.findConfiguration(); // non utilisé ici
            
            // Récupérer la direction
            String direction = "%";
            TypeObjet crd = new TypeObjet();
            crd.setNomTable("LOG_DIRECTION");
            crd.setId(utilisateur.getAdruser());
            TypeObjet[] directions = (TypeObjet[]) CGenUtil.rechercher(crd, null, null, "");
            if (directions != null && directions.length > 0) {
                direction = directions[0].getVal();
            }

            // Récupérer la page d'accueil
            String homePage = userEJB.findHomePageServices(utilisateur.getIdrole());

            // Construire les données de réponse
            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", utilisateur.getRefuser());
            userData.put("username", utilisateur.getNomuser());
            userData.put("role", utilisateur.getIdrole());
            userData.put("direction", direction);
            userData.put("homePage", homePage);
            userData.put("adresse", utilisateur.getAdruser());

            // Réponse succès
            ApiResponse<Map<String, Object>> res = ApiResponse.success("Connexion réussie", userData);
            res.write(response, HttpServletResponse.SC_OK);

            LOGGER.info("Login réussi pour l'utilisateur: " + identifiant);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du login", e);
            ApiResponse<Void> res = ApiResponse.error(e.getMessage() != null ? e.getMessage() : "Erreur d'authentification");
            res.write(response, HttpServletResponse.SC_UNAUTHORIZED);
        }
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, Object> body = new HashMap<>();
        body.put("usage", getUsageInfo());
        ApiResponse<Map<String, Object>> res = ApiResponse.success("Utilisez la méthode POST pour vous authentifier", body);
        res.write(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Support pour les requêtes préliminaires CORS
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private Map<String, Object> getUsageInfo() {
        Map<String, Object> usage = new HashMap<>();
        usage.put("endpoint", "POST /api/login");
        usage.put("contentType", "application/json");
        
        Map<String, String> body = new HashMap<>();
        body.put("identifiant", "string (obligatoire) - Nom d'utilisateur");
        body.put("passe", "string (obligatoire) - Mot de passe");
        body.put("interim", "string (optionnel)");
        body.put("service", "string (optionnel)");
        usage.put("body", body);
        
        return usage;
    }
}

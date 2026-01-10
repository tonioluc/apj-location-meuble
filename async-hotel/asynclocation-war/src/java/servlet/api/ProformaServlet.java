package servlet.api;

import bean.CGenUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import proforma.Proforma;
import proforma.ProformaDetails;
import proforma.ProformaDetailsLib;
import proforma.ProformaLib;
import utilitaire.UtilDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.ApiResponse;

/**
 * Servlet REST pour la gestion des Proformas.
 * 
 * Endpoints:
 * GET /api/proforma -> Liste tous les proformas
 * GET /api/proforma?id=XXX -> Récupère un proforma par ID
 * POST /api/proforma -> Crée un nouveau proforma
 * PUT /api/proforma?id=XXX -> Met à jour un proforma
 * DELETE /api/proforma?id=XXX -> Supprime un proforma
 * POST /api/proforma/details?idProforma=XXX -> Ajoute un détail
 * GET /api/proforma/calculate?id=XXX -> Calcule le total
 */
@WebServlet(urlPatterns = { "/api/proforma", "/api/proforma/*" })
public class ProformaServlet extends HttpServlet {

    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            String pathInfo = request.getPathInfo();
            String id = request.getParameter("id");

            // Support path-based id: /api/proforma/{id} and /api/proforma/calculate/{id}
            if (pathInfo != null) {
                if (pathInfo.equals("/calculate")) {
                    handleCalculateTotal(request, response, c);
                    return;
                }
                if (pathInfo.startsWith("/calculate/")) {
                    // rewrite parameter id for calculate
                    String calcId = pathInfo.substring("/calculate/".length());
                    request.setAttribute("_tmp_calc_id", calcId);
                    handleCalculateTotal(request, response, c);
                    return;
                }
                // If pathInfo is just an id (e.g. /PROF0001)
                if (pathInfo.length() > 1) {
                    String idFromPath = pathInfo.substring(1);
                    handleGetById(idFromPath, response, c);
                    return;
                }
            }

            // GET /api/proforma?id=XXX -> Un seul proforma avec ses détails
            if (id != null && !id.isEmpty()) {
                handleGetById(id, response, c);
            } else {
                // GET /api/proforma -> Liste de tous les proformas
                handleGetAll(response, c);
            }
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            closeConnection(c);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            String pathInfo = request.getPathInfo();

            // POST /api/proforma/details?idProforma=XXX -> Ajouter un détail
            if (pathInfo != null && pathInfo.equals("/details")) {
                handleAddDetail(request, response, c);
                return;
            }

            // POST /api/proforma -> Créer un nouveau proforma
            handleCreate(request, response, c);
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            closeConnection(c);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            // allow id in path (/api/proforma/{id}) or as parameter
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && pathInfo.length() > 1 && (request.getParameter("id") == null || request.getParameter("id").isEmpty())) {
                String idFromPath = pathInfo.substring(1);
                // set as request attribute so handleUpdate can fetch it like a parameter
                request.setAttribute("_tmp_id", idFromPath);
            }
            handleUpdate(request, response, c);
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            closeConnection(c);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            // allow id in path (/api/proforma/{id}) or as parameter
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && pathInfo.length() > 1 && (request.getParameter("id") == null || request.getParameter("id").isEmpty())) {
                String idFromPath = pathInfo.substring(1);
                request.setAttribute("_tmp_id", idFromPath);
            }
            handleDelete(request, response, c);
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            closeConnection(c);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // ==================== Handlers ====================

    private void handleGetAll(HttpServletResponse response, Connection c) throws Exception {
        ProformaLib[] proformas = (ProformaLib[]) CGenUtil.rechercher(
                new ProformaLib(), null, null, c, " ORDER BY daty DESC");

        List<Map<String, Object>> result = new ArrayList<>();
        for (ProformaLib p : proformas) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("client", p.getIdClientLib());
            map.put("date", p.getDaty() != null ? p.getDaty().toString() : null);
            map.put("montantTotal", p.getMontantTtc());
            map.put("designation", p.getDesignation());
            map.put("etat", p.getEtat());
            map.put("etatLib", p.getEtatLib());
            result.add(map);
        }
        sendJson(response, result);
    }

    private void handleGetById(String id, HttpServletResponse response, Connection c) throws Exception {
        ProformaLib[] proformas = (ProformaLib[]) CGenUtil.rechercher(
                new ProformaLib(), null, null, c, " AND id = '" + id + "'");

        if (proformas.length == 0) {
            sendError(response, HttpServletResponse.SC_NOT_FOUND, "Proforma non trouvé");
            return;
        }

        ProformaLib p = proformas[0];
        Map<String, Object> result = new HashMap<>();
        result.put("id", p.getId());
        result.put("client", p.getIdClientLib());
        result.put("date", p.getDaty() != null ? p.getDaty().toString() : null);
        result.put("montantTotal", p.getMontantTtc());
        result.put("designation", p.getDesignation());
        result.put("description", p.getRemarque());
        result.put("etat", p.getEtat());
        result.put("etatLib", p.getEtatLib());
        result.put("remise", p.getRemise());
        result.put("tva", p.getTva());

        // Récupérer les détails
        ProformaDetailsLib[] details = (ProformaDetailsLib[]) CGenUtil.rechercher(
                new ProformaDetailsLib(), null, null, c, " AND idProforma = '" + id + "'");

        List<Map<String, Object>> detailsList = new ArrayList<>();
        for (ProformaDetailsLib d : details) {
            Map<String, Object> detailMap = new HashMap<>();
            detailMap.put("id", d.getId());
            detailMap.put("designation", d.getDesignation());
            detailMap.put("quantite", d.getQte());
            detailMap.put("prixUnitaire", d.getPu());
            detailMap.put("remise", d.getRemise());
            detailMap.put("tva", d.getTva());
            detailsList.add(detailMap);
        }
        result.put("details", detailsList);

        sendJson(response, result);
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response, Connection c) throws Exception {
        String body = readBody(request);
        Map<String, Object> data = gson.fromJson(body, Map.class);

        Proforma proforma = new Proforma();
        proforma.setMode("insert");
        proforma.construirePK(c);

        if (data.containsKey("client")) {
            proforma.setIdClient((String) data.get("client"));
        }
        if (data.containsKey("designation")) {
            proforma.setDesignation((String) data.get("designation"));
        }
        if (data.containsKey("description")) {
            proforma.setRemarque((String) data.get("description"));
        }
        if (data.containsKey("date")) {
            proforma.setDaty(java.sql.Date.valueOf((String) data.get("date")));
        } else {
            proforma.setDaty(new java.sql.Date(System.currentTimeMillis()));
        }
        if (data.containsKey("remise")) {
            proforma.setRemise(((Number) data.get("remise")).doubleValue());
        }
        if (data.containsKey("tva")) {
            proforma.setTva(((Number) data.get("tva")).doubleValue());
        }

        // Use domain API to persist
        proforma.createObject("system", c);

        Map<String, Object> result = new HashMap<>();
        result.put("id", proforma.getId());
        result.put("message", "Proforma créé avec succès");

        sendJson(response, result);
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response, Connection c) throws Exception {
        String id = request.getParameter("id");
        if ((id == null || id.isEmpty()) && request.getAttribute("_tmp_id") != null) {
            id = (String) request.getAttribute("_tmp_id");
        }
        if (id == null || id.isEmpty()) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "ID requis");
            return;
        }

        String body = readBody(request);
        Map<String, Object> data = gson.fromJson(body, Map.class);

        Proforma[] proformas = (Proforma[]) CGenUtil.rechercher(
                new Proforma(), null, null, c, " AND id = '" + id + "'");

        if (proformas.length == 0) {
            sendError(response, HttpServletResponse.SC_NOT_FOUND, "Proforma non trouvé");
            return;
        }

        Proforma proforma = proformas[0];
        proforma.setMode("modif");

        if (data.containsKey("client")) {
            proforma.setIdClient((String) data.get("client"));
        }
        if (data.containsKey("designation")) {
            proforma.setDesignation((String) data.get("designation"));
        }
        if (data.containsKey("description")) {
            proforma.setRemarque((String) data.get("description"));
        }
        if (data.containsKey("date")) {
            proforma.setDaty(java.sql.Date.valueOf((String) data.get("date")));
        }
        if (data.containsKey("remise")) {
            proforma.setRemise(((Number) data.get("remise")).doubleValue());
        }
        if (data.containsKey("tva")) {
            proforma.setTva(((Number) data.get("tva")).doubleValue());
        }

        // Persist changes using ClassMere history-aware update
        proforma.updateToTableWithHisto("system", c);

        Map<String, Object> result = new HashMap<>();
        result.put("id", proforma.getId());
        result.put("message", "Proforma modifié avec succès");

        sendJson(response, result);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response, Connection c) throws Exception {
        String id = request.getParameter("id");
        if ((id == null || id.isEmpty()) && request.getAttribute("_tmp_id") != null) {
            id = (String) request.getAttribute("_tmp_id");
        }
        if (id == null || id.isEmpty()) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "ID requis");
            return;
        }

        Proforma[] proformas = (Proforma[]) CGenUtil.rechercher(
                new Proforma(), null, null, c, " AND id = '" + id + "'");

        if (proformas.length == 0) {
            sendError(response, HttpServletResponse.SC_NOT_FOUND, "Proforma non trouvé");
            return;
        }

        // Delete using domain API
        proformas[0].deleteToTable(c);

        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("message", "Proforma supprimé avec succès");
        sendJson(response, result);
    }

    private void handleAddDetail(HttpServletRequest request, HttpServletResponse response, Connection c)
            throws Exception {
        String idProforma = request.getParameter("idProforma");
        if (idProforma == null || idProforma.isEmpty()) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "idProforma requis");
            return;
        }

        String body = readBody(request);
        Map<String, Object> data = gson.fromJson(body, Map.class);

        ProformaDetails detail = new ProformaDetails();
        detail.setMode("insert");
        detail.construirePK(c);
        detail.setIdProforma(idProforma);

        if (data.containsKey("designation")) {
            detail.setDesignation((String) data.get("designation"));
        }
        if (data.containsKey("quantite")) {
            detail.setQte(((Number) data.get("quantite")).intValue());
        }
        if (data.containsKey("prixUnitaire")) {
            detail.setPu(((Number) data.get("prixUnitaire")).doubleValue());
        }
        if (data.containsKey("remise")) {
            detail.setRemise(((Number) data.get("remise")).doubleValue());
        }
        if (data.containsKey("tva")) {
            detail.setTva(((Number) data.get("tva")).doubleValue());
        }

        // Persist detail
        detail.createObject("system", c);

        Map<String, Object> result = new HashMap<>();
        result.put("id", detail.getId());
        result.put("message", "Détail ajouté avec succès");

        sendJson(response, result);
    }

    private void handleCalculateTotal(HttpServletRequest request, HttpServletResponse response, Connection c)
            throws Exception {
        String id = request.getParameter("id");
        if ((id == null || id.isEmpty()) && request.getAttribute("_tmp_calc_id") != null) {
            id = (String) request.getAttribute("_tmp_calc_id");
        }
        if (id == null || id.isEmpty()) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "ID requis");
            return;
        }

        ProformaDetailsLib[] details = (ProformaDetailsLib[]) CGenUtil.rechercher(
                new ProformaDetailsLib(), null, null, c, " AND idProforma = '" + id + "'");

        double total = 0;
        for (ProformaDetailsLib d : details) {
            double ligne = d.getQte() * d.getPu();
            double remise = ligne * (d.getRemise() / 100);
            double ht = ligne - remise;
            double tva = ht * (d.getTva() / 100);
            total += ht + tva;
        }

        sendJson(response, total);
    }

    // ==================== Utilitaires ====================

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private void sendJson(HttpServletResponse response, Object data) throws IOException {
        ApiResponse<Object> api = ApiResponse.success("Opération réussie", data);
        api.write(response, HttpServletResponse.SC_OK);
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        ApiResponse<Object> api = ApiResponse.error(message);
        api.write(response, status);
    }

    private String readBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private void closeConnection(Connection c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception ignored) {
            }
        }
    }
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package station.export;


import bean.AdminGen;
import bean.CGenUtil;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Constante;
import caisse.MvtCaisseCpl;
import constante.ConstanteAffichage;
import net.sf.jasperreports.engine.JRException;
import reporting.ReportingCdn;
import reservation.*;
import utils.ConstanteLocation;
import utils.ConstanteStation;
import web.mg.cnaps.servlet.etat.UtilitaireImpression;
import encaissement.*;
import java.util.Arrays;
import org.xhtmlrenderer.css.style.derived.StringValue;

import client.Client;

import prelevement.PrelevementPompiste;
import proforma.ProformaDetailsLib;
import proforma.ProformaLib;
import utilitaire.*;
import vente.*;
import faturefournisseur.*;
 
/**
 *
 * @author Admin
 */
@WebServlet(name = "ExportPDF", urlPatterns = {"/ExportPDF"})
public class ExportPDF extends HttpServlet {
    String nomJasper = "";
    ReportingCdn.Fonctionnalite fonctionnalite = ReportingCdn.Fonctionnalite.RECETTE;
    
    public String getReportPath() throws IOException {
        return getServletContext().getRealPath(File.separator + "report" + File.separator + getNomJasper() + ".jasper");
    }
    public String getNomJasper() {
        return nomJasper;
    }

    public void setNomJasper(String nomJasper) {
        this.nomJasper = nomJasper;
    }
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
            throws ServletException, IOException, Exception {
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("fiche_encaissement")) impressionEncaissement(request, response);
        if (action.equalsIgnoreCase("fiche_encaissement_pompiste")) impressionEncaissementPompist(request, response);
        if (action.equalsIgnoreCase("fiche_vente")) fiche_vente(request, response);
        if (action.equalsIgnoreCase("fiche_bc")) fiche_bc(request, response);
        if (action.equalsIgnoreCase("fiche_bl")) fiche_bl(request, response);
        if (action.equalsIgnoreCase("bl")) bl_groupe(request, response);
        if (action.equalsIgnoreCase("proforma")) proforma(request, response);
        if (action.equalsIgnoreCase("vente_liste")) vente_liste(request, response);
        if (action.equalsIgnoreCase("vente_liste_mere_fille")) vente_liste_mere_fille(request, response);
    }

    private void bl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Connection c = null;
        Map param = new HashMap();
        List dataSource = new ArrayList();
        String id = request.getParameter("id");
        ReservationLib v = new ReservationLib();
        v.setNomTable("RESERVATION_LIB_MIN_DATY");
        ReservationLib[] enc_mere = (ReservationLib[]) CGenUtil.rechercher(v, null, null, null,
                " AND ID = '" + id + "'");
        String numBL = null;
        if (enc_mere != null && enc_mere.length > 0) {
            numBL = enc_mere[0].getNumBl();
            Client client = new Client();
            Client[] clients = (Client[]) CGenUtil.rechercher(client, null, null, null,"");
            if (clients != null && clients.length > 0) {
                param.put("client_nom", clients[0].getNom());
                param.put("client_adresse", clients[0].getAdresse());
                param.put("client_telephone", clients[0].getTelephone());
                param.put("client_mail", clients[0].getMail());
            }
            param.put("numero_bon", numBL);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            param.put("date_location", df.format(enc_mere[0].getDaty()));
            param.put("lieu_location", enc_mere[0].getLieulocation());
        }
        if (numBL != null && numBL.isEmpty() == false) {
            CheckInLib check = new  CheckInLib();
            check.setNomTable("CHECKINLIBELLE_PDF");
            check.setNumBl(numBL);
            Check[] checks = (Check[]) CGenUtil.rechercher(check, null, null, null,"");
            dataSource.addAll(Arrays.asList(checks));
        }
        setNomJasper("BL_1");
        UtilitaireImpression.imprimer(request, response, getNomJasper(), param, dataSource, getReportPath());
    }

    private void bl_groupe(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Connection c = null;
        Map param = new HashMap();
        List dataSource = new ArrayList();
        String id = request.getParameter("id");
        String nomClient = "";

        ReservationLib v = new ReservationLib();
        v.setNomTable("RESERVATION_LIB_MIN_DATY");
        ReservationLib[] enc_mere = (ReservationLib[]) CGenUtil.rechercher(v, null, null, null,
                " AND ID = '" + id + "'");

        String numBL = null;
        if (enc_mere != null && enc_mere.length > 0) {
            numBL = enc_mere[0].getNumBl();

            // Récupération client
            Client client = new Client();
            client.setId(enc_mere[0].getIdclient());
            Client[] clients = (Client[]) CGenUtil.rechercher(client, null, null, null, "");
            if (clients != null && clients.length > 0) {
                param.put("client_nom", clients[0].getNom());
                nomClient = clients[0].getNom();
                param.put("client_adresse", clients[0].getAdresse());
                param.put("client_telephone", clients[0].getTelephone());
                param.put("client_mail", clients[0].getMail());
            }

            param.put("numero_bon", numBL);

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            param.put("date_location", df.format(enc_mere[0].getDaty()));
            param.put("lieu_location", enc_mere[0].getLieulocation());
        }

        if (numBL != null && numBL.isEmpty() == false) {
            CheckInLib check = new CheckInLib();
            check.setNomTable("CHECKINLIBELLE_PDF");
            check.setNumBl(numBL);
            CheckInLib[] checks = (CheckInLib[]) CGenUtil.rechercher(check, null, null, null, "");

            if (checks != null && checks.length > 0) {
                // DUPLIQUER LES DONNÉES POUR LIVRAISON ET RETOUR

                // 1. Ajouter toutes les lignes pour LIVRAISON
                for (CheckInLib checkItem : checks) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("type", "LIVRAISON");
                    row.put("refproduit", checkItem.getRefproduit());
                    row.put("produitLibelle", checkItem.getProduitLibelle());
                    row.put("qte", checkItem.getQte());
                    row.put("unite", checkItem.getUnite());
                    dataSource.add(row);
                }

                // 2. Ajouter les mêmes lignes pour RETOUR
                for (CheckInLib checkItem : checks) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("type", "RETOUR");
                    row.put("refproduit", checkItem.getRefproduit());
                    row.put("produitLibelle", checkItem.getProduitLibelle());
                    row.put("qte", checkItem.getQte());
                    row.put("unite", checkItem.getUnite());
                    dataSource.add(row);
                }
            }
        }

        setNomJasper("BL");
        String nomPdf = "BL ATIPIK " + nomClient + " " + numBL;
        UtilitaireImpression.imprimer(request, response, nomPdf, param, dataSource, getReportPath());
    }

    private void fiche_bc(HttpServletRequest request, HttpServletResponse response) throws IOException, JRException, Exception{
        Connection c = null;
        Map param = new HashMap();
        List dataSource = new ArrayList();
         String id = request.getParameter("id");
        As_BonDeCommandeCpl v = new As_BonDeCommandeCpl();
        v.setNomTable("As_BonDeCommande_MERECPL");
        As_BonDeCommandeCpl[] enc_mere = (As_BonDeCommandeCpl[]) CGenUtil.rechercher(v, null, null, null,
                " AND ID = '" + id + "'");
        
        if (enc_mere.length > 0) {
          param.put("designation", enc_mere[0].getDesignation());
          param.put("ref", enc_mere[0].getReference());
          param.put("daty", enc_mere[0].getDaty());
          param.put("remarque", enc_mere[0].getRemarque());
          param.put("fournisseur", enc_mere[0].getFournisseurlib());
          param.put("modeP", enc_mere[0].getModepaiementlib());
          param.put("num", id);
          param.put("iddevise", enc_mere[0].getIdDevise());
          param.put("montantHT", enc_mere[0].getMontantHT());
          param.put("montantTVA", enc_mere[0].getMontantTVA());
          param.put("montantTTC", enc_mere[0].getMontantTTC());
          param.put("devise", enc_mere[0].getIdDeviselib());
        }
       
        As_BonDeCommande_Fille_CPL vf = new As_BonDeCommande_Fille_CPL();
        vf.setNomTable("AS_BONDECOMMANDE_CPL");
        As_BonDeCommande_Fille_CPL[] v_fille = (As_BonDeCommande_Fille_CPL[]) CGenUtil.rechercher(vf, null, null, null,
                " AND idbc = '" + id + "'");
        dataSource.addAll(Arrays.asList(v_fille));
        setNomJasper("BonDeCommande");
        UtilitaireImpression.imprimer(request, response, getNomJasper(), param, dataSource, getReportPath());
    }
    
    private void fiche_bl(HttpServletRequest request, HttpServletResponse response) throws IOException, JRException, Exception{
        Connection c = null;
        Map param = new HashMap();
        List dataSource = new ArrayList();
         String id = request.getParameter("id");
        As_BondeLivraisonClient_Cpl v = new As_BondeLivraisonClient_Cpl();
        v.setNomTable("AS_BONDELIVRAISON_CLIENT_CPL");
        As_BondeLivraisonClient_Cpl[] enc_mere = (As_BondeLivraisonClient_Cpl[]) CGenUtil.rechercher(v, null, null, null,
                " AND id = '" + id + "'");
        if (enc_mere.length > 0) {
          param.put("designation", enc_mere[0].getDesignation());
          param.put("daty", enc_mere[0].getDaty());
          param.put("remarque", enc_mere[0].getRemarque());
          param.put("magasin", enc_mere[0].getMagasin());
          param.put("num", id);
        }
        As_BondeLivraisonClientFille_Cpl vf = new As_BondeLivraisonClientFille_Cpl();
        vf.setNomTable("AS_BONLIVRFILLE_CLIENT_CPL");
        As_BondeLivraisonClientFille_Cpl[] v_fille = (As_BondeLivraisonClientFille_Cpl[]) CGenUtil.rechercher(vf, null, null, null,
                " AND numbl = '" + id + "'");
        dataSource.addAll(Arrays.asList(v_fille));
        setNomJasper("BonDeLivraison");
        UtilitaireImpression.imprimer(request, response, getNomJasper(), param, dataSource, getReportPath());
    }
    private void fiche_vente(HttpServletRequest request, HttpServletResponse response) throws IOException, JRException, Exception {
        Connection c = null;
        Map param = new HashMap();
        List dataSource = new ArrayList();
        String id = request.getParameter("id");
    
        param.put("siege" , ConstanteLocation.lieuMagasin);
        param.put("tel" , ConstanteLocation.tel);
        param.put("mail" , ConstanteLocation.mail);
        param.put("nif" , ConstanteLocation.nif);
        param.put("stat" , ConstanteLocation.stat);
        String nomClient = "";
    
        VenteLib v = new VenteLib();
        v.setNomTable("VENTE_CPL_NUM");
        VenteLib[] enc_mere = (VenteLib[]) CGenUtil.rechercher(v, null, null, null,
                " AND ID = '" + id + "'");
        if (enc_mere.length > 0) {
            String num = "FACTURE ATIPIK " + enc_mere[0].getNumfacture();
            param.put("numFact", num);
            param.put("datyFact", enc_mere[0].getDaty());
            param.put("projetFact", "Location mat&eacute;riel");
            param.put("dateLocation", enc_mere[0].getPeriode());
            param.put("lieuLocation", enc_mere[0].getLieulocation());
    
            Client cl = new Client();
            cl.setId(enc_mere[0].getIdClient());
            Client[] clients = (Client[]) CGenUtil.rechercher(cl, null, null, null,"");
            if(clients.length>0){
                param.put("nomClient", clients[0].getNom());
                nomClient = clients[0].getNom();
                param.put("addrClient", clients[0].getAdresse());
                param.put("respClient", clients[0].getRepresentant());
                param.put("telClient",  clients[0].getTelephone());
                param.put("mailClient",  clients[0].getMail());
                param.put("statClient",  clients[0].getStat());
                param.put("nifClient",  clients[0].getNif());
            }
        }

        VenteDetailsLib vf = new VenteDetailsLib();
        vf.setNomTable("VENTE_DETAILS_CPL");
        VenteDetailsLib[] v_fille = (VenteDetailsLib[]) CGenUtil.rechercher(
                vf, null, null, null, " AND idVente = '" + id + "'");
    
        List<VenteDetailsLib> detailsNormaux = new ArrayList<>();
        double sommeCaution = 0.0;
        double sommeTransportAller = 0.0;
        double sommeTransportRetour = 0.0;
        double sommeTransportPers = 0.0;
        String devise = "";
    
        for (VenteDetailsLib detail : v_fille) {
            String idProd = detail.getIdProduit();
    
            if (idProd.equals(ConstanteLocation.id_produit_caution)) {
                sommeCaution += detail.getMontantTTC();
                devise = detail.getIdDevise();
            } else if (idProd.equals(ConstanteLocation.id_produit_transport_aller)) {
                sommeTransportAller += detail.getMontantTTC();
                devise = detail.getIdDevise();
            } else if (idProd.equals(ConstanteLocation.id_produit_transport_retour)) {
                sommeTransportRetour += detail.getMontantTTC();
                devise = detail.getIdDevise();
            } else if (idProd.equals(ConstanteLocation.id_produit_transport_pers)) {
                sommeTransportPers += detail.getMontantTTC();
                devise = detail.getIdDevise();
            } else {
                detailsNormaux.add(detail);
                if (devise.isEmpty()) {
                    devise = detail.getIdDevise();
                }
            }

            if (detail.getDimension() != null && Integer.valueOf(detail.getDimension()) > 1){
                String txt = detail.getDesignation() + " (" + detail.getNombre() + " pièces)";
                String utf8 = new String(txt.getBytes("ISO-8859-1"), "UTF-8");
                detail.setDesignation(utf8);
                detail.setNombre((int) (detail.getNombre() * Integer.valueOf(detail.getDimension())));
            }
        }
        dataSource.addAll(detailsNormaux);
        double montantHT = AdminGen.calculSommeDouble(
                detailsNormaux.toArray(new VenteDetailsLib[0]), "montantHTLocal");
        double montantRemise = AdminGen.calculSommeDouble(
                detailsNormaux.toArray(new VenteDetailsLib[0]), "montantRemise");
        double remiseMt = AdminGen.calculSommeDouble(
                detailsNormaux.toArray(new VenteDetailsLib[0]), "remisemontant");
        double montantTTC = AdminGen.calculSommeDouble(
                detailsNormaux.toArray(new VenteDetailsLib[0]), "montantTTCLocal");
        param.put("nbTotalArticles", dataSource.size());
        param.put("montantHT", Utilitaire.formaterAr(montantHT) + " " + devise);
        param.put("remise", detailsNormaux.isEmpty() ? "" : detailsNormaux.get(0).getRemise());
        param.put("remiseMt", Utilitaire.formaterAr(montantRemise) + " " + devise);
        param.put("montantremise", Utilitaire.formaterAr(montantTTC) + " " + devise);
    
        param.put("montantCaution", Utilitaire.formaterAr(sommeCaution) + " " + devise);
        param.put("fraisTransportAller", Utilitaire.formaterAr(sommeTransportAller) + " " + devise);
        param.put("fraisTransportRetour", Utilitaire.formaterAr(sommeTransportRetour) + " " + devise);
        param.put("fraisTransportPers", Utilitaire.formaterAr(sommeTransportPers) + " " + devise);
    
        double montantTotalGlobal=montantTTC+sommeCaution+sommeTransportAller+sommeTransportRetour+sommeTransportPers;
        param.put("montantTotal", Utilitaire.formaterAr(montantTotalGlobal) + " " + devise);
        param.put("montantApayer", Utilitaire.formaterAr(montantTotalGlobal) + " " + devise);
        if (enc_mere.length > 0) param.put("montantrestepayer", Utilitaire.formaterAr(enc_mere[0].getMontantreste()) + " " + devise);
        param.put("montantTotalLettre", ChiffreLettre.convertRealToString(montantTotalGlobal));
        
        MvtCaisseCpl[] mvtcaisse = (MvtCaisseCpl[]) CGenUtil.rechercher(new MvtCaisseCpl(), null, null, null,
                " AND IDORIGINE = '" + id + "' and etat=11");
        List<MvtCaisseCpl> listmvtcaisse =  Arrays.asList(mvtcaisse);
        param.put("listmvtcaisse", listmvtcaisse);

        setNomJasper("facture_vente-bru");
        String nomPdf = "FACTURE ATIPIK " + nomClient + " " + enc_mere[0].getNumfacture();
        UtilitaireImpression.imprimer(request, response, nomPdf, param, dataSource, getReportPath());
    }
    
    
    private void proforma(HttpServletRequest request, HttpServletResponse response) throws IOException, JRException, Exception {
        Connection c = null;
        Map param = new HashMap();
        List dataSource = new ArrayList();
        String id = request.getParameter("id");
    
        // Infos société
        param.put("siege" , ConstanteLocation.lieuMagasin);
        param.put("tel" , ConstanteLocation.tel);
        param.put("mail" , ConstanteLocation.mail);
        param.put("nif" , ConstanteLocation.nif);
        param.put("stat" , ConstanteLocation.stat);
    
        String devise = "";
        double montantTTC = 0;
        String nomClient = "";
    
        // Récupération entête
        ProformaLib v = new ProformaLib();
        v.setNomTable("PROFORMA_CPL_NUM");
        ProformaLib[] enc_mere = (ProformaLib[]) CGenUtil.rechercher(v, null, null, null,
                " AND ID = '" + id + "'");
        if (enc_mere.length > 0) {
            montantTTC = enc_mere[0].getMontantTtc();
            String num = "FACTURE PROFORMA N: " + enc_mere[0].getNumproforma();
            devise = enc_mere[0].getIdDevise();
            param.put("numFact", num);
            param.put("datyFact", enc_mere[0].getDaty());
            param.put("projetFact", "Location mat&eacute;riel");
            param.put("dateLocation", enc_mere[0].getPeriode());
            param.put("periode", enc_mere[0].getPeriode());
            param.put("lieuLocation", enc_mere[0].getLieulocation());
            param.put("idDevise", devise);
            param.put("montantApayer", Utilitaire.formaterAr(enc_mere[0].getMontantreste()) + " " + devise);
            param.put("remise", enc_mere[0].getRemise());
    
            Client cl = new Client();
            cl.setId(enc_mere[0].getIdClient());
            Client[] clients = (Client[]) CGenUtil.rechercher(cl, null, null, null,"");
            if(clients.length>0){
                param.put("nomClient", clients[0].getNom());
                nomClient =  clients[0].getNom();
                param.put("addrClient", clients[0].getAdresse());
                param.put("respClient", clients[0].getRepresentant());
                param.put("telClient",  clients[0].getTelephone());
                param.put("mailClient",  clients[0].getMail());
                param.put("statClient",  clients[0].getStat());
                param.put("nifClient",  clients[0].getNif());
            }
        }
    
        // Récupération détails
        ProformaDetailsLib vf = new ProformaDetailsLib();
        ProformaDetailsLib[] v_fille = (ProformaDetailsLib[]) CGenUtil.rechercher(
                vf, null, null, null, " AND idProforma = '" + id + "'");
    
        // Séparation produits spéciaux
        List<ProformaDetailsLib> detailsNormaux = new ArrayList<>();
        double sommeCaution = 0.0;
        double sommeTransportAller = 0.0;
        double sommeTransportRetour = 0.0;
        double sommeTransportPers = 0.0;
    
        for (ProformaDetailsLib detail : v_fille) {
            String idProd = detail.getIdProduit();
    
            if (idProd.equals(ConstanteLocation.id_produit_caution)) {
                sommeCaution += detail.getMontantttc();
            } else if (idProd.equals(ConstanteLocation.id_produit_transport_aller)) {
                sommeTransportAller += detail.getMontantttc();
            } else if (idProd.equals(ConstanteLocation.id_produit_transport_retour)) {
                sommeTransportRetour += detail.getMontantttc();
            } else if (idProd.equals(ConstanteLocation.id_produit_transport_pers)) {
                sommeTransportPers += detail.getMontantttc();
            } else {
                detailsNormaux.add(detail);
            }

            if (detail.getDimension() != null && Integer.valueOf(detail.getDimension()) > 1){
                String txt = detail.getDesignation() + " (" + detail.getNombre() + " pièces)";
                String utf8 = new String(txt.getBytes("ISO-8859-1"), "UTF-8");
                detail.setDesignation(utf8);
                detail.setNombre(detail.getNombre() * Integer.valueOf(detail.getDimension()));
            }
        }
    
        // Ajouter uniquement les détails normaux dans le dataSource Jasper
        dataSource.addAll(detailsNormaux);
    
        // Calculs sur les détails normaux
        double montantHT = AdminGen.calculSommeDouble(
                detailsNormaux.toArray(new ProformaDetailsLib[0]), "puTotal");
        double remiseMt = AdminGen.calculSommeDouble(
                detailsNormaux.toArray(new ProformaDetailsLib[0]), "remisemontant");
        double montantTTCNormaux = AdminGen.calculSommeDouble(
                detailsNormaux.toArray(new ProformaDetailsLib[0]), "montantTTC");

        // Paramètres Jasper
        param.put("montantHT", Utilitaire.formaterAr(montantHT) + " " + devise);
        param.put("remiseMt", Utilitaire.formaterAr(remiseMt) + " " + devise);
        param.put("montantremise", Utilitaire.formaterAr(montantTTCNormaux) + " " + devise);
    
        // Produits spéciaux
        param.put("montantCaution", Utilitaire.formaterAr(sommeCaution) + " " + devise);
        param.put("fraisTransportAller", Utilitaire.formaterAr(sommeTransportAller) + " " + devise);
        param.put("fraisTransportRetour", Utilitaire.formaterAr(sommeTransportRetour) + " " + devise);
        param.put("fraisTranspPers", Utilitaire.formaterAr(sommeTransportPers) + " " + devise);

    
        // Total global
        double montantTotalGlobal = montantTTCNormaux + sommeCaution + sommeTransportAller + sommeTransportRetour + sommeTransportPers;

        double montantttc75 = montantTotalGlobal*0.75;
        double montantttc25 = montantTotalGlobal*0.25;

        param.put("montantttc75", Utilitaire.formaterAr(montantttc75) + " " + devise);
        param.put("montantttc25", Utilitaire.formaterAr(montantttc25) + " " + devise);
        param.put("montantTotal", Utilitaire.formaterAr(montantTotalGlobal) + " " + devise);
        param.put("montantApayer", Utilitaire.formaterAr(montantTotalGlobal) + " " + devise);
        param.put("montantTotalLettre", ChiffreLettre.convertRealToString(montantTotalGlobal));
    
        // Impression Jasper
        setNomJasper("proforma-bru");
        String nomPdf = "DEVIS ATIPIK " + nomClient + " " + enc_mere[0].getNumproforma();
        UtilitaireImpression.imprimer(request, response, nomPdf, param, dataSource, getReportPath());
    }
    
    private void impressionEncaissement(HttpServletRequest request, HttpServletResponse response) throws IOException, JRException, Exception{
        Connection c = null;
        Map param = new HashMap();
        List dataSource = new ArrayList();
         String id = request.getParameter("id");
        EncaissementLib eM = new EncaissementLib();
        eM.setNomTable("ENCAISSEMENT_LIB");
        EncaissementLib[] enc_mere = (EncaissementLib[]) CGenUtil.rechercher(eM, null, null, null,
                " AND ID = '" + id + "'");
        if (enc_mere.length > 0) {
          param.put("carburants", Utilitaire.formaterAr(enc_mere[0].getVenteCarburant()));
          param.put("lubrifiants", Utilitaire.formaterAr(enc_mere[0].getVenteLubrifiant()));
          param.put("totalrecette", Utilitaire.formaterAr(enc_mere[0].getTotalRecette()));
          param.put("depense", Utilitaire.formaterAr(enc_mere[0].getDepense()));
          param.put("montantecart", Utilitaire.formaterAr(enc_mere[0].getEcart()));
          param.put("versement", Utilitaire.formaterAr(enc_mere[0].getTotalVersement()));
         
        }
        EncaissementDetailsLib eF = new EncaissementDetailsLib();
        eF.setNomTable("Encaissement_Details_Lib");
        EncaissementDetailsLib[] enc_fille = (EncaissementDetailsLib[]) CGenUtil.rechercher(eF, null, null, null,
                " AND idEncaissement = '" + id + "'");
        dataSource.addAll(Arrays.asList(enc_fille));
        setNomJasper("encaissement");
        UtilitaireImpression.imprimer(request, response, getNomJasper(), param, dataSource, getReportPath());
    }

     private void impressionEncaissementPompist (HttpServletRequest request, HttpServletResponse response) throws IOException, JRException, Exception{
        Connection c = null;
        Map param = new HashMap();
        List dataSource = new ArrayList();
         String id = request.getParameter("id");
        EncaissementFichePdf eM = new EncaissementFichePdf();
       
        EncaissementFichePdf[] enc_mere = (EncaissementFichePdf[]) CGenUtil.rechercher(eM, null, null, null,
                " AND ID = '" + id + "'");

      
        EncaissementReport er=new EncaissementReport();
        er.setId(id);
        er.init(c);

        if (enc_mere.length > 0) {
          param.put("date", enc_mere[0].getDaty());
          param.put("nom", enc_mere[0].getIdPompisteLib());
          param.put("ecart", enc_mere[0].getEcart());
          param.put("versement", enc_mere[0].getTotalVersement());
          param.put("espece", enc_mere[0].getTotalEspece());
          param.put("om", enc_mere[0].getTotalOrangeMoney());
        }
       
        dataSource.add(er);


        setNomJasper("encaissementPompiste");
        UtilitaireImpression.imprimer(request, response, getNomJasper(), param, dataSource, getReportPath());
    }
    private void vente_liste(HttpServletRequest request, HttpServletResponse response) throws IOException, JRException, Exception{
        Connection c = null;
        Map param = new HashMap();
        List dataSource = new ArrayList();
        String id = request.getParameter("id");
        String designation = request.getParameter("designation");
        String idClientLib = request.getParameter("idClientLib");
        String idmodepaiement = request.getParameter("idmodepaiement");
        String daty1 = request.getParameter("daty1");
        String daty2 = request.getParameter("daty2");
        String awhere="";
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
        VenteLib[] enc_mere = (VenteLib[]) CGenUtil.rechercher(v, null, null, null,awhere);
        //double montantHT = AdminGen.calculSommeDouble(enc_mere,"montantHT");
        dataSource.addAll(Arrays.asList(enc_mere));
        setNomJasper("facture_vente_mere");
        UtilitaireImpression.imprimer(request, response, getNomJasper(), param, dataSource, getReportPath());
    }
    private void vente_liste_mere_fille(HttpServletRequest request, HttpServletResponse response) throws IOException, JRException, Exception{
        Connection c = null;
        Map param = new HashMap();
        List dataSource = new ArrayList();
        String id = request.getParameter("id");
        String designation = request.getParameter("designation");
        String idClientLib = request.getParameter("idClientLib");
        String daty1 = request.getParameter("daty1");
        String daty2 = request.getParameter("daty2");
        String awhere="";
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
        UtilitaireImpression.imprimer(request, response, getNomJasper(), param, dataSourceMere, getReportPath());

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
            Logger.getLogger(ExportPDF.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ExportPDF.class.getName()).log(Level.SEVERE, null, ex);
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
    }// </editor-fold>
    
}

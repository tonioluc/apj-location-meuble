<%--
  Created by IntelliJ IDEA.
  User: safidy
  Date: 05/08/2025
  Time: 11:44
  To change this template use File | Settings | File Templates.
--%>

<%@page import="magasin.Magasin"%>
<%@page import="vente.*"%>
<%@page import="user.*"%>
<%@page import="bean.*" %>
<%@page import="affichage.*"%>
<%@page import="utilitaire.*"%>
<%@ page import="client.Client" %>
<%@ page import="proforma.*" %>
<%@ page import="vente.dmdprix.DmdPrix" %>

<%
    try{
        UserEJB u = null;
        u = (UserEJB) session.getValue("u");
        PageInsertMultiple pi=null;
        Proforma mere = new Proforma();
        ProformaDetails fille = new ProformaDetails();
        int nombreLigne = 10;
        pi = new PageInsertMultiple(mere,fille,request, nombreLigne,u);
        Proforma prerempli = null;

        pi.setLien((String) session.getValue("lien"));
        pi.getFormu().getChamp("remarque").setLibelle("Remarque");
        pi.getFormu().getChamp("daty").setLibelle("Date");
        pi.getFormu().getChamp("etat").setVisible(false);
        pi.getFormu().getChamp("idClient").setLibelle("Client");
        pi.getFormu().getChamp("idClient").setPageAppelComplete("client.Client","id","Client");
        pi.getFormu().getChamp("idClient").setPageAppelInsert("client/client-saisie.jsp","idClient;idClientlibelle","id;nom");
        pi.getFormu().getChamp("designation").setLibelle("D&eacute;signation");
        pi.getFormu().getChamp("idMagasin").setLibelle("Point");
        pi.getFormu().getChamp("idorigine").setLibelle("ID Origine");
        pi.getFormu().getChamp("datyPrevu").setLibelle("Date Pr&eacute;vue");
        pi.getFormu().getChamp("idReservation").setVisible(false);
        pi.getFormu().getChamp("echeance").setVisible(false);
        pi.getFormu().getChamp("reglement").setVisible(false);
        pi.getFormu().getChamp("estPrevu").setVisible(false);

        if(request.getParameter("idDmd")!=null && !request.getParameter("idDmd").isEmpty()){
            String idDmd = request.getParameter("idDmd");
            DmdPrix demandePrix = new DmdPrix();
            prerempli = (Proforma) demandePrix.genererProforma(idDmd, null);


        }

        String idDevis = request.getParameter("idDevis");
       /*if(idDevis != null && !idDevis.isEmpty()){
            Devis devis = new Devis();
            devis.setId(idDevis);
            prerempli = (Proforma) devis.genererProforma(idDevis, null);
        }*/

        Liste[] liste = new Liste[1];
        Magasin m = new Magasin();
        m.setNomTable("magasin");
        liste[0] = new Liste("idMagasin",m,"val","id");
        pi.getFormu().changerEnChamp(liste);
        pi.getFormu().getChamp("idMagasin").setLibelle("Magasin");

        pi.getFormufle().getChamp("idproduit_0").setLibelle("Produit");
        pi.getFormufle().getChamp("pu_0").setLibelle("Prix unitaire");
        pi.getFormufle().getChamp("iddemandeprixfille_0").setLibelle("Id demande de prix");
        pi.getFormufle().getChamp("compte_0").setLibelle("Compte");
        //pi.getFormufle().getChamp("idDevise_0").setLibelle("Devise");
        pi.getFormufle().getChamp("tva_0").setLibelle("TVA");
        //pi.getFormufle().getChamp("remise_0").setLibelle("Remise");
        pi.getFormufle().getChamp("designation_0").setLibelle("D&eacute;signation");
        pi.getFormufle().getChamp("qte_0").setLibelle("Quantit&eacute;");
        pi.getFormufle().getChamp("unite_0").setLibelle("Unite");
        pi.getFormufle().getChampMulitple("idDevise").setVisible(false);
        pi.getFormufle().getChampMulitple("remise").setVisible(false);

        for (int i = 0; i < pi.getNombreLigne(); i++) {
            pi.getFormufle().getChamp("id_"+i).setAutre("readonly");
            affichage.Champ.setPageAppelComplete(pi.getFormufle().getChampFille("idproduit"),"annexe.ProduitLib","id","PRODUIT_LIB","id;puVente;desce","id;pu;designation");
            pi.getFormufle().getChamp("idproduite_"+i).setAutreHidden("onchange='reloadPage(event)'");
            pi.getFormufle().getChamp("iddemandeprixfille_"+i).setAutre("readonly");
            pi.getFormufle().getChamp("unite_"+i).setAutre("readonly");
            pi.getFormufle().getChamp("compte_"+i).setAutre("readonly");
            //pi.getFormufle().getChamp("iddevise_"+i).setAutre("readonly");
            pi.getFormufle().getChamp("tva_"+i).setAutre("readonly");
        }

        affichage.Champ.setVisible(pi.getFormufle().getChampFille("id"),false);
        affichage.Champ.setVisible(pi.getFormufle().getChampFille("idProforma"),false);
        affichage.Champ.setVisible(pi.getFormufle().getChampFille("idOrigine"),false);
        affichage.Champ.setVisible(pi.getFormufle().getChampFille("puAchat"),false);
        affichage.Champ.setVisible(pi.getFormufle().getChampFille("puVente"),false);
        affichage.Champ.setVisible(pi.getFormufle().getChampFille("puRevient"),false);
        affichage.Champ.setVisible(pi.getFormufle().getChampFille("tauxdechange"),false);
        affichage.Champ.setVisible(pi.getFormufle().getChampFille("compte"),false);
        affichage.Champ.setVisible(pi.getFormufle().getChampFille("tva"),false);

        if(prerempli !=null){
            String idDmd = request.getParameter("idDmd");

            pi.getFormu().setDefaut(prerempli);
//            pi.setDefautFille(prerempli.getFille("PROFORMA_DETAILS", null, ""));
            pi.getFormu().getChamp("daty").setDefaut(Utilitaire.dateDuJour());
            ProformaDetails[] detFille = new DmdPrix().genererProformaDetails(idDmd, null);
            pi.setDefautFille(detFille);

        }

        String[] colOrdre = {"idproduit","designation", "tva", "qte", "pu","unite", "iddemandeprixfille", "compte"};
        pi.getFormufle().setColOrdre(colOrdre);

        pi.preparerDataFormu();

        //Variables de navigation
        String classeMere = "proforma.Proforma";
        String classeFille = "proforma.ProformaDetails";
        String butApresPost = "vente/proforma/proforma-fiche.jsp";
        String colonneMere = "idProforma";

        String[] ordre = {"daty"};
        //pi.getFormu().setOrdre(ordre);
        pi.getFormu().makeHtmlInsertTabIndex();
        pi.getFormufle().makeHtmlInsertTableauIndex();

        String titre = "Saisie Proforma";
        String apres = "apresMultiple.jsp";
        String acte = "insert";
        if(request.getParameter("acte")!=null){
            titre = "Modification Proforma";
            apres = "proforma/apresModifier.jsp";
            acte = "modifier";
            butApresPost = "proforma/proforma-fiche.jsp";
        }

%>
<div class="content-wrapper">
    <h1><%= titre %></h1>
    <form class='container' action="<%=pi.getLien()%>?but=<%=apres%>" method="post" >
        <%
            out.println(pi.getFormu().getHtmlInsert());
            out.println(pi.getFormufle().getHtmlTableauInsert());
        %>

        <input name="acte" type="hidden" id="nature" value="<%= acte %>">
        <input name="bute" type="hidden" id="bute" value="<%= butApresPost %>">
        <input name="classe" type="hidden" id="classe" value="<%= classeMere %>">
        <input name="classefille" type="hidden" id="classefille" value="<%= classeFille %>">
        <input name="nombreLigne" type="hidden" id="nombreLigne" value="<%= nombreLigne %>">
        <input name="colonneMere" type="hidden" id="colonneMere" value="<%= colonneMere %>">
    </form>

</div>

<%
} catch (Exception e) {
    e.printStackTrace();
%>
<script language="JavaScript">
    alert('<%=e.getMessage()%>');
    history.back();
</script>
<% }%>




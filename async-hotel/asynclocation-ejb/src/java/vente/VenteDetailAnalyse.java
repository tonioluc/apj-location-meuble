package vente;

import bean.CGenUtil;
import bean.ClassMAPTable;
import bean.ResultatEtSomme;
import utilitaire.Utilitaire;
import utils.ConstanteLocation;

import java.sql.Connection;
import java.sql.Date;

public class VenteDetailAnalyse extends ClassMAPTable {
    String idProduit,idProduitLib, id, reference;
    double duree, nbclient, ca;
    Date datejour;

    public Date getDatejour() {
        return datejour;
    }

    public void setDatejour(Date datejour) {
        this.datejour = datejour;
    }

    public VenteDetailAnalyse() {
        this.setNomTable("VENTE_DETAIL_ANALYSE");
    }

    public String getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(String idProduit) {
        this.idProduit = idProduit;
    }

    public String getIdProduitLib() {
        return idProduitLib;
    }

    public void setIdProduitLib(String idProduitLib) {
        this.idProduitLib = idProduitLib;
    }

    public double getDuree() {
        return duree;
    }

    public void setDuree(double duree) {
        this.duree = duree;
    }

    public double getNbclient() {
        return nbclient;
    }

    public void setNbclient(double nbclient) {
        this.nbclient = nbclient;
    }

    public double getCa() {
        return ca;
    }

    public void setCa(double ca) {
        this.ca = ca;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String getTuppleID() {
        return this.getIdProduit();
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    public String generateQueryCore(Date datemin, Date datemax) {
        return
                "SELECT \n" +
                        "    a.id,\n" +
                        "    i.reference,\n" +
                        "    a.idproduitlib,\n" +
                        "    a.duree,\n" +
                        "    a.nbclient,\n" +
                        "    a.ca,\n" +
                        "    SYSDATE          AS datejour\n" +
                        "FROM (\n" +
                        "    SELECT\n" +
                        "        vd.IDPRODUIT AS id,\n" +
                        "        i.LIBELLE    AS idproduitlib,\n" +
                        "        SUM(vd.QTE) AS duree,\n" +
                        "        COUNT(DISTINCT v.IDCLIENT) AS nbclient,\n" +
                        "        SUM(vd.QTE * vd.pu * vd.NOMBRE * NVL(i.DURRE,1)) AS ca\n" +
                        "    FROM VENTE_DETAILS vd\n" +
                        "    JOIN VENTE v ON v.ID = vd.IDVENTE\n" +
                        "    JOIN AS_INGREDIENTS i ON i.ID = vd.IDPRODUIT\n" +
                        "    WHERE v.ETAT >= 11\n" +
                        "      AND vd.IDPRODUIT NOT IN (\n" +
                        "          '" + ConstanteLocation.id_produit_caution + "',\n" +
                        "          '" + ConstanteLocation.id_produit_transport_aller + "',\n" +
                        "          '" + ConstanteLocation.id_produit_transport_retour + "',\n" +
                        "          '" + ConstanteLocation.id_produit_transport_pers + "'\n" +
                        "      )\n" +
                        "      AND v.DATY BETWEEN DATE '" + datemin + "' AND DATE '" + datemax + "'\n" +
                        "    GROUP BY vd.IDPRODUIT, i.LIBELLE\n" +
                        ") a\n" +
                        "LEFT JOIN AS_INGREDIENTS i ON i.ID = a.id";
    }

    public ResultatEtSomme rechercherPage(String[] colInt, String[]valInt, int numPage, String apresWhere, String[]nomColSomme, Connection c, int npp) throws Exception {
        String daty= Utilitaire.dateDuJour();
        String daty2=Utilitaire.dateDuJour();
        if(valInt!=null&&valInt.length>1) {
            daty=valInt[0].toString();
            daty2=valInt[1].toString();
        }
        Date datemin = Utilitaire.stringDate(daty);
        Date datemax = Utilitaire.stringDate(daty2);
        String query = this.generateQueryCore(datemin, datemax);
        return CGenUtil.rechercherPage(this,query,numPage,nomColSomme,apresWhere,c,npp);
    }
}

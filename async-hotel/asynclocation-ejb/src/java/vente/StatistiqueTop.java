package vente;

import bean.CGenUtil;
import bean.ClassMAPTable;
import bean.ResultatEtSomme;
import utilitaire.Utilitaire;
import utils.ConstanteLocation;

import java.sql.Connection;
import java.sql.Date;

public class StatistiqueTop extends ClassMAPTable {
    String idProduit,idProduitLib,idClient,idClientLib;
    double caclient,caproduit;
    Date datejour;
    int top;

    public int getTop() {
        return top;
    }
    public void setTop(int top) {
        this.top = top;
    }

    public Date getDatejour() {
        return datejour;
    }

    public void setDatejour(Date datejour) {
        this.datejour = datejour;
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

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getIdClientLib() {
        return idClientLib;
    }

    public void setIdClientLib(String idClientLib) {
        this.idClientLib = idClientLib;
    }

    public double getCaclient() {
        return caclient;
    }

    public void setCaclient(double caclient) {
        this.caclient = caclient;
    }

    public double getCaproduit() {
        return caproduit;
    }

    public void setCaproduit(double caproduit) {
        this.caproduit = caproduit;
    }

    public StatistiqueTop(){
        super.setNomTable("StatistiqueTop");
    }

    @Override
    public String getTuppleID() {
        return this.getIdProduit();
    }

    @Override
    public String getAttributIDName() {
        return "idProduit";
    }

    public String generateQueryCore(Date datemin, Date datemax,int rang) {
        return "WITH ventes_agg AS (\n" +
                "    SELECT\n" +
                "        RANG,\n" +
                "        IDCLIENT,\n" +
                "        IDCLIENTLIB,\n" +
                "        CA\n" +
                "    FROM (SELECT ROW_NUMBER() OVER (ORDER BY SUM(MONTANTTOTAL) DESC) AS RANG,\n" +
                "                 IDCLIENT,\n" +
                "                 IDCLIENTLIB,\n" +
                "                 SUM(MONTANTTOTAL)                                   AS CA\n" +
                "          FROM VENTE_CPL\n" +
                "          WHERE ETAT >= 11\n" +
                "            AND (DATY >= DATE '"+datemin+"' AND DATY <= DATE '"+datemax+"')\n" +
                "          GROUP BY IDCLIENT, IDCLIENTLIB\n" +
                "          order by SUM(NVL(MONTANTTOTAL, 0)) desc)\n" +
                "    where RANG<"+rang+"\n" +
                "),\n" +
                "ventesd_agg AS (\n" +
                "    SELECT\n" +
                "        RANG,\n" +
                "        IDPRODUIT,\n" +
                "        IDPRODUITLIB,\n" +
                "        qteJour\n" +
                "    FROM ( SELECT\n" +
                "            ROW_NUMBER() OVER (ORDER BY SUM(NVL(MONTANT, 0)) DESC) AS RANG,\n" +
                "            IDPRODUIT,\n" +
                "            IDPRODUITLIB,\n" +
                "            SUM(NVL(MONTANT, 0)) AS qteJour\n" +
                "        FROM\n" +
                "            VENTE_DETAILS_CPL\n" +
                "        WHERE\n" +
                "            DATERESERVATION IS NOT NULL\n" +
                "            AND UPPER(TRIM(IDPRODUITLIB)) NOT IN (\n" +
                "                'TRANSPORT ALLER',\n" +
                "                'TRANSPORT PERS',\n" +
                "                'CAUTION',\n" +
                "                'TRANSPORT RETOUR'\n" +
                "            )\n" +
                "        AND (DATERESERVATION>= DATE '"+datemin+"' AND DATERESERVATION<= DATE '"+datemax+"')\n" +
                "        GROUP BY\n" +
                "            IDPRODUITLIB,\n" +
                "            IDPRODUIT\n" +
                "        order by SUM(NVL(MONTANT, 0)) desc)\n" +
                "    where RANG<"+rang+"\n" +
                ")\n" +
                "select\n" +
                "    vd.IDPRODUIT,vd.IDPRODUITLIB,vd.qteJour as caproduit, v.IDCLIENT,v.IDCLIENTLIB,v.CA as caclient, sysdate as datejour,3 as top\n" +
                "from\n" +
                "    ventes_agg v full join ventesd_agg vd on v.rang=vd.RANG";
    }

    public ResultatEtSomme rechercherPage(String[] colInt, String[]valInt, int numPage, String apresWhere, String[]nomColSomme, Connection c, int npp) throws Exception {
        String daty= Utilitaire.dateDuJour();
        String daty2=Utilitaire.dateDuJour();
        int rang = 3;
        if(valInt!=null&&valInt.length>1) {
            daty = valInt[0].toString();
            daty2 = valInt[1].toString();
            rang = Integer.parseInt(valInt[3].toString())+1;
        }
        Date datemin = Utilitaire.stringDate(daty);
        Date datemax = Utilitaire.stringDate(daty2);
        String query = this.generateQueryCore(datemin, datemax,rang);
        System.err.println(query);
        return CGenUtil.rechercherPage(this,query,numPage,nomColSomme,apresWhere,c,npp);
    }
}

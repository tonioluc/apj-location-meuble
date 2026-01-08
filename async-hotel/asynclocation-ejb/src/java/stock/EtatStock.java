package stock;

import bean.CGenUtil;
import bean.ClassMAPTable;

import java.sql.Connection;
import java.sql.Date;

import bean.ResultatEtSomme;
import utilitaire.Utilitaire;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Angela
 */
public class EtatStock extends ClassMAPTable {
    protected String id;
    protected String idProduitLib;
    protected String idTypeProduit;
    protected String idTypeProduitLib;
    protected String idMagasin;
    protected String idMagasinLib;
    protected Date dateDernierInventaire,dateDernierMouvement, daty;
    protected double quantite;
    protected double entree;
    protected double sortie;
    protected double reste,montantReste,montantSortie,montantEntree;
    protected double puVente;
    protected String idUnite;
    protected String idUniteLib;
    protected String idPoint;
    protected String idPointLib;
    protected  double pu;
    protected String unite;
    protected String reference;
    private String image;

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public EtatStock() {
        this.setNomTable("v_etatstock_ing");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProduitLib() {
        return idProduitLib;
    }

    public void setIdProduitLib(String idProduitLib) {
        this.idProduitLib = idProduitLib;
    }

    public String getIdTypeProduit() {
        return idTypeProduit;
    }

    public void setIdTypeProduit(String idTypeProduit) {
        this.idTypeProduit = idTypeProduit;
    }

    public String getIdTypeProduitLib() {
        return idTypeProduitLib;
    }

    public void setIdTypeProduitLib(String idTypeProduitLib) {
        this.idTypeProduitLib = idTypeProduitLib;
    }

    public String getIdMagasin() {
        return idMagasin;
    }

    public void setIdMagasin(String idMagasin) {
        this.idMagasin = idMagasin;
    }

    public String getIdMagasinLib() {
        return idMagasinLib;
    }

    public void setIdMagasinLib(String idMagasinLib) {
        this.idMagasinLib = idMagasinLib;
    }

    public Date getDateDernierInventaire() {
        return dateDernierInventaire;
    }

    public void setDateDernierInventaire(Date dateDernierInventaire) {
        this.dateDernierInventaire = dateDernierInventaire;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getEntree() {
        return entree;
    }

    public void setEntree(double entree) {
        this.entree = entree;
    }

    public double getSortie() {
        return sortie;
    }

    public void setSortie(double sortie) {
        this.sortie = sortie;
    }

    public double getReste() {
        return reste;
    }

    public void setReste(double reste) {
        this.reste = reste;
    }

    public String getIdUnite() {
        return idUnite;
    }

    public void setIdUnite(String idUnite) {
        this.idUnite = idUnite;
    }

    public String getIdUniteLib() {
        return idUniteLib;
    }

    public void setIdUniteLib(String idUniteLib) {
        this.idUniteLib = idUniteLib;
    }


    public String getFieldDateName() {
        return "dateDernierInventaire";
    }

    public double getPuVente() {
        return puVente;
    }

    public void setPuVente(double puVente) {
        this.puVente = puVente;
    }


    public String generateQueryCore(Date dateMin, Date dateMax) {
        String query =  " SELECT  " +
                "	inv.IDPRODUIT AS ID, " +
                "	p.libelle AS idproduitLib, " +
                "	p.CATEGORIEINGREDIENT, " +
                "	tp.desce AS idtypeproduitlib, " +
                "	inv.idmagasin, " +
                "	mag.desce AS idmagasinlib, " +
                "	inv.DATY dateDernierinventaire, " +
                "	NVL(inv.QUANTITE,0) QUANTITE, " +
                "	NVL(mvt.ENTREE,0) ENTREE,  " +
                "	NVL(mvt.SORTIE,0) SORTIE,  " +
                "	NVL(mvt.ENTREE,0)+NVL(inv.QUANTITE,0)-NVL(mvt.SORTIE,0) reste, " +
                "	p.UNITE, " +
                "	u.desce AS idunitelib, " +
                "   CAST(NVL(p.PV ,0) AS NUMBER(30,2)) PUVENTE, " +
                "	mag.IDPOINT, " +
                "	mag.IDTYPEMAGASIN "+
                "FROM  " +
                "	INVENTAIRE_FILLE_CPL inv, " +
                "	( " +
                "       SELECT  " +
                "			inv.IDPRODUIT , " +
                "                   inv.IDMAGASIN, "+
                "			MAX(inv.DATY) maxDateInventaire " +
                "		FROM  " +
                "			INVENTAIRE_FILLE_CPL inv  " +
                "		WHERE  " +
                "			inv.ETAT = 11  " +
                "			AND inv.DATY <= '"+Utilitaire.datetostring(dateMin)+"' " +
                "		GROUP BY inv.IDPRODUIT,inv.IDMAGASIN " +
                "	) invm, " +
                "	( " +
                "		SELECT  " +
                "			m.IDPRODUIT , " +
                "                   dinv.IDMAGASIN, "+
                "			SUM(nvl(m.ENTREE,0)) ENTREE ,  " +
                "			SUM(nvl(m.SORTIE ,0)) SORTIE  " +
                "		FROM  " +
                "			MVTSTOCKFILLELIB m , " +
                "			( " +
                "			SELECT  " +
                "				inv.IDPRODUIT , " +
                "                           inv.IDMAGASIN,"+
                "				MAX(inv.DATY) maxDateInventaire " +
                "			FROM  " +
                "				INVENTAIRE_FILLE_CPL inv  " +
                "			WHERE  " +
                "				inv.ETAT = 11  " +
                "				AND inv.DATY <= '"+Utilitaire.datetostring(dateMin)+"' " +
                "			GROUP BY inv.IDPRODUIT,inv.IDMAGASIN " +
                "			) dinv " +
                "		WHERE  " +
                "			m.IDPRODUIT = dinv.IDPRODUIT(+) " +
                "                   AND m.IDMAGASIN = dinv.IDMAGASIN(+)"+
                "			AND m.DATY > dinv.maxDateInventaire " +
                "			AND m.DATY <= '"+Utilitaire.datetostring(dateMax)+"' " +
                "		GROUP BY m.IDPRODUIT,dinv.IDMAGASIN " +
                "	) mvt, " +
                "	as_ingredients p, " +
                "	CATEGORIEINGREDIENT tp, " +
                "	magasin mag, " +
                "	unite u " +
                "WHERE  " +
                "	inv.DATY = invm.maxDateInventaire " +
                "	AND inv.IDMAGASIN = invm.IDMAGASIN " +
                "	AND inv.IDPRODUIT = invm.IDPRODUIT " +
                "	AND inv.IDPRODUIT = mvt.IDPRODUIT(+) " +
                "	AND inv.IDMAGASIN = mvt.IDMAGASIN(+) " +
                "	AND inv.IDPRODUIT = p.ID(+) " +
                "	AND p.CATEGORIEINGREDIENT = tp.ID " +
                "	AND inv.idmagasin = mag.ID " +
                "	AND p.UNITE = u.ID(+) "+
                "	AND ( mvt.sortie > 0  OR mvt.ENTREE > 0 ) "+
                "   AND inv.ETAT >= 11 ";
        return query;
    }

    @Override
    public String getTuppleID() {
        return this.id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    @Override
    public String getValColLibelle() {
        return this.getIdProduitLib()+";"+this.getPuVente();
    }

    public ResultatEtSomme rechercherPage(String[] colInt, String[]valInt, int numPage, String apresWhere, String[]nomColSomme, Connection c, int npp) throws Exception {
        String daty=Utilitaire.dateDuJour();
        if(valInt!=null&&valInt.length>1) {daty=valInt[1].toString();}
        String req= "SELECT\n" +
                "\tp.ID AS ID,\n" +
                "\tp.LIBELLE AS idproduitLib,\n" +
                "\tp.CATEGORIEINGREDIENT,\n" +
                "\ttp.ID AS idTypeProduit,\n" +
                "\ttp.DESCE AS idtypeproduitlib,\n" +
                "\tms.IDMAGASIN,\n" +
                "\tmag.DESCE AS idmagasinlib,\n" +
                "\tTO_DATE('01-01-2001', 'DD-MM-YYYY') AS dateDernierMouvement,\n" +
                "\tms.quantite AS QUANTITE,\n" +
                "\tms.entree AS ENTREE,\n" +
                "\tms.sortie AS SORTIE,\n" +
                "\tms.quantite AS reste,\n" +
                "\tp.UNITE,\n" +
                "\tu.DESCE AS idunitelib,\n" +
                "\tCAST(NVL(p.PV, 0) AS NUMBER(30, 2)) AS PUVENTE,\n" +
                "\tmag.IDPOINT,\n" +
                "\tmag.IDTYPEMAGASIN,\n" +
                "\tp.SEUILMIN,\n" +
                "\tp.SEUILMAX,\n" +
                "\tms.montantEntree,\n" +
                "\tms.montantSortie,\n" +
                "\tp.pu,\n" +
                "\tms.montant as montantReste,\n" +
                "p.reference,\n"+
                "p.image\n"+
                "FROM AS_INGREDIENTS p\n" +
                "LEFT JOIN (SELECT\n" +
                "    mf.IDPRODUIT,\n" +
                "    SUM(NVL(mf.ENTREE,0)) AS ENTREE,\n" +
                "    SUM(NVL(mf.SORTIE,0)) AS SORTIE,\n" +
                "    SUM(NVL(mf.ENTREE,0)) - SUM(NVL(mf.SORTIE,0)) AS quantite,\n" +
                "    cast(sum(mf.montantEntree) as number(30,2))  AS montantEntree,\n" +
                "    cast(sum(mf.montantSortie) as number(30,2))  AS montantSortie,\n" +
                "    CAST(NVL(ai.PU, 0) * (SUM(NVL(mf.ENTREE,0)) - SUM(NVL(mf.SORTIE,0))) AS NUMBER(30,2)) AS montant,\n" +
                "    m.IDMAGASIN\n" +
                "FROM\n" +
                "    mvtStockFilleMontant mf\n" +
                "JOIN MVTSTOCK m ON m.id = mf.IDMVTSTOCK\n" +
                "JOIN AS_INGREDIENTS ai ON ai.ID = mf.IDPRODUIT\n" +
                "WHERE\n" +
                "    m.ETAT >= 11\n" +
                "    AND mf.IDPRODUIT IS NOT NULL\n" +
                "    AND m.daty<='"+daty+"' \n" +
                "GROUP BY\n" +
                "    mf.IDPRODUIT,\n" +
                "    ai.PU,m.IDMAGASIN) ms ON ms.IDPRODUIT = p.ID\n" +
                "LEFT JOIN CATEGORIEINGREDIENT tp ON p.CATEGORIEINGREDIENT = tp.ID\n" +
                "LEFT JOIN MAGASINPOINT mag ON ms.IDMAGASIN = mag.ID\n" +
                "LEFT JOIN AS_UNITE u ON p.UNITE = u.ID\n" +
                "where NVL(ms.ENTREE, 0)>0 or NVL(ms.SORTIE, 0)>0";
        ResultatEtSomme rs= CGenUtil.rechercherPage(this,req,numPage,nomColSomme,apresWhere,c,npp);
        return rs;
    }


    public Date getDateDernierMouvement() {
        return dateDernierMouvement;
    }

    public void setDateDernierMouvement(Date dateDernierMouvement) {
        this.dateDernierMouvement = dateDernierMouvement;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

    public double getMontantReste() {
        return montantReste;
    }

    public void setMontantReste(double montantReste) {
        this.montantReste = montantReste;
    }

    public double getMontantSortie() {
        return montantSortie;
    }

    public void setMontantSortie(double montantSortie) {
        this.montantSortie = montantSortie;
    }

    public double getMontantEntree() {
        return montantEntree;
    }

    public void setMontantEntree(double montantEntree) {
        this.montantEntree = montantEntree;
    }

    public String getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(String idPoint) {
        this.idPoint = idPoint;
    }

    public String getIdPointLib() {
        return idPointLib;
    }

    public void setIdPointLib(String idPointLib) {
        this.idPointLib = idPointLib;
    }

    public double getPu() {
        return pu;
    }

    public void setPu(double pu) {
        this.pu = pu;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

}

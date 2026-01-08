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
public class EtatStockMagasin extends ClassMAPTable {
    protected String id;
    protected String idProduitLib;
    protected String idTypeProduit;
    protected String idTypeProduitLib;
    protected String idMagasin;
    protected String idMagasinLib;
    protected Date dateDernierInventaire,dateDernierMouvement, daty;
    protected double quantite;
    protected double montantReste,montantSortie,montantEntree;
    protected double puVente;
    protected String idUnite;
    protected String idUniteLib;
    protected String idPoint;
    protected String idPointLib;
    protected  double pu;
    protected String unite;
    protected String reference;
    private String image;
    private double PNT000122; //bel air
    private double PNT000124; //ankadikely
    private double PNT000103; //neurones
    private double PNT000084; //domaine
    private double PNT000086; //Ankorahotra
    private double PNT000085; //Atelier
    private double total;


    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public EtatStockMagasin() {
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

    public ResultatEtSomme rechercherPage(String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, Connection c, int npp) throws Exception {
        String daty = Utilitaire.dateDuJour();
        if (valInt != null && valInt.length > 1) {
            daty = valInt[1].toString();
        }

        String req =
                "SELECT\n" +
                        "    p.ID AS ID,\n" +
                        "    p.LIBELLE AS idproduitLib,\n" +
                        "    p.CATEGORIEINGREDIENT,\n" +
                        "    tp.ID AS idTypeProduit,\n" +
                        "    tp.DESCE AS idtypeproduitlib,\n" +
                        "    TO_DATE('01-01-2001', 'DD-MM-YYYY') AS dateDernierMouvement,\n" +
                        "    CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000122' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) AS PNT000122,\n" +
                        "    CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000124' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) AS PNT000124,\n" +
                        "    CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000103' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) AS PNT000103,\n" +
                        "    CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000084' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) AS PNT000084,\n" +
                        "    CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000086' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) AS PNT000086,\n" +
                        "    CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000085' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) AS PNT000085,\n" +
                        "    (\n" +
                        "        CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000122' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) +\n" +
                        "        CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000124' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) +\n" +
                        "        CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000103' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) +\n" +
                        "        CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000084' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) +\n" +
                        "        CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000085' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) +\n" +
                        "        CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000086' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2))\n" +
                        "    ) AS TOTAL,\n" +
                        "    p.UNITE,\n" +
                        "    u.DESCE AS idunitelib,\n" +
                        "    CAST(NVL(p.PV, 0) AS NUMBER(30, 2)) AS PUVENTE,\n" +
                        "    p.SEUILMIN,\n" +
                        "    p.SEUILMAX,\n" +
                        "    p.pu,\n" +
                        "    p.reference,\n" +
                        "    p.image\n" +
                        "FROM AS_INGREDIENTS p\n" +
                        "LEFT JOIN (\n" +
                        "    SELECT\n" +
                        "        mf.IDPRODUIT,\n" +
                        "        m.IDMAGASIN,\n" +
                        "        SUM(NVL(mf.ENTREE, 0)) - SUM(NVL(mf.SORTIE, 0)) AS quantite\n" +
                        "    FROM mvtStockFilleMontant mf\n" +
                        "    JOIN MVTSTOCK m ON m.id = mf.IDMVTSTOCK\n" +
                        "    WHERE m.ETAT >= 11\n" +
                        "      AND mf.IDPRODUIT IS NOT NULL\n" +
                        "      AND m.daty <= '" + daty + "'\n" +
                        "    GROUP BY mf.IDPRODUIT, m.IDMAGASIN\n" +
                        ") ms ON ms.IDPRODUIT = p.ID\n" +
                        "LEFT JOIN CATEGORIEINGREDIENT tp ON p.CATEGORIEINGREDIENT = tp.ID\n" +
                        "LEFT JOIN AS_UNITE u ON p.UNITE = u.ID\n" +
                        "GROUP BY p.ID, p.LIBELLE, p.CATEGORIEINGREDIENT, tp.ID, tp.DESCE, p.UNITE, u.DESCE, p.PV, p.SEUILMIN, p.SEUILMAX, p.pu, p.reference, p.image";


        ResultatEtSomme rs = CGenUtil.rechercherPage(this, req, numPage, nomColSomme, apresWhere, c, npp);
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

    public double getPNT000122() {
        return PNT000122;
    }

    public void setPNT000122(double PNT000122) {
        this.PNT000122 = PNT000122;
    }

    public double getPNT000124() {
        return PNT000124;
    }

    public void setPNT000124(double PNT000124) {
        this.PNT000124 = PNT000124;
    }

    public double getPNT000103() {
        return PNT000103;
    }

    public void setPNT000103(double PNT000103) {
        this.PNT000103 = PNT000103;
    }

    public double getPNT000084() {
        return PNT000084;
    }

    public void setPNT000084(double PNT000084) {
        this.PNT000084 = PNT000084;
    }

    public double getPNT000086() {
        return PNT000086;
    }

    public void setPNT000086(double PNT000086) {
        this.PNT000086 = PNT000086;
    }

    public double getPNT000085() {
        return PNT000085;
    }

    public void setPNT000085(double PNT000085) {
        this.PNT000085 = PNT000085;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

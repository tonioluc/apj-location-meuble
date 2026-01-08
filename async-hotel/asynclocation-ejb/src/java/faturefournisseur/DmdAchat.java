package faturefournisseur;

import bean.ClassMere;
import produits.Ingredients;
import utilitaire.UtilDB;

import java.sql.Connection;
import java.sql.Date;

public class DmdAchat extends ClassMere {
    private String id;
    private Date daty;
    private String fournisseur;
    private String remarque;
    boolean estHistorise = true;
    String idCommande;
    String idBu;

    public DmdAchat() throws Exception {
        this.setNomTable("DMDACHAT");
        this.setLiaisonFille("idmere");
        this.setNomClasseFille("faturefournisseur.DmdAchatFille");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date date) {
        this.daty = date;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public FactureFournisseurDetails[] getFactureFournisseurDetails() throws Exception {
        DmdAchatFille[] dmdAchatFille = (DmdAchatFille[])this.getFille("DMDACHATFILLE",null,"");
        FactureFournisseurDetails[] factureFoDetails = new  FactureFournisseurDetails[dmdAchatFille.length];

        for (int i = 0; i < dmdAchatFille.length; i++) {
            factureFoDetails[i] = new FactureFournisseurDetails();
            Ingredients ingredients = new Ingredients();
            ingredients = (Ingredients) ingredients.getById(dmdAchatFille[i].getIdproduit(),"AS_INGREDIENTS",null);
            factureFoDetails[i].setIdProduit(dmdAchatFille[i].getIdproduit());
            factureFoDetails[i].setCompte(ingredients.getCompte_achat());
            factureFoDetails[i].setPu(dmdAchatFille[i].getPu());
            factureFoDetails[i].setQte(dmdAchatFille[i].getQuantite());
            factureFoDetails[i].setTva(dmdAchatFille[i].getTva());
        }

        return factureFoDetails;
    }

    public FactureFournisseurDetails[] getFactureFournisseurDetails(Connection c) throws Exception {
        int indice = 0;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                indice = 1;
            }
            DmdAchatFille[] dmdAchatFille = (DmdAchatFille[])this.getFille("DMDACHATFILLE",c,"");
            FactureFournisseurDetails[] factureFoDetails = new  FactureFournisseurDetails[dmdAchatFille.length];

            for (int i = 0; i < dmdAchatFille.length; i++) {
                Ingredients ing = (Ingredients)new Ingredients().getById(dmdAchatFille[i].getIdproduit(),"AS_INGREDIENTS",c);
                factureFoDetails[i] = new FactureFournisseurDetails();
                factureFoDetails[i].setIdProduit(dmdAchatFille[i].getIdproduit());
                factureFoDetails[i].setPu(dmdAchatFille[i].getPu());
                factureFoDetails[i].setQte(dmdAchatFille[i].getQuantite());
                factureFoDetails[i].setTva(dmdAchatFille[i].getTva());
                factureFoDetails[i].setIdDevise("AR");
                factureFoDetails[i].setCompte(ing.getCompte_achat());
            }
            return factureFoDetails;
        }catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }finally{
            if(indice == 1 && c!=null)c.close();
        }
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("DMDA", "GETSEQDMDACHAT");
        this.setId(makePK(c));
    }

    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    @Override
    public boolean isEstHistorise() {
        return estHistorise;
    }

    @Override
    public void setEstHistorise(boolean estHistorise) {
        this.estHistorise = estHistorise;
    }

    public String getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(String idCommande) {
        this.idCommande = idCommande;
    }

    public String getIdBu() {
        return idBu;
    }

    public void setIdBu(String idBu) {
        this.idBu = idBu;
    }

    public proforma.ProformaAchat genererProforma(String id, Connection c) throws Exception {
        boolean estOuvert = false;
        try {
            if (c == null){
                estOuvert = true;
                c = new utilitaire.UtilDB().GetConn();
            }

            DmdAchat demandePrix = new DmdAchat();
            demandePrix = (DmdAchat) demandePrix.getById(id, "DmdAchat", c);
            if (demandePrix == null) {
                throw new Exception("Demande d'achat non trouvée pour l'ID: " + id);
            }

            DmdAchatFille demandePrixFille = new DmdAchatFille();
            demandePrixFille.setNomTable("DmdAchatFille");
            demandePrixFille.setIdmere(demandePrix.getId());
            DmdAchatFille[] demandePrixFilleLibs = (DmdAchatFille[]) bean.CGenUtil.rechercher(demandePrixFille, null, null, c, "");

            proforma.ProformaAchat proforma = new proforma.ProformaAchat();
            proforma.setIdDmdAchat(demandePrix.getId());
            proforma.setDaty(demandePrix.getDaty());
            proforma.setRemarque("Proforma de la demande d'achat: " + demandePrix.getId());

            if(demandePrixFilleLibs != null) {
                java.util.List<proforma.ProformaAchatDetail> details = new java.util.ArrayList<>();
                for (int i = 0; i < demandePrixFilleLibs.length; i++) {
                    proforma.ProformaAchatDetail detail = demandePrixFilleLibs[i].genererProformaAchatDt();
                    details.add(detail);
                }
                proforma.setFille(details.toArray(new proforma.ProformaAchatDetail[]{}));
            }

            return proforma;
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            if (c != null && estOuvert) {
                c.close();
            }
        }
    }
}

package stock;

import bean.ClassMAPTable;
import produits.Ingredients;

import java.sql.Connection;
import java.sql.Date;

public class MvtStockArrivage extends MvtStock {
    public MvtStockArrivage() throws Exception {
        this.setNomTable("MVTSTOCK");
        this.setNomClasseFille("stock.MvtStockFille");
        this.setLiaisonFille("idMvtStock");
    }

    @Override
    public String getTuppleID() {
        return super.getTuppleID();
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    @Override
    public ClassMAPTable createObject(String u, Connection c) throws Exception {
        this.setIdobjet("ARRIVAGE");
        return super.createObject(u, c);
    }

    @Override
    public Object validerObject(String u, Connection c) throws Exception {
        MvtStockFille[] mvtStockFille = (MvtStockFille[]) this.getFille();
        if (this.getIdobjet().equalsIgnoreCase("ARRIVAGE")) {
            for (int i = 0; i < mvtStockFille.length; i++) {
                Ingredients ingredients = new Ingredients();
                ingredients.setId(mvtStockFille[i].getIdProduit());
                ingredients = (Ingredients) ingredients.getById(mvtStockFille[i].getIdProduit(), "AS_INGREDIENTS", c);
                ingredients.setQuantiteParPack(ingredients.getQuantiteParPack() + mvtStockFille[i].getEntree());
                ingredients.updateObject(u, c);
            }
        }
        return super.validerObject(u, c);
    }
}

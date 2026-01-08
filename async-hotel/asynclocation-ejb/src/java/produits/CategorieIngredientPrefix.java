package produits;

public class CategorieIngredientPrefix extends CategorieIngredient{
    private String prefix, id, val;
    private int nextnumber;
    public CategorieIngredientPrefix() {
        this.setNomTable("CATEGORIEINGREDIENTPREFIX");
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getNextnumber() {
        return nextnumber;
    }

    public void setNextnumber(int nextnumber) {
        this.nextnumber = nextnumber;
    }
}

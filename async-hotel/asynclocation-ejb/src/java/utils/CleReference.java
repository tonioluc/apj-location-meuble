package utils;

import bean.CGenUtil;
import bean.ClassMAPTable;

import java.sql.Connection;

public class CleReference extends ClassMAPTable {
    private String id;
    private String nomTab;
    private int nextVal;

    public CleReference(){
        this.setNomTable("CLEREFERENCE");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomTab() {
        return nomTab;
    }

    public void setNomTab(String nomTab) {
        this.nomTab = nomTab;
    }

    public int getNextVal() {
        return nextVal;
    }

    public void setNextVal(int nextVal) {
        this.nextVal = nextVal;
    }

    @Override
    public ClassMAPTable createObject(String u, Connection c) throws Exception {
        String awhere = " AND id='"+this.getId()+"' AND nomTab='"+this.getNomTab()+"' ORDER BY nextVal DESC";
        CleReference[] clr = (CleReference[]) CGenUtil.rechercher(new CleReference(),null,null,c,awhere);
        if (clr.length == 0) {
            this.setNextVal(1);
            return super.createObject(u, c);
        }
        this.setNextVal(clr[0].getNextVal()+1);
        return super.createObject(u, c);
    }

    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }
}

package vente.dmdprix;

import bean.CGenUtil;

public class DmdPrixLib extends DmdPrix{

    private String clientLib;

    public DmdPrixLib() throws Exception {
        this.setNomTable("DMDPRIXLIB");
    }


    public String getClientLib() {
        return clientLib;
    }

    public void setClientLib(String clientLib) {
        this.clientLib = clientLib;
    }

    public DmdPrixFilleLib[] getFilleLib() throws  Exception {
        DmdPrixFilleLib search = new DmdPrixFilleLib();
        search.setIdMere(getId());
        return (DmdPrixFilleLib[]) CGenUtil.rechercher(search, null, null, "");
    }
}

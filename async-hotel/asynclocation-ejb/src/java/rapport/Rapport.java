package rapport;

import bean.CGenUtil;
import utilitaire.UtilDB;
import utils.ConstanteAsync;

import java.sql.Connection;
import java.sql.Date;

public class Rapport {

    public String[] exportFiles(String daty, Connection c)throws Exception{

        String[] vals = new String[3];

        ImpressionPdf impressionPdf = new ImpressionPdf();
//        String daty1 = "2025-09-08";
        vals[0] = impressionPdf.vente_liste(daty, daty, "vente", c);
        vals[1] = impressionPdf.vente_liste_mere_fille(daty, daty, "venteDetails", c);
        vals[2] = impressionPdf.vente_liste_excel(daty, daty,"ventes", c);

        return vals;
    }

    public void sendMail(String daty)throws Exception{
        Connection c=null;
        try{
            c=new UtilDB().GetConn();

            MailRapport[] mail=getListeMail("MAILRAPPORT",c);

            if(mail!=null && mail.length>0){
                String corpsMail="<p>\n" +
                        "        Veuillez trouver ci-joint le <strong>rapport journalier</strong> relatif aux ventes effectu\u00E9es en date du\n" +
                        "        <strong>"+daty+"</strong>.\n" +
                        "      </p>";
                String objet="";

                objet = "Rapport Journalier du "+daty;

                System.out.println("---------------------------------");
                System.out.println("---------------------------------");

                Mail retour=new Mail(ConstanteAsync.getMailRapport()[0],ConstanteAsync.getMailRapport()[1],objet);
                retour.setMessage(corpsMail);
                retour.setTo(mail[0].getVal());
                if(mail.length>1){
                    String[] cc=new String[mail.length-1];
                    for(int i=1;i<mail.length;i++){
                        cc[i-1]=mail[i].getVal();
                    }
                    retour.setCc(cc);
                }

                String[] files = exportFiles(daty, c);
                retour.setFiles(files);

                retour.send();
            }

        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally{
            if(c!=null)c.close();
        }


    }

    public static MailRapport[] getListeMail(String nomTable, Connection c)throws Exception{
        try{
            MailRapport mail=new MailRapport();
            return (MailRapport[]) CGenUtil.rechercher(mail,null,null,c,"");
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}

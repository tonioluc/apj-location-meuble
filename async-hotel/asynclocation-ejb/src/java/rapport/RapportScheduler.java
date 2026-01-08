package rapport;

import utilitaire.Utilitaire;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.sql.Date;

@Singleton
@Startup
public class RapportScheduler {

    @Schedule(hour = "17", minute = "30", second = "0", persistent = false)
    public void envoyerCRQuotidien() {
        try {

            Date daty = Utilitaire.dateDuJourSql();
            String date = daty.toString();
            System.out.println(date);
            Rapport rapport = new Rapport();
            rapport.sendMail(date);
            System.out.println("Email envoye");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
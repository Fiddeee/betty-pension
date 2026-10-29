/* TODO
- Lav funktion til at hente data fra tblOphold
- Skal hentes som seperate enheder



 */

import java.util.List;
import java.util.Map;

public class dogStay {
    /**
     * Udskriver alle ophold fra databasen.
     */
    public static void printAllStays() {
        dbConnector.getJdbi().useHandle(handle -> {
            List<Map<String, Object>> stays = handle.createQuery(""" 
                SELECT fldOpholdID, fldHundID, fldAnkomstDato,
                       fldForventetAfrejse, fldFaktiskAfrejse
                FROM tblOphold
                ORDER BY fldOpholdID
                """)
                    .mapToMap()
                    .list();

            if (stays.isEmpty()) {
                System.out.println("Der er ingen ophold.");
                return;
            }

            for (var stay : stays) {
                System.out.println(stay);
            }
        });
    }
}

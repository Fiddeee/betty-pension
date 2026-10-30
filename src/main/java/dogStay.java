/* TODO
- Lav funktion til at hente data fra tblOphold
- Skal hentes som seperate enheder



 */

public class dogStay {
    /**
     * Udskriver alle ophold fra databasen.
     */
    public static void printAllStays() {
        dbConnector.getJdbi().useHandle(handle -> {
            var stays = handle.createQuery("""
                SELECT FLDOpholdID, FLDHundID, FLDAnkomstDato,
                       FLDForventningAfrejse, FLDFaktiskAfrejse
                FROM tblOphold
                ORDER BY FLDOpholdID
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

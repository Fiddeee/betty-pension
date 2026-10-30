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

            System.out.printf("%-8s %-30s %-30s %-30s %-30s%n", "OpholdID", "HundID", "Ankomstdato", "Forventet afrejse", "Faktisk afrejse");
            System.out.println("----------------------------------------");
            for (var stay : stays) {
                System.out.printf(
                        "%-8s %-30s %-30s %-30s %-30s%n",
                        stay.get("fldopholdid"),
                        stay.get("fldhundid"),
                        stay.get("fldankomstdato"),
                        stay.get("fldforventetafrejse"),
                        stay.get("fldfaktiskafrejse")
                );
            }
        });
    }
}

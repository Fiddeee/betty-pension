/**
 * Udskriver et opholds oplysninger, måltider og gåture fra databasen.
 */
public class EventLog {
    public static int opholdID;
    /**
     * Henter og udskriver hændelsesloggen for ét ophold i konsollen.
     *
     */
    public static void print(int opholdID) {

        if (opholdID <= 0) {
            System.out.println("Opholdets ID skal være større end 0.");
            return;
        }

        dbConnector.getJdbi().useHandle(handle -> {
            var stays = handle.createQuery("""
                    SELECT fldOpholdID, fldHundID,
                           fldAnkomstDato,
                           fldForventningAfrejse,
                           fldFaktiskAfrejse
                    FROM tblOphold
                    WHERE fldOpholdID = :opholdID
                    """)
                    .bind("opholdID", opholdID)
                    .mapToMap()
                    .list();

            if (stays.isEmpty()) {
                System.out.println("Der findes ikke et ophold med ID " + opholdID + ".");
                return;
            }

            /**
            Hent måltider fra tblMaaltid
            Hent fodertype navnet, ud fra at ttblMaaltid.fodertypeId blvier søgt i tblFoodType.fldFoodTypeID

             */
            var meals = handle.createQuery("""
                    SELECT tblMaaltid.tidspunkt, tblMaaltid.maengde,
                           tblMaaltid.fodertypeId, tblFoodType.fldFoodtypeNavn
                    FROM tblMaaltid
                    LEFT JOIN tblFoodType
                        ON tblMaaltid.fodertypeId = tblFoodType.fldFoodtypeID
                    WHERE tblMaaltid.opholdId = :opholdID
                    ORDER BY tblMaaltid.tidspunkt
                    """)
                    .bind("opholdID", opholdID)
                    .mapToMap()
                    .list();

            var walks = handle.createQuery("""
                    SELECT fldTidspunkt, fldDistance,
                           fldVarighed
                    FROM tblGaatur
                    WHERE fldOpholdID = :opholdID
                    ORDER BY fldTidspunkt, fldVarighed, fldDistance
                    """)
                    .bind("opholdID", opholdID)
                    .mapToMap()
                    .list();

            var stay = stays.get(0); // Henter informationer om opholdet fra indeks 0 - Det første resultat der kommer.
            System.out.println("========== HÆNDELSESLOG ==========");
            System.out.println("Ophold-ID: " + stay.get("fldopholdid"));
            System.out.println("Hund-ID: " + stay.get("fldhundid"));
            System.out.println("Ankomst: " + stay.get("fldankomstdato"));
            System.out.println("Forventet afrejse: " + stay.get("fldforventningafrejse"));
            if(stay.get("fldfaktiskafrejse") == null){
                System.out.println("Faktisk afrejse: Ikke bestemt endnu.");
            }
            else{
                System.out.println("Faktisk afrejse:" + stay.get("fldfaktiskafrejse"));
            }


            System.out.println("MÅLTIDER");
            if (meals.isEmpty()) {
                System.out.println("Ingen måltider registreret.");
            } else {
                System.out.printf("%-12s %-30s %s%n", "Tidspunkt", "Fodertype", "Mængde (gram)");
                for (var meal : meals) {
                    System.out.printf("%-12s %-30s %s%n",
                            meal.get("tidspunkt"),
                            meal.get("fodertype"),
                            meal.get("maengde"));
                }
            }

            System.out.println("GÅTURE");
            if (walks.isEmpty()) {
                System.out.println("Ingen gåture registreret.");
            } else {
                System.out.printf("%-12s %-16s %s%n", "Tidspunkt", "Distance (km)", "Varighed (tt:mm:ss)");
                for (var walk : walks) {
                    System.out.printf("%-12s %-16s %s%n",
                            walk.get("fldtidspunkt"), walk.get("flddistance"), walk.get("fldvarighed"));
                }
            }
            System.out.println("=================================\n");
        });
    }
}

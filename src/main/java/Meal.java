/**
 * Tilhører tblMaaltid
 *
 * Kan hente fodertyper fra tblFoodtype
 */

import java.time.LocalTime; //Bruges til tidspunktsformattering
import java.time.LocalDate; //Bruges til dagens dato
import java.time.LocalDateTime; //Samler det, så databasen kan bruge det i et samlet felt, istedet for 2 felter, et til LocalTime, og et til LocalDate
import java.time.format.DateTimeFormatter;


public class Meal {
    static int weightInGrams = 0; //Fodervægt i gram
    static String clock = ""; //Klokkeslæt
    static int foderTypeID; //FodertypeID - tjekkes fra tblFoodtype
    static int opholdId;



        /**
         * Udskriver alle fodertyper fra tblFoodType
         */
        public static void getFoodTypes() {
            dbConnector.getJdbi().useHandle(handle -> {
                var foodTypes = handle.createQuery("""
                SELECT fldFoodtypeID, fldFoodtypeNavn
                FROM tblFoodType
                ORDER BY fldFoodtypeID
                """)
                        .mapToMap()
                        .list();

                if (foodTypes.isEmpty()) {
                    System.out.println("Der er ingen fodertyper.");
                    return;
                }

                System.out.printf("%-8s %-30s%n", "ID", "Fodertype");
                System.out.println("----------------------------------------");

                for (var foodType : foodTypes) {
                    System.out.printf(
                            "%-8s %-30s%n",
                            foodType.get("fldfoodtypeid"),
                            foodType.get("fldfoodtypenavn")
                    );
                }
            });
        }


    /**
     * Opretter et måltid på et eksisterende ophold.
     */
    public static void createMeal(
            int opholdId, int fodertypeId,
            String clock, int weightInGrams) {

        LocalDateTime tidspunkt = LocalDate.now().atTime(LocalTime.parse(clock));
        String formateretTidspunkt = tidspunkt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        dbConnector.getJdbi().useHandle(handle -> {
            handle.createUpdate("""
                            INSERT INTO tblMaaltid
                                (fldOpholdID, fldFodertypeID, fldTidspunkt, fldMaengde)
                            VALUES
                                (:opholdId, :fodertypeId, :clock, :weight)
                            """)
                    .bind("opholdId", opholdId)
                    .bind("fodertypeId", fodertypeId)
                    .bind("clock", formateretTidspunkt)
                    .bind("weight", weightInGrams)
                    .execute();
        });

        System.out.println("Måltidet er nu oprettet kl. " + tidspunkt);
    }
}

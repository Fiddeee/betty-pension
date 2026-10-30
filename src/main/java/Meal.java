/** TODO
Opret måltid og send til Database.

int weight - Foderets vægt
? time - Tidspunkt for fodring
Enum foodType - Fodertype - Skal hentes fra tblFoodType

 */
import java.time.LocalTime;



public class Meal {
    int weightInGrams = 0; //Weight in grams of food
    static String clock = "";
    int foderTypeID;



        /**
         * Udskriver alle ophold fra databasen.
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
     * Gemmer et måltid på et eksisterende ophold.
     */
    public static void createMeal(
            int opholdId, int fodertypeId,
            String clock, int weightInGrams) {

        LocalTime tidspunkt = LocalTime.parse(clock); //Formateres via ex. 12:30

        dbConnector.getJdbi().useHandle(handle -> {
            handle.createUpdate("""
                            INSERT INTO maaltid
                                (opholdId, fodertypeId, tidspunkt, maengde)
                            VALUES
                                (:opholdId, :fodertypeId, :clock, :weight)
                            """)
                    .bind("opholdId", opholdId)
                    .bind("fodertypeId", fodertypeId)
                    .bind("clock", tidspunkt)
                    .bind("weight", weightInGrams)
                    .execute();
        });

        System.out.println("Måltidet er nu oprettet kl. " + tidspunkt);
    }
}

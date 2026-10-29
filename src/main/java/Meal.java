/** TODO
Opret måltid og send til Database.

int weight - Foderets vægt
? time - Tidspunkt for fodring
Enum foodType - Fodertype - Skal hentes fra tblFoodType

 */
import java.time.LocalTime;


import static java.rmi.Naming.bind;

public class Meal {
    int weightInGrams = 0; //Weight in grams of food
    static String clock = "";
    int foderTypeID;


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

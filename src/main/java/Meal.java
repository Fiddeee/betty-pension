/* TODO
Opret måltid og send til Database.

int weight - Foderets vægt
? time - Tidspunkt for fodring
Enum foodType - Fodertype - Skal hentes fra tblFoodType

 */

import java.sql.Time;
import java.sql.Timestamp;

import static java.rmi.Naming.bind;

public class Meal {
    int weightInGrams = 0; //Weight in grams of food
    static String clock = "";
    int foderTypeID;


    
    public static Object Timestamp(String hour, String minute) {
        clock = hour + ":" + minute;
        System.out.println(clock);
        return clock;
    }



    /**
     * Gemmer et måltid på et eksisterende ophold.
     */
    public static void createMeal(int opholdId, int fodertypeId, String clock, int weightInGrams) {

        dbConnector.getJdbi().useHandle(handle -> {
            handle.createUpdate("""
                INSERT INTO maaltid
                    (opholdId, fodertypeId, tidspunkt, maengde)
                VALUES
                    (:opholdId, :fodertypeId, :clock, :weight)
                """)
                    .bind("opholdId", opholdId)
                    .bind("fodertypeId", fodertypeId)
                    .bind("clock", clock)
                    .bind("weight", weightInGrams)
                    .execute();
        });
        System.out.println("Måltidet er nu oprettet.");
        System.out.println("Ophold ID:" + opholdId);
        System.out.println("Fodertype ID:" + fodertypeId);
        System.out.println("Clock:" + clock);
        System.out.println("Weight (In grams):" + weightInGrams);
    }

}

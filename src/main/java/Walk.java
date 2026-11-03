/**
 * Tilhører tblGaatur
 *
 * Opretter gåturer
 */

import java.time.LocalDate; //Sætter dato for gåturen
import java.time.LocalDateTime; //Samler LocalDate og LocalTime så det kan indsættes i 1 felt sammen.
import java.time.LocalTime; //Indeholder tid
import java.time.format.DateTimeFormatter; //Bruges til formattering, så vi kan fjerne dato fra Varigheden, så det indsættes korrekt i databasen.


public class Walk {
    static int opholdID;
    static double Distance; // Distance walked in km
    static String stringVarighed; //Used for time
    static String stringTidspunkt;


    /**
     *
     * @param opholdID
     * @param Distance
     * @param stringVarighed
     * @param stringTidspunkt
     *
     * Opretter en gåtur i tblGaatur. Formaterer Tidspunkt i dd/MM/yyyy - HH:mm:ss Så det står i dansk formattering
     * Varighed i HH:mm:ss
     */
    public static void createWalk(int opholdID, double Distance, String stringVarighed, String stringTidspunkt){
        String Varighed = LocalTime.parse(stringVarighed).format(DateTimeFormatter.ofPattern("HH:mm:ss"));


        LocalDateTime Tidspunkt = LocalDate.now().atTime(LocalTime.parse(stringTidspunkt)); //Formateres via ex. 12:30
        String formateretTid = Tidspunkt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        dbConnector.getJdbi().useHandle(handle -> {
            handle.createUpdate("""
                            INSERT INTO tblGaatur
                                (fldOpholdID, fldDistance, fldVarighed, fldTidspunkt)
                            VALUES
                                (:fldOpholdID, :fldDistance, :fldVarighed, :fldTidspunkt)
                            """)
                    .bind("fldOpholdID", opholdID)
                    .bind("fldDistance", Distance)
                    .bind("fldVarighed", Varighed)
                    .bind("fldTidspunkt", formateretTid)
                    .execute();
        });
        System.out.println("===");
        System.out.println("En gåtur er oprettet med følgende oplysninger");
        System.out.println(" ");
        System.out.println("Ophold ID : "+opholdID);
        System.out.println("Distance : "+Distance);
        System.out.println("Varighed : "+Varighed);
        System.out.println("Tidspunkt : "+formateretTid);
        System.out.println(" ");
    }
}

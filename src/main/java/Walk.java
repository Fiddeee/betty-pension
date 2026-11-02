/* TODO
Lav funktion til oprettelse af gåtur.

Skal indeholde

double Distance - Distancen der er gået
? walkTime - Tiden det har taget at gå
 */

import java.time.LocalTime;

public class Walk {
    static int opholdID;
    static double Distance; // Distance walked in km
    static String stringVarighed; //Used for time
    static String stringTidspunkt;



    public static void createWalk(int opholdID, double Distance, String stringVarighed, String stringTidspunkt){
        LocalTime Varighed = LocalTime.parse(stringVarighed);
        LocalTime Tidspunkt = LocalTime.parse(stringTidspunkt);

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
                    .bind("fldTidspunkt", Tidspunkt)
                    .execute();
        });
        System.out.println("===");
        System.out.println("En gåtur er oprettet med følgende oplysninger");
        System.out.println(" ");
        System.out.println("Ophold ID : "+opholdID);
        System.out.println("Distance : "+Distance);
        System.out.println("Varighed : "+Varighed);
        System.out.println("Tidspunkt : "+Tidspunkt);
        System.out.println(" ");
    }
}

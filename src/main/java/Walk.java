/* TODO
Lav funktion til oprettelse af gåtur.

Skal indeholde

double Distance - Distancen der er gået
? walkTime - Tiden det har taget at gå
 */

import java.time.LocalTime;

public class Walk {
    double Distance; // Distance walked in km
    String stringVarighed; //Used for time
    String stringTidspunkt;



    public void createWalk(int opholdID, double Distance, String stringVarighed, String stringTidspunkt){
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

    }
}

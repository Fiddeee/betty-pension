/* TODO
Lav funktion til oprettelse af gåtur.

Skal indeholde

double Distance - Distancen der er gået
? walkTime - Tiden det har taget at gå
 */

import java.time.LocalTime;

public class Walk {
    double Distance; // Distance walked in km
    String timeUsed; //Used for time



    public void createWalk(int opholdID, double Distance, String timeUsed){
        LocalTime walkTime = LocalTime.parse(timeUsed);

        dbConnector.getJdbi().useHandle(handle -> {
            handle.createUpdate("""
                            INSERT INTO walk
                                (opholdId, Distance, timeUsed)
                            VALUES
                                (:opholdId, :Distance, :timeUsed)
                            """)
                    .bind("opholdId", opholdID)
                    .bind("Distance", Distance)
                    .bind("timeUsed", timeUsed)
                    .execute();
        });

    }
}

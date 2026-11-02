import java.util.Locale; //Bruges til at systemet kan tage højde for at modtage både "komma" og "punktum" i Kilometer
import java.util.Scanner; //Bruges til at modtage user input

/**
 * Indeholder alt brugeren møder i konsollen.
 *
 * Filen indeholder metoder der kan bruges til at kontrollerer diverse input der modtages, istedet for
 * at skulle lave en kontrol ved hvert input, er der oprettet forskellige metoder som kan genbruges
 * -readInt
 * -readStayId
 * -foodTypeExists
 * -readDistance
 * -readTime
 * -readAgain
 *
 *
 */

/** Viser menuer og kontrollerer brugerens input. */
public class UX {
    private static Scanner userNav = new Scanner(System.in);

    /** Viser hovedmenuen, indtil brugeren vælger 0. */
    public static void adminMenu() throws InterruptedException {
        int choice = -1;

        while (choice != 0) {
            System.out.println("Betty Pension - Administration");
            System.out.println("1. Vis alle ophold");
            System.out.println("2. Tilføj måltid");
            System.out.println("3. Tilføj gåtur");
            System.out.println("4. Vis hændelseslog");
            System.out.println("0. Afslut");
            choice = readInt("Indtast et nummer:", 0, 4);

            if (choice == 1) {
                printStayList();
            } else if (choice == 2) {
                addMeal();
            } else if (choice == 3) {
                addWalk();
            } else if (choice == 4) {
                printEventlog();
            }
        }
    }

    /** Viser alle ophold og venter på Enter. */
    public static void printStayList() throws InterruptedException {
        dogStay.printAllStays();
        System.out.println("Tryk Enter for at komme tilbage til menuen.");
        userNav.nextLine();
    }

    /** Spørger efter oplysninger og opretter et måltid. */
    public static void addMeal() throws InterruptedException {
        int again = 1;
        while (again == 1) {
            int opholdId = readStayId();
            if (opholdId == 0) {
                return;
            }

            Meal.getFoodTypes();
            int foodTypeId = readInt("Indtast fodertype-ID (0 = tilbage):", 0, Integer.MAX_VALUE);
            while (foodTypeId != 0 && !foodTypeExists(foodTypeId)) {
                System.out.println("Fodertypen findes ikke. Vælg et ID fra listen.");
                foodTypeId = readInt("Indtast fodertype-ID (0 = tilbage):", 0, Integer.MAX_VALUE);
            }
            if (foodTypeId == 0) {
                return;
            }

            String clock = readTime("Hvornår skete fodringen? Fx 12:30 eller 12:30:30.", false);
            int weight = readInt("Mængde foder i hele gram (større end 0):", 1, Integer.MAX_VALUE);
            Meal.createMeal(opholdId, foodTypeId, clock, weight);
            again = readAgain("Vil du tilføje flere måltider?");
        }
    }

    /** Spørger efter oplysninger og opretter en gåtur. */
    public static void addWalk() throws InterruptedException {
        int again = 1;
        while (again == 1) {
            int opholdId = readStayId();
            if (opholdId == 0) {
                return;
            }
            double distance = readDistance();
            String duration = readTime("Hvor længe varede turen? Fx 01:03:22 (under 24 timer).", true);
            String start = readTime("Hvornår startede turen? Fx 12:23 eller 12:23:00.", false);
            Walk.createWalk(opholdId, distance, duration, start);
            again = readAgain("Vil du tilføje flere gåture?");
        }
    }

    /** Viser hændelsesloggen for et eksisterende ophold. */
    public static void printEventlog() throws InterruptedException {
        int again = 1;
        while (again == 1) {
            int opholdId = readStayId();
            if (opholdId == 0) {
                return;
            }
            EventLog.print(opholdId);
            again = readAgain("Vil du vise flere hændelseslogs?");
        }
    }

    /** Læser et heltal mellem minimum og maksimum. */
    private static int readInt(String question, int minimum, int maximum) {
        while (true) {
            System.out.println(question);
            String input = userNav.nextLine();

            // Denne Scanner undersøger kun den indtastede linje.
            Scanner inputCheck = new Scanner(input);
            if (inputCheck.hasNextInt()) {
                int number = inputCheck.nextInt();
                boolean extraInput = inputCheck.hasNext();
                inputCheck.close();

                if (!extraInput && number >= minimum && number <= maximum) {
                    return number;
                }
                System.out.println("Skriv kun ét heltal fra " + minimum + " til " + maximum + ".");
            } else {
                inputCheck.close();
                System.out.println("Forkert input. Du skal skrive et heltal.");
            }
        }
    }

    /** Læser et opholds-ID og kontrollerer, at opholdet findes. */
    private static int readStayId() {
        while (true) {
            int opholdId = readInt("Indtast opholds-ID (0 = tilbage):", 0, Integer.MAX_VALUE);
            if (opholdId == 0) {
                return 0;
            }
            boolean exists = dbConnector.getJdbi().withHandle(handle -> {
                var stays = handle.createQuery("SELECT fldOpholdID FROM tblOphold WHERE fldOpholdID = :id")
                        .bind("id", opholdId)
                        .mapToMap()
                        .list();
                return !stays.isEmpty();
            });
            if (exists) {
                return opholdId;
            }
            System.out.println("Opholdet findes ikke. Prøv igen.");
        }
    }

    /** Undersøger, om fodertypen findes. */
    private static boolean foodTypeExists(int foodTypeId) {
        return dbConnector.getJdbi().withHandle(handle -> {
            var foodTypes = handle.createQuery("SELECT fldFoodtypeID FROM tblFoodType WHERE fldFoodtypeID = :id")
                    .bind("id", foodTypeId)
                    .mapToMap()
                    .list();
            return !foodTypes.isEmpty();
        });
    }

    /** Læser en positiv distance med komma eller punktum som decimaltegn. */
    private static double readDistance() {
        while (true) {
            System.out.println("Distance i kilometer, fx 1,5 eller 1.5:");
            String input = userNav.nextLine().trim().replace(',', '.');
            Scanner inputCheck = new Scanner(input);
            inputCheck.useLocale(Locale.US); // Læser tal med punktum som decimaltegn, efter at komma er erstattet med punktum.

            if (inputCheck.hasNextDouble()) {
                double distance = inputCheck.nextDouble();
                boolean extraInput = inputCheck.hasNext();
                inputCheck.close();
                if (!extraInput && distance > 0 && distance <= Double.MAX_VALUE) {
                    return distance;
                }
            } else {
                inputCheck.close();
            }
            System.out.println("Forkert input. Skriv et tal større end 0 uden enhed, fx 1,5.");
        }
    }

    /**
     * Læser tid som tt:mm eller tt:mm:ss.
     * En varighed skal være større end nul og under 24 timer, da Walk bruger LocalTime.
     */
    private static String readTime(String question, boolean duration) {
        while (true) {
            System.out.println(question);
            String input = userNav.nextLine().trim();
            boolean formatCorrect = false;

            if (input.length() == 5 || input.length() == 8) {
                formatCorrect = true;
                for (int i = 0; i < input.length(); i++) {
                    if (i == 2 || i == 5) {
                        if (input.charAt(i) != ':') {
                            formatCorrect = false;
                        }
                    } else if (input.charAt(i) < '0' || input.charAt(i) > '9') {
                        formatCorrect = false;
                    }
                }
            }

            // Først efter formatkontrollen er det sikkert at læse tallene.
            if (formatCorrect) {
                int hours = Integer.parseInt(input.substring(0, 2));
                int minutes = Integer.parseInt(input.substring(3, 5));
                int seconds = 0;
                if (input.length() == 8) {
                    seconds = Integer.parseInt(input.substring(6, 8));
                }
                if (hours < 24 && minutes < 60 && seconds < 60) {
                    if (!duration || hours > 0 || minutes > 0 || seconds > 0) {
                        return input;
                    }
                }
            }
            System.out.println("Forkert input. Brug tt:mm eller tt:mm:ss. Timer: 00-23, minutter/sekunder: 00-59.");
            if (duration) {
                System.out.println("Varigheden skal også være større end nul.");
            }
        }
    }

    /** Læser 1 for ja eller 2 for at vende tilbage til menuen. */
    private static int readAgain(String question) {
        System.out.println(question);
        System.out.println("1. Ja");
        System.out.println("2. Nej, tilbage til menuen");
        return readInt("Indtast 1 eller 2:", 1, 2);
    }
}

import org.jdbi.v3.core.Jdbi;

/**
 * Samler databaseopsætningen og giver adgang til et fælles Jdbi-objekt.
 */
public class dbConnector {

    private static final Jdbi jdbi = Jdbi.create(
            "jdbc:sqlserver://localhost:1433;" +
                    "databaseName=dbAdgangTilDatabase;" + // Ret databaseName til det navn du har givet databasen I SQL Server
                    "encrypt=true;" +
                    "trustServerCertificate=true",
            "frede_java", //Brugernavn  vi opretter I SQL
            "Test1234!" // Password vi opretter I SQL
    );

    /**
     * Returnerer projektets fælles Jdbi-objekt.
     *
     *
     */
    public static Jdbi getJdbi() {
        return jdbi;
    }
}

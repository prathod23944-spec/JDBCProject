import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DBConnection {

    private static final Logger LOGGER =
            Logger.getLogger(DBConnection.class.getName());

    private static final String URL =
            System.getenv().getOrDefault(
                    "DB_URL",
                    "jdbc:mysql://localhost:3306/college"
            );

    private static final String USERNAME =
            System.getenv().getOrDefault("DB_USER", "root");

    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    private static final HikariDataSource DATA_SOURCE =
            createDataSource();

    private DBConnection() {
    }

    private static HikariDataSource createDataSource() {
        if (PASSWORD == null || PASSWORD.trim().isEmpty()) {
            throw new IllegalStateException(
                    "DB_PASSWORD environment variable is not set."
            );
        }

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setPoolName("StudentManagementPool");

        LOGGER.info("Initializing database connection pool.");

        return new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DATA_SOURCE.getConnection();
            LOGGER.fine("Connection acquired from pool.");
            return connection;
        } catch (SQLException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Failed to acquire database connection.",
                    e
            );
            throw e;
        }
    }

    public static void closePool() {
        if (!DATA_SOURCE.isClosed()) {
            DATA_SOURCE.close();
            LOGGER.info("Database connection pool closed.");
        }
    }
}


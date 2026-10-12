import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class UpdateStudent {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/college";
        String username = "root";
        String password = "Rathod@97";

        try {

            // Database Connection
            Connection con = DriverManager.getConnection(
                    url, username, password
            );

            System.out.println("Database Connected");

            // Create Statement
            Statement stmt = con.createStatement();

            // UPDATE Query
            String sql = "UPDATE students " +
                    "SET course = 'MCA' " +
                    "WHERE id = 1";

            // Execute Update
            int rows = stmt.executeUpdate(sql);

            System.out.println(rows + " Student Updated Successfully");

            // Close Connection
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
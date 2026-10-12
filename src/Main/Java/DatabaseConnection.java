package Main.Java;
import java.sql.Connection;
import java.sql.DriverManager;


public class Main {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/college";
        String username = "root";
        String password = "Rathod@97";

        try {
            Connection con =
                    DriverManager.getConnection(
                            url, username, password
                    );

            System.out.println("Database Connected");

            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

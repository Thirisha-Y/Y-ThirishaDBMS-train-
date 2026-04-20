package train;
import java.sql.*;
import java.util.Scanner;

public class Train {

    static final String DB_URL = "jdbc:mysql://localhost:3306/project?useSSL=false&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "dbms";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

            while (true) {
                System.out.println("\n===== TRAIN RESERVATION SYSTEM =====");
                System.out.println("1. View Trains");
                System.out.println("2. View Passengers");
                System.out.println("3. View Reservations");
                System.out.println("4. Add Train");
                System.out.println("5. Update Ticket Price");
                System.out.println("6. Delete Train");
                System.out.println("7. Exit");
                System.out.print("Enter choice: ");

                int choice = sc.nextInt();

                switch (choice) {

                    case 1:
                        viewTrains(conn);
                        break;

                    case 2:
                        viewPassengers(conn);
                        break;

                    case 3:
                        viewReservations(conn);
                        break;

                    case 4:
                        addTrain(conn, sc);
                        break;

                    case 5:
                        updateTrain(conn, sc);
                        break;

                    case 6:
                        deleteTrain(conn, sc);
                        break;

                    case 7:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // VIEW TRAINS
    static void viewTrains(Connection conn) throws SQLException {
        String sql = "SELECT * FROM train";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\n--- Train List ---");
        while (rs.next()) {
            System.out.println(rs.getString("train_id") + " | "
                    + rs.getString("train_name") + " | "
                    + rs.getString("source") + " | "
                    + rs.getString("destination") + " | "
                    + rs.getInt("price"));
        }
    }

    // VIEW PASSENGERS
    static void viewPassengers(Connection conn) throws SQLException {
        String sql = "SELECT * FROM passenger";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\n--- Passenger List ---");
        while (rs.next()) {
            System.out.println(rs.getString("passenger_id") + " | "
                    + rs.getString("name") + " | "
                    + rs.getString("contact"));
        }
    }

    // VIEW RESERVATIONS
    static void viewReservations(Connection conn) throws SQLException {
        String sql = "SELECT * FROM reservation";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\n--- Reservation List ---");
        while (rs.next()) {
            System.out.println(rs.getString("reservation_id") + " | "
                    + rs.getString("train_id") + " | "
                    + rs.getString("passenger_id") + " | "
                    + rs.getDate("travel_date"));
        }
    }

    // ADD TRAIN
    static void addTrain(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Train ID: ");
        String id = sc.next();

        System.out.print("Enter Train Name: ");
        String name = sc.next();

        System.out.print("Enter Source: ");
        String source = sc.next();

        System.out.print("Enter Destination: ");
        String destination = sc.next();

        System.out.print("Enter Ticket Price: ");
        int price = sc.nextInt();

        String sql = "INSERT INTO train VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, id);
        ps.setString(2, name);
        ps.setString(3, source);
        ps.setString(4, destination);
        ps.setInt(5, price);

        ps.executeUpdate();
        System.out.println("Train added successfully!");
    }

    // UPDATE TRAIN PRICE
    static void updateTrain(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Train ID: ");
        String id = sc.next();

        System.out.print("Enter New Price: ");
        int price = sc.nextInt();

        String sql = "UPDATE train SET price=? WHERE train_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, price);
        ps.setString(2, id);

        int rows = ps.executeUpdate();
        System.out.println(rows + " row(s) updated.");
    }

    // DELETE TRAIN
    static void deleteTrain(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Train ID: ");
        String id = sc.next();

        String sql = "DELETE FROM train WHERE train_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, id);

        int rows = ps.executeUpdate();
        System.out.println(rows + " row(s) deleted.");
    }
}

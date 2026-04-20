package trainreservationgui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class TrainReservationGUI {

    static final String DB_URL = "jdbc:mysql://localhost:3306/train_reservation?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "dbms";

    JFrame frame;

    public TrainReservationGUI() {
        frame = new JFrame("Train Reservation Management System");
        frame.setSize(900, 500);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Trains", trainPanel());
        tabs.add("Passengers", passengerPanel());
        tabs.add("Reservations", reservationPanel());

        frame.add(tabs);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ================= TRAINS =================
    JPanel trainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Train ID", "Train Name", "Source", "Destination", "Price"}, 0);
        JTable table = new JTable(model);

        JPanel btn = new JPanel();
        JButton load = new JButton("Load");
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");

        btn.add(load); btn.add(add); btn.add(update); btn.add(delete);

        // LOAD
        load.addActionListener(e -> {
            model.setRowCount(0);
            try (Connection c = DriverManager.getConnection(DB_URL, USER, PASS);
                 Statement s = c.createStatement();
                 ResultSet rs = s.executeQuery("SELECT * FROM train")) {

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("train_id"),
                            rs.getString("train_name"),
                            rs.getString("source"),
                            rs.getString("destination"),
                            rs.getInt("price")
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        // ADD
        add.addActionListener(e -> {
            try (Connection c = DriverManager.getConnection(DB_URL, USER, PASS)) {

                String id = JOptionPane.showInputDialog("Train ID");
                String name = JOptionPane.showInputDialog("Train Name");
                String source = JOptionPane.showInputDialog("Source");
                String dest = JOptionPane.showInputDialog("Destination");
                int price = Integer.parseInt(JOptionPane.showInputDialog("Price"));

                PreparedStatement ps = c.prepareStatement("INSERT INTO train VALUES (?, ?, ?, ?, ?)");
                ps.setString(1, id);
                ps.setString(2, name);
                ps.setString(3, source);
                ps.setString(4, dest);
                ps.setInt(5, price);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Train Added!");
                load.doClick();

            } catch (SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(frame, "Train ID already exists!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        // UPDATE (FULL + PARTIAL)
        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(frame, "Select a row!");
                return;
            }

            String id = model.getValueAt(r, 0).toString();

            try (Connection c = DriverManager.getConnection(DB_URL, USER, PASS)) {

                String[] options = {"Price Only", "Full Row"};
                int choice = JOptionPane.showOptionDialog(frame, "Choose Update Type",
                        "Update", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, options, options[0]);

                if (choice == 0) {
                    int price = Integer.parseInt(JOptionPane.showInputDialog("New Price"));

                    PreparedStatement ps = c.prepareStatement(
                            "UPDATE train SET price=? WHERE train_id=?");
                    ps.setInt(1, price);
                    ps.setString(2, id);
                    ps.executeUpdate();

                } else {
                    String name = JOptionPane.showInputDialog("Train Name", model.getValueAt(r, 1));
                    String source = JOptionPane.showInputDialog("Source", model.getValueAt(r, 2));
                    String dest = JOptionPane.showInputDialog("Destination", model.getValueAt(r, 3));
                    int price = Integer.parseInt(JOptionPane.showInputDialog("Price", model.getValueAt(r, 4)));

                    PreparedStatement ps = c.prepareStatement(
                            "UPDATE train SET train_name=?, source=?, destination=?, price=? WHERE train_id=?");

                    ps.setString(1, name);
                    ps.setString(2, source);
                    ps.setString(3, dest);
                    ps.setInt(4, price);
                    ps.setString(5, id);
                    ps.executeUpdate();
                }

                JOptionPane.showMessageDialog(frame, "Updated!");
                load.doClick();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        // DELETE
        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) return;

            String id = model.getValueAt(r, 0).toString();

            try (Connection c = DriverManager.getConnection(DB_URL, USER, PASS)) {
                PreparedStatement ps = c.prepareStatement("DELETE FROM train WHERE train_id=?");
                ps.setString(1, id);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Deleted!");
                load.doClick();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btn, BorderLayout.SOUTH);
        return panel;
    }

    // ================= PASSENGERS =================
    JPanel passengerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Passenger ID", "Name", "Contact"}, 0);
        JTable table = new JTable(model);

        JPanel btn = new JPanel();
        JButton load = new JButton("Load");
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");

        btn.add(load); btn.add(add); btn.add(update); btn.add(delete);

        load.addActionListener(e -> {
            model.setRowCount(0);
            try (Connection c = DriverManager.getConnection(DB_URL, USER, PASS);
                 Statement s = c.createStatement();
                 ResultSet rs = s.executeQuery("SELECT * FROM passenger")) {

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("passenger_id"),
                            rs.getString("name"),
                            rs.getString("contact")
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        add.addActionListener(e -> {
            try (Connection c = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String id = JOptionPane.showInputDialog("Passenger ID");
                String name = JOptionPane.showInputDialog("Name");
                String contact = JOptionPane.showInputDialog("Contact");

                PreparedStatement ps = c.prepareStatement("INSERT INTO passenger VALUES (?, ?, ?)");
                ps.setString(1, id);
                ps.setString(2, name);
                ps.setString(3, contact);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Passenger Added!");
                load.doClick();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) return;

            String id = model.getValueAt(r, 0).toString();

            try (Connection c = DriverManager.getConnection(DB_URL, USER, PASS)) {

                String[] options = {"Contact Only", "Full Row"};
                int choice = JOptionPane.showOptionDialog(frame, "Update Type",
                        "Update", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, options, options[0]);

                if (choice == 0) {
                    String contact = JOptionPane.showInputDialog("New Contact");

                    PreparedStatement ps = c.prepareStatement(
                            "UPDATE passenger SET contact=? WHERE passenger_id=?");
                    ps.setString(1, contact);
                    ps.setString(2, id);
                    ps.executeUpdate();

                } else {
                    String name = JOptionPane.showInputDialog("Name", model.getValueAt(r, 1));
                    String contact = JOptionPane.showInputDialog("Contact", model.getValueAt(r, 2));

                    PreparedStatement ps = c.prepareStatement(
                            "UPDATE passenger SET name=?, contact=? WHERE passenger_id=?");
                    ps.setString(1, name);
                    ps.setString(2, contact);
                    ps.setString(3, id);
                    ps.executeUpdate();
                }

                JOptionPane.showMessageDialog(frame, "Updated!");
                load.doClick();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) return;

            String id = model.getValueAt(r, 0).toString();

            try (Connection c = DriverManager.getConnection(DB_URL, USER, PASS)) {
                PreparedStatement ps = c.prepareStatement("DELETE FROM passenger WHERE passenger_id=?");
                ps.setString(1, id);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Deleted!");
                load.doClick();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btn, BorderLayout.SOUTH);
        return panel;
    }

    // ================= RESERVATIONS =================
    JPanel reservationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Reservation ID", "Train ID", "Passenger ID", "Travel Date"}, 0);
        JTable table = new JTable(model);

        JPanel btn = new JPanel();
        JButton load = new JButton("Load");
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");

        btn.add(load); btn.add(add); btn.add(update); btn.add(delete);

        load.addActionListener(e -> {
            model.setRowCount(0);
            try (Connection c = DriverManager.getConnection(DB_URL, USER, PASS);
                 Statement s = c.createStatement();
                 ResultSet rs = s.executeQuery("SELECT * FROM reservation")) {

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("reservation_id"),
                            rs.getString("train_id"),
                            rs.getString("passenger_id"),
                            rs.getDate("travel_date")
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        add.addActionListener(e -> {
            try (Connection c = DriverManager.getConnection(DB_URL, USER, PASS)) {

                String id = JOptionPane.showInputDialog("Reservation ID");
                String trainId = JOptionPane.showInputDialog("Train ID");
                String passengerId = JOptionPane.showInputDialog("Passenger ID");
                String date = JOptionPane.showInputDialog("Date (YYYY-MM-DD)");

                PreparedStatement ps = c.prepareStatement("INSERT INTO reservation VALUES (?, ?, ?, ?)");
                ps.setString(1, id);
                ps.setString(2, trainId);
                ps.setString(3, passengerId);
                ps.setDate(4, Date.valueOf(date));
                ps.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Reservation Added!");
                load.doClick();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) return;

            String id = model.getValueAt(r, 0).toString();

            try (Connection c = DriverManager.getConnection(DB_URL, USER, PASS)) {

                String[] options = {"Date Only", "Full Row"};
                int choice = JOptionPane.showOptionDialog(frame, "Update Type",
                        "Update", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, options, options[0]);

                if (choice == 0) {
                    String date = JOptionPane.showInputDialog("New Date");
                    PreparedStatement ps = c.prepareStatement(
                            "UPDATE reservation SET travel_date=? WHERE reservation_id=?");
                    ps.setDate(1, Date.valueOf(date));
                    ps.setString(2, id);
                    ps.executeUpdate();

                } else {
                    String trainId = JOptionPane.showInputDialog("Train ID", model.getValueAt(r, 1));
                    String passengerId = JOptionPane.showInputDialog("Passenger ID", model.getValueAt(r, 2));
                    String date = JOptionPane.showInputDialog("Date", model.getValueAt(r, 3));

                    PreparedStatement ps = c.prepareStatement(
                            "UPDATE reservation SET train_id=?, passenger_id=?, travel_date=? WHERE reservation_id=?");

                    ps.setString(1, trainId);
                    ps.setString(2, passengerId);
                    ps.setDate(3, Date.valueOf(date));
                    ps.setString(4, id);
                    ps.executeUpdate();
                }

                JOptionPane.showMessageDialog(frame, "Updated!");
                load.doClick();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) return;

            String id = model.getValueAt(r, 0).toString();

            try (Connection c = DriverManager.getConnection(DB_URL, USER, PASS)) {
                PreparedStatement ps = c.prepareStatement("DELETE FROM reservation WHERE reservation_id=?");
                ps.setString(1, id);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Deleted!");
                load.doClick();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btn, BorderLayout.SOUTH);
        return panel;
    }

    public static void main(String[] args) {
        new TrainReservationGUI();
    }
}


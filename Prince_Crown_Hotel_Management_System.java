import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/hotel_db";
        String password = "your_password";
        String username = "postgres";

        try {
            Connection connection = DriverManager.getConnection(
                    url,
                    username,
                    password
            );

            Scanner scanner = new Scanner(System.in);

            while (true) {

                System.out.println("======================Prince Crown Hotel===================");
                System.out.println();
                System.out.println("1. Reserve a room: ");
                System.out.println("2. View Reservation: ");
                System.out.println("3. Get Room Number: ");
                System.out.println("4. Update Reservation: ");
                System.out.println("5. Delete Reservation: ");
                System.out.println("0. Exit ");
                System.out.println();

                System.out.print("Enter Choice: ");
                int choice = scanner.nextInt();

                switch (choice) {

                    case 1:
                        reserveRoom(scanner, connection);
                        break;

                    case 2:
                        viewReservation(connection);
                        break;

                    case 3:
                        getRoomNumber(scanner, connection);
                        break;

                    case 4:
                        updateReservation(scanner, connection);
                        break;

                    case 5:
                        deleteReservation(scanner, connection);
                        break;

                    case 0:
                        exit();
                        scanner.close();
                        connection.close();
                        return;

                    default:
                        System.out.println("Invalid option input");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    // ================= RESERVE ROOM =================

    public static void reserveRoom(
            Scanner scanner,
            Connection connection
    ) throws SQLException {

        System.out.print("Enter guest name: ");
        String guestName = scanner.next();

        System.out.print("Enter room number: ");
        int roomNumber = scanner.nextInt();

        System.out.print("Enter Contact Number: ");
        String contactNumber = scanner.next();

        String sql = "INSERT INTO reservation_data " +
                "(guest_name, room_nu, contact_nu) " +
                "VALUES ('" + guestName + "', " +
                roomNumber + ", '" +
                contactNumber + "')";

        try (Statement statement = connection.createStatement()) {

            int affectedRows = statement.executeUpdate(sql);

            if (affectedRows > 0) {
                System.out.println("Reservation Successful.");
            } else {
                System.out.println("Reservation Failed.");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // ================= VIEW RESERVATION =================

    public static void viewReservation(Connection connection)
            throws SQLException {

        String sql = "SELECT reservation_id, guest_name, " +
                "room_nu, contact_nu, reservation_date " +
                "FROM reservation_data;";

        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            System.out.println("Current Reservation Details--");

            System.out.println(
                    "+---------------------------------+" + "---------------------------------+" + "---------------------------------+" + "---------------------------------+" + "---------------------------------+"
            );

            System.out.println(
                    "|           Reservation_id        " + "|             Guest_Name          " + "|             Room_number         " + "|            Contact_Number       " + "|         Reservation_date        |"
            );

            System.out.println(
                    "+---------------------------------+" + "---------------------------------+" + "---------------------------------+" + "---------------------------------+" + "---------------------------------+"
            );

            while (result.next()) {

                int reservation_id =
                        result.getInt("reservation_id");

                String guest_name =
                        result.getString("guest_name");

                int room_nu =
                        result.getInt("room_nu");

                String contact_nu =
                        result.getString("contact_nu");

                String reservation_date =
                        result.getTimestamp("reservation_date")
                                .toString();

                System.out.printf(
                        "| %-31d | %-31s | %-31d | %-31s | %-31s |\n",
                        reservation_id,
                        guest_name,
                        room_nu,
                        contact_nu,
                        reservation_date
                );

                System.out.println(
                        "+---------------------------------+" + "---------------------------------+" + "---------------------------------+" + "---------------------------------+" + "---------------------------------+"
                );
            }
        }
    }


    public static void getRoomNumber(
            Scanner scanner,
            Connection connection
    ) throws SQLException {

        try {

            System.out.print("Enter reservation id : ");
            int reservation_id = scanner.nextInt();

            System.out.print("Enter Guest_name : ");
            String guest_name = scanner.next();

            String sql = "SELECT room_nu FROM reservation_data " +
                    "WHERE reservation_id = " + reservation_id +
                    " AND guest_name = '" + guest_name + "'";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {

                    int roomNumber =
                            resultSet.getInt("room_nu");

                    System.out.println(
                            "Room number for Reservation ID "
                                    + reservation_id
                                    + " and Guest "
                                    + guest_name
                                    + " is: "
                                    + roomNumber
                    );

                } else {

                    System.out.println(
                            "Reservation not found for the given ID and guest name."
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void updateReservation(
            Scanner scanner,
            Connection connection
    ) throws SQLException {

        try {

            System.out.print("Enter reservation_id to update: ");
            int reservationId = scanner.nextInt();

            if (!reservationExists(connection, reservationId)) {

                System.out.println(
                        "Reservation not found for the given ID."
                );

                return;
            }

            System.out.print("Enter the Guest name: ");
            String newGuestName = scanner.next();

            System.out.print("Enter new room number: ");
            int newRoomNumber = scanner.nextInt();

            System.out.print("Enter new Contact Number: ");
            String newContactNumber = scanner.next();

            String sql = "UPDATE reservation_data SET " +
                    "guest_name = '" + newGuestName + "', " +
                    "room_nu = " + newRoomNumber + ", " +
                    "contact_nu = '" + newContactNumber + "' " +
                    "WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement()) {

                int affectedRows =
                        statement.executeUpdate(sql);

                if (affectedRows > 0) {

                    System.out.println(
                            "Reservation updated Successfully."
                    );

                } else {

                    System.out.println(
                            "Reservation update failed."
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void deleteReservation(
            Scanner scanner,
            Connection connection
    ) throws SQLException {

        try {

            System.out.print("Enter Reservation id to delete: ");
            int reservation_id = scanner.nextInt();

            if (!reservationExists(connection, reservation_id)) {

                System.out.println(
                        "Reservation not found for the given ID."
                );

                return;
            }

            String sql = "DELETE FROM reservation_data " +
                    "WHERE reservation_id = " + reservation_id;

            try (Statement statement = connection.createStatement()) {

                int affectedRows =
                        statement.executeUpdate(sql);

                if (affectedRows > 0) {

                    System.out.println(
                            "Reservation Deleted Successfully."
                    );

                } else {

                    System.out.println(
                            "Reservation deletion failed."
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(
            Connection connection,
            int reservation_id
    ) throws SQLException {

        try {

            String sql = "SELECT reservation_id " +
                    "FROM reservation_data " +
                    "WHERE reservation_id = " + reservation_id;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next();
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    public static void exit() throws InterruptedException {

        System.out.println("Exiting System");

        int i = 5;

        while (i >= 0) {

            System.out.print(".");
            Thread.sleep(450);
            i--;
        }

        System.out.println();

        System.out.println(
                "Thank you for visiting Prince Crown Hotel!!"
        );
    }
}

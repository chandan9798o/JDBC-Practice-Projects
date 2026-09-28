//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.sql.*;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.Date;
import java.sql.Timestamp;

public class Main {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/hospital_db";
        String username = "postgres";
        String password = "5498";

        try {
            Connection connection = DriverManager.getConnection(
                    url,
                    username,
                    password
            );
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("===================King George Medical University===================");
                System.out.println();
                System.out.println("1.Enter new Patient: ");
                System.out.println("2.View patients data: ");
                System.out.println("3.Check patient details: ");
                System.out.println("4.Update patient details: ");
                System.out.println("5.Delete patient data: ");
                System.out.println("0.Exit: ");
                System.out.print("Enter option: ");
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        newPatient(scanner, connection);
                        break;
                    case 2:
                        viewData(connection);
                        break;
                    case 3:
                        patientDetails(scanner, connection);
                        break;
                    case 4:
                        updateDetails(scanner, connection);
                        break;
                    case 5:
                        deleteDetails(scanner, connection);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        connection.close();
                        return;
                    default:
                        System.out.println("Invalid option input.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public static void newPatient(Scanner scanner, Connection connection) throws SQLException{
            System.out.print("Enter Patient name: ");
            String patient_name = scanner.next();
            System.out.print("Enter room number: ");
            int room_number = scanner.nextInt();
            System.out.print("Enter bed number: ");
            int bed_number = scanner.nextInt();
            System.out.print("Enter medical department: ");
            String department = scanner.next();
            System.out.print("Enter Guardian name: ");
            String guardian_name = scanner.next();
            System.out.print("Enter Contact number: ");
            String contact_number = scanner.next();
            System.out.print("Enter location: ");
            String location = scanner.next();
            System.out.println();

            String query = "INSERT INTO patient_data" +
                "(patient_name, room_nu, bed_nu, department, guardian_name, contact_nu, location)" +
                "VALUES ('" + patient_name + "'," +
                room_number + "," +
                bed_number + ",'" +
                department + "','" +
                guardian_name + "','" +
                contact_number + "','" +
                location + "')";
            try(Statement statement = connection.createStatement()){
                int affectedRows  = statement.executeUpdate(query);
                if(affectedRows > 0){
                    System.out.println("New Patient reservation succesfull.");
                } else{
                    System.out.println("New Reservation failed.");
                }

            } catch(SQLException e){
                e.printStackTrace();
            }
    }
    public static void viewData(Connection connection) throws SQLException {

        String query = "SELECT registration_id, " +
                "patient_name, " +
                "room_nu, " +
                "bed_nu, " +
                "department, " +
                "guardian_name, " +
                "contact_nu, " +
                "location, " +
                "registration_date FROM patient_data";

        try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(query)
        ) {

            System.out.println("Current Admit Patient.");

            System.out.println(
                    "+--------------------------+" +
                            "--------------------------+" +
                            "--------------------------+" +
                            "--------------------------+" +
                            "--------------------------+" +
                            "--------------------------+" +
                            "--------------------------+" +
                            "--------------------------+" +
                            "--------------------------+"
            );

            System.out.printf(
                    "| %-24s | %-24s | %-24s | %-24s | %-24s | %-24s | %-24s | %-24s | %-24s |%n",
                    "Registration_Id",
                    "Patient_name",
                    "Room_number",
                    "Bed_number",
                    "Department",
                    "Guardian_name",
                    "Contact_number",
                    "Location",
                    "Registration_date"
            );

            System.out.println(
                    "+--------------------------+" +
                            "--------------------------+" +
                            "--------------------------+" +
                            "--------------------------+" +
                            "--------------------------+" +
                            "--------------------------+" +
                            "--------------------------+" +
                            "--------------------------+" +
                            "--------------------------+"
            );

            while (result.next()) {

                int registration_id = result.getInt("registration_id");
                String patient_name = result.getString("patient_name");
                int room_nu = result.getInt("room_nu");
                int bed_nu = result.getInt("bed_nu");
                String department = result.getString("department");
                String guardian_name = result.getString("guardian_name");
                String contact_nu = result.getString("contact_nu");
                String location = result.getString("location");
                String registration_date =
                        result.getTimestamp("registration_date").toString();

                System.out.printf(
                        "| %-24d | %-24s | %-24d | %-24d | %-24s | %-24s | %-24s | %-24s | %-24s |%n",
                        registration_id,
                        patient_name,
                        room_nu,
                        bed_nu,
                        department,
                        guardian_name,
                        contact_nu,
                        location,
                        registration_date
                );

                System.out.println(
                        "+--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+"
                );
            }
        }
    }
    public static void patientDetails(Scanner scanner, Connection connection) throws SQLException {

        try {
            System.out.print("Enter registration_id to see Details: ");
            int registration_id = scanner.nextInt();

            if (!registrationExists(connection, registration_id)) {
                System.out.println("Registration not found for given id.");
                return;
            }

            String query = "SELECT registration_id, " +
                    "patient_name, " +
                    "room_nu, " +
                    "bed_nu, " +
                    "department, " +
                    "guardian_name, " +
                    "contact_nu, " +
                    "location, " +
                    "registration_date " +
                    "FROM patient_data " +
                    "WHERE registration_id=" + registration_id;

            try (
                    Statement statement = connection.createStatement();
                    ResultSet result = statement.executeQuery(query)
            ) {

                System.out.println("Given Id Patient Details --");

                System.out.println(
                        "+--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+"
                );

                System.out.printf(
                        "| %-24s | %-24s | %-24s | %-24s | %-24s | %-24s | %-24s | %-24s | %-24s |%n",
                        "Registration_Id",
                        "Patient_name",
                        "Room_number",
                        "Bed_number",
                        "Department",
                        "Guardian_name",
                        "Contact_number",
                        "Location",
                        "Registration_date"
                );

                System.out.println(
                        "+--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+" +
                                "--------------------------+"
                );

                while (result.next()) {

                    int registration_id_result = result.getInt("registration_id");
                    String patient_name = result.getString("patient_name");
                    int room_nu = result.getInt("room_nu");
                    int bed_nu = result.getInt("bed_nu");
                    String department = result.getString("department");
                    String guardian_name = result.getString("guardian_name");
                    String contact_nu = result.getString("contact_nu");
                    String location = result.getString("location");
                    String registration_date =
                            result.getTimestamp("registration_date").toString();

                    System.out.printf(
                            "| %-24d | %-24s | %-24d | %-24d | %-24s | %-24s | %-24s | %-24s | %-24s |%n",
                            registration_id_result,
                            patient_name,
                            room_nu,
                            bed_nu,
                            department,
                            guardian_name,
                            contact_nu,
                            location,
                            registration_date
                    );

                    System.out.println(
                            "+--------------------------+" +
                                    "--------------------------+" +
                                    "--------------------------+" +
                                    "--------------------------+" +
                                    "--------------------------+" +
                                    "--------------------------+" +
                                    "--------------------------+" +
                                    "--------------------------+" +
                                    "--------------------------+"
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateDetails(Scanner scanner, Connection connection) throws SQLException {
        try {
            System.out.print("Enter registration_id to update the details: ");
            int registration_id = scanner.nextInt();
            if (!registrationExists(connection, registration_id)) {
                System.out.println("Registration did not found for given Id.");
                return;
            }
            System.out.print("Enter Patient new name: ");
            String new_patient_name = scanner.next();
            System.out.print("Enter new room number: ");
            int new_room_number = scanner.nextInt();
            System.out.print("Enter new bed number: ");
            int new_bed_number = scanner.nextInt();
            System.out.print("Enter  new medical department: ");
            String new_department = scanner.next();
            System.out.print("Enter new Guardian name: ");
            String new_guardian_name = scanner.next();
            System.out.print("Enter new Contact number: ");
            String new_contact_number = scanner.next();
            System.out.print("Enter new location: ");
            String new_location = scanner.next();

            String query = "UPDATE patient_data SET " +
                    "patient_name='" + new_patient_name + "', " +
                    "room_nu=" + new_room_number + ", " +
                    "bed_nu=" + new_bed_number + ", " +
                    "guardian_name='" + new_guardian_name + "', " +
                    "contact_nu='" + new_contact_number + "', " +
                    "location='" + new_location + "' " +
                    "WHERE registration_id=" + registration_id;
            try (Statement statement = connection.createStatement()) {
                int affactedRows = statement.executeUpdate(query);
                if (affactedRows > 0) {
                    System.out.println("Updated data successfull.");
                } else {
                    System.out.println("Updation failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void deleteDetails(Scanner scanner, Connection connection) throws SQLException{
        try{
            System.out.print("Enter patient_id to delete:- ");
            int registration_id = scanner.nextInt();
            if(!registrationExists(connection, registration_id)){
                System.out.println("Registration id did not found for deletion.");
            }
            String query = "DELETE FROM patient_data WHERE registration_id = " + registration_id;
            try(Statement statement = connection.createStatement()){
                int affactedRows = statement.executeUpdate(query);
                if(affactedRows> 0){
                    System.out.println("Patient details deleted Successfully.");
                } else{
                    System.out.println("Deletion failed.");
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static boolean registrationExists(Connection connection, int registration_id) throws SQLException {

        try {
            String query = "SELECT registration_id " +
                    "FROM patient_data " +
                    "WHERE registration_id=" + registration_id;

            try (
                    Statement statement = connection.createStatement();
                    ResultSet result = statement.executeQuery(query)
            ) {
                return result.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void exit() throws InterruptedException{
        System.out.println("System Existing");
        int i = 1;
        while(i>=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println("Thank you for visiting KGMU. Get well soon");
    }

}

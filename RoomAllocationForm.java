import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomAllocationForm extends JFrame {
    private JTextField rollNoField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField departmentField;
    private JLabel allocatedRoomLabel;

    public RoomAllocationForm(Connection connection) {
        setTitle("Hostel Room Allocation");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Create form components
        JLabel rollNoLabel = new JLabel("Roll Number:");
        rollNoField = new JTextField();
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField();
        JLabel departmentLabel = new JLabel("Department:");
        departmentField = new JTextField();
        JButton allocateButton = new JButton("Allocate Room");
        allocatedRoomLabel = new JLabel("");

        // Add components to the frame
        add(rollNoLabel);
        add(rollNoField);
        add(nameLabel);
        add(nameField);
        add(ageLabel);
        add(ageField);
        add(departmentLabel);
        add(departmentField);
        add(allocateButton);
        add(allocatedRoomLabel);

        // Add button click action
        allocateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allocateRoom(connection);
            }
        });
    }

    private void allocateRoom(Connection connection) {
        String rollNo = rollNoField.getText();
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String department = departmentField.getText();

        try {
            // Find an available room
            int allocatedRoom = findAvailableRoom(connection);
            if (allocatedRoom != -1) {
                // Save student to the database
                Student student = new Student(rollNo, name, age, department, "2000-01-01"); // Dummy DOB
                student.saveToDatabase(connection);

                // Allocate room and update the allocations table
                saveAllocationToDatabase(connection, rollNo, allocatedRoom);

                // Update the label with the allocated room number
                allocatedRoomLabel.setText("Allocated Room: " + allocatedRoom);
            } else {
                allocatedRoomLabel.setText("No rooms available.");
            }
        } catch (SQLException | NumberFormatException ex) {
            allocatedRoomLabel.setText("Error: " + ex.getMessage());
        }
    }

    private int findAvailableRoom(Connection connection) throws SQLException {
        String query = "SELECT room_no FROM roomavailability WHERE is_available = true LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("room_no");
            }
        }
        return -1; // No available room
    }

    private void saveAllocationToDatabase(Connection connection, String rollNo, int roomNo) throws SQLException {
        String query = "INSERT INTO allocations (rollno, room_no) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rollNo);
            statement.setInt(2, roomNo);
            statement.executeUpdate();
        }
    }

    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnector.connect();
            RoomAllocationForm form = new RoomAllocationForm(connection);
            form.setVisible(true);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }
}

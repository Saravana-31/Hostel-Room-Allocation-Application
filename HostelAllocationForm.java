import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class HostelAllocationForm extends JFrame {
    private JTextField rollnoField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField departmentField;
    private JTextField dobField;
    private JLabel allocatedRoomLabel;

    public HostelAllocationForm(Connection connection) {
        setTitle("Hostel Allocation Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Input fields
        add(new JLabel("Roll No:"));
        rollnoField = new JTextField();
        add(rollnoField);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Age:"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Department:"));
        departmentField = new JTextField();
        add(departmentField);

        add(new JLabel("DOB (YYYY-MM-DD):"));
        dobField = new JTextField();
        add(dobField);

        JButton submitButton = new JButton("Allocate Room");
        add(submitButton);

        allocatedRoomLabel = new JLabel("");
        add(allocatedRoomLabel);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allocateRoom(connection);
            }
        });
    }

    private void allocateRoom(Connection connection) {
        String rollno = rollnoField.getText();
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String department = departmentField.getText();
        String dob = dobField.getText();

        // Create a new Student object
        Student student = new Student(rollno, name, age, department, dob);
        student.saveToDatabase(connection);

        // Allocate room logic here (you may want to implement a room allocation method)
        // This is just a placeholder
        Integer allocatedRoomNumber = 101; // Replace this with your room allocation logic
        allocatedRoomLabel.setText("Room Allocated: " + allocatedRoomNumber);
        student.setAllocatedRoomNumber(allocatedRoomNumber);
    }

    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnector.connect();
            HostelAllocationForm form = new HostelAllocationForm(connection);
            form.setVisible(true);
        } catch (Exception e) {  // Change SQLException to Exception
            System.err.println("Error: " + e.getMessage());
        }
    }
}

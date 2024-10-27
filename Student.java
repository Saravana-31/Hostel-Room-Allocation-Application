import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Student {
    private String studentID;
    private String name;
    private int age;
    private String department;
    private String dob; // Date of Birth
    private Integer allocatedRoomNumber; // Room number where the student is allocated

    public Student(String studentID, String name, int age, String department, String dob) {
        this.studentID = studentID;
        this.name = name;
        this.age = age;
        this.department = department;
        this.dob = dob;
        this.allocatedRoomNumber = null; // Initially, no room is allocated
    }

    // Getters
    public String getStudentID() {
        return studentID;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getDepartment() {
        return department;
    }

    public String getDob() {
        return dob;
    }

    public Integer getAllocatedRoomNumber() {
        return allocatedRoomNumber;
    }

    public void setAllocatedRoomNumber(Integer allocatedRoomNumber) {
        this.allocatedRoomNumber = allocatedRoomNumber;
    }

    // Method to save student details to the database
    public void saveToDatabase(Connection connection) {
        String query = "INSERT INTO student (rollno, name, age, department, dob) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, studentID);
            statement.setString(2, name);
            statement.setInt(3, age);
            statement.setString(4, department);
            statement.setString(5, dob);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to save student " + name + ": " + e.getMessage());
        }
    }
}


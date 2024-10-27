import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Allocation {

    public static void allocateStudents(List<Student> students, Connection connection) {
        try {
            for (Student student : students) {
                int availableRoom = getAvailableRoom(connection);
                if (availableRoom != -1) {
                    student.setAllocatedRoomNumber(availableRoom);
                    saveAllocationToDatabase(student, connection, availableRoom);
                    markRoomAsOccupied(connection, availableRoom); // Mark room as occupied
                } else {
                    System.out.println("No available rooms for student: " + student.getName());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during room allocation: " + e.getMessage());
        }
    }

    private static int getAvailableRoom(Connection connection) throws SQLException {
        String query = "SELECT room_no FROM roomavailability WHERE is_available = TRUE LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("room_no");
            }
        }
        return -1; // No available room
    }

    private static void saveAllocationToDatabase(Student student, Connection connection, int roomNo) throws SQLException {
        String query = "INSERT INTO allocations (rollno, room_no) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, student.getStudentID());
            statement.setInt(2, roomNo);
            statement.executeUpdate();
        }
    }

    private static void markRoomAsOccupied(Connection connection, int roomNo) throws SQLException {
        String query = "UPDATE roomavailability SET is_available = FALSE WHERE room_no = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, roomNo);
            statement.executeUpdate();
        }
    }
}

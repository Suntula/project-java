import java.io.*;
import java.util.*;

public class MISSystem {

    // Student class
    static class Student {
        private String id;
        private String name;
        private String email;

        public Student(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Email: " + email;
        }
    }

    // StudentManager class
    static class StudentManager {
        private List<Student> students = new ArrayList<>();
        private static final String FILE_NAME = "students.txt";

        public void addStudent(Student student) {
            if (isLCIDUnique(student.getId())) {
                students.add(student);
                saveToFile();
                System.out.println("[LCID – " + student.getId() + " – Add Student]");
            } else {
                System.out.println("Error: LCID already exists.");
            }
        }

        public void updateStudent(String id, String name, String email) {
            for (Student student : students) {
                if (student.getId().equals(id)) {
                    student.setName(name);
                    student.setEmail(email);
                    saveToFile();
                    System.out.println("[LCID – " + id + " – Update Student]");
                    return;
                }
            }
            System.out.println("Student not found.");
        }

        public void viewStudents() {
            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                for (Student student : students) {
                    System.out.println(student);
                }
            }
            System.out.println("[LCID – View – View Students]");
        }

        public void deleteStudent(String id) {
            Iterator<Student> iterator = students.iterator();
            while (iterator.hasNext()) {
                Student student = iterator.next();
                if (student.getId().equals(id)) {
                    iterator.remove();
                    saveToFile();
                    System.out.println("[LCID – " + id + " – Delete Student]");
                    return;
                }
            }
            System.out.println("Student not found.");
        }

        private boolean isLCIDUnique(String id) {
            for (Student student : students) {
                if (student.getId().equals(id)) {
                    return false;
                }
            }
            return true;
        }

        private void saveToFile() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (Student student : students) {
                    writer.write(student.getId() + "," + student.getName() + "," + student.getEmail());
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error saving to file: " + e.getMessage());
            }
        }

        public void loadFromFile() {
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        students.add(new Student(parts[0], parts[1], parts[2]));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading from file: " + e.getMessage());
            }
        }
    }

    // Main class to run the menu
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManager studentManager = new StudentManager();
        studentManager.loadFromFile();
        boolean exit = false;

        while (!exit) {
            System.out.println("==============================");
            System.out.println("Welcome to MIS System");
            System.out.println("==============================");
            System.out.println("1. Add Information");
            System.out.println("2. Update Information");
            System.out.println("3. View Inventory");
            System.out.println("4. Delete Information");
            System.out.println("5. Exit");
            System.out.println("==============================");
            System.out.print("Please enter your choice (1-5): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter ID (10-digit LCID): ");
                    String id = scanner.nextLine();
                    if (id.length() != 10) {
                        System.out.println("Error: LCID must be 10 digits long.");
                        break;
                    }
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    studentManager.addStudent(new Student(id, name, email));
                    break;
                case 2:
                    System.out.print("Enter ID of student to update: ");
                    String updateId = scanner.nextLine();
                    System.out.print("Enter new Name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new Email: ");
                    String newEmail = scanner.nextLine();
                    studentManager.updateStudent(updateId, newName, newEmail);
                    break;
                case 3:
                    studentManager.viewStudents();
                    break;
                case 4:
                    System.out.print("Enter ID of student to delete: ");
                    String deleteId = scanner.nextLine();
                    studentManager.deleteStudent(deleteId);
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }

            // Clear the console (platform-dependent)
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }

        scanner.close();
    }
}

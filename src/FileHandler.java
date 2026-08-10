import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    private static final String FILE_NAME = "employees.txt";

    // Save ALL employees to file (overwrites file completely)
    public static void saveAllEmployees(ArrayList<Employee> list) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
            for (Employee emp : list) {
                writer.write(emp.toFileString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving employees: " + e.getMessage());
        }
    }

    // Load ALL employees from file at startup
    public static ArrayList<Employee> loadAllEmployees() {
        ArrayList<Employee> list = new ArrayList<>();
        File file = new File(FILE_NAME);

        // If file doesn't exist yet, return empty list
        if (!file.exists()) return list;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Employee emp = parseLine(line);
                if (emp != null) list.add(emp);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading employees: " + e.getMessage());
        }
        return list;
    }

    // Parse a CSV line from file and create the correct Employee subclass
    private static Employee parseLine(String line) {
        try {
            String[] parts = line.split(",");
            String role = parts[0];

            // Common fields: role, id, name, dept, contact
            String id      = parts[1];
            String name    = parts[2];
            String dept    = parts[3];
            String contact = parts[4];

            if (role.equals("Engineer")) {
                String spec = parts[5];
                int exp = Integer.parseInt(parts[6]);
                return new Engineer(id, name, dept, contact, spec, exp);

            } else if (role.equals("Manager")) {
                String managedDept = parts[5];
                int teamSize = Integer.parseInt(parts[6]);
                return new Manager(id, name, dept, contact, managedDept, teamSize);

            } else if (role.equals("Worker")) {
                String jobType = parts[5];
                double hourlyRate = Double.parseDouble(parts[6]);
                int hours = Integer.parseInt(parts[7]);
                return new Worker(id, name, dept, contact, jobType, hourlyRate, hours);
            }
        } catch (Exception e) {
            System.out.println("Error parsing line: " + line);
        }
        return null;
    }

}

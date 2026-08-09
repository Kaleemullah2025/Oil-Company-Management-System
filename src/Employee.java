// =========================================================
// Abstract Employee Class - Base class for all employee types
// Demonstrates: Abstraction, Encapsulation
// =========================================================
public abstract class Employee {

    // Private fields - Encapsulation
    private String employeeId;
    private String name;
    private String department;
    private String contactNumber;

    // Constructor
    public Employee(String employeeId, String name, String department, String contactNumber) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.contactNumber = contactNumber;
    }

    // Abstract method - Polymorphism (each subclass implements differently)
    public abstract double calculateSalary();

    // Abstract method to get employee role/type
    public abstract String getRole();

    // Getters - Encapsulation
    public String getEmployeeId() { return employeeId; }
    public String getName()       { return name; }
    public String getDepartment() { return department; }
    public String getContactNumber() { return contactNumber; }

    // Setters - Encapsulation
    public void setName(String name)                   { this.name = name; }
    public void setDepartment(String department)       { this.department = department; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    // Convert to CSV line for file saving
    public String toFileString() {
        return employeeId + "," + name + "," + department + "," + contactNumber;
    }

    // Display info as array (for JTable rows)
    public Object[] toTableRow() {
        return new Object[]{
            employeeId, name, getRole(), department,
            contactNumber, String.format("%.2f", calculateSalary())
        };
    }
}

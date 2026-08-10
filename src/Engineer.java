// =========================================================
// Engineer Class - Inherits from Employee
// Demonstrates: Inheritance, Polymorphism
// =========================================================
public class Engineer extends Employee {

    private int experienceYears;

    // Base salary for Engineer role
    private static final double BASE_SALARY = 120000.0;

    // Constructor
    public Engineer(String employeeId, String name, String department,
                    String contactNumber, String specialization, int experienceYears) {
        super(employeeId, name, department, contactNumber); // Call parent constructor
        this.experienceYears = experienceYears;
    }

    // Polymorphism - Engineer salary = base + experience bonus
    @Override
    public double calculateSalary() {
        double bonus = experienceYears * 5000.0; // 5000 per year of experience
        return BASE_SALARY + bonus;
    }

    @Override
    public String getRole() {
        return "Engineer";
    }

    // Getters and Setters
    public int getExperienceYears()               { return experienceYears; }
    public void setExperienceYears(int y)          { this.experienceYears = y; }

    // Save to file: role,id,name,dept,contact,spec,exp
    @Override
    public String toFileString() {
        return "Engineer," + super.toFileString() + ","  + "," + experienceYears;
    }
}

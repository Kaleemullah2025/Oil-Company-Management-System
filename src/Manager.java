public class Manager extends Employee {

    private String managedDept;  // Which department this manager manages
    private int teamSize;        // Number of employees under this manager

    private static final double BASE_SALARY = 180000.0;

    public Manager(String employeeId, String name, String department,
                   String contactNumber, String managedDept, int teamSize) {
        super(employeeId, name, department, contactNumber);
        this.managedDept = managedDept;
        this.teamSize = teamSize;
    }

    @Override
    public double calculateSalary() {
        double teamAllowance = teamSize * 3000.0; // 3000 per team member
        return BASE_SALARY + teamAllowance;
    }

    @Override
    public String getRole() {
        return "Manager";
    }

    public String getManagedDept()           { return managedDept; }
    public int getTeamSize()                 { return teamSize; }
    public void setManagedDept(String d)     { this.managedDept = d; }
    public void setTeamSize(int t)           { this.teamSize = t; }

    @Override
    public String toFileString() {
        return "Manager," + super.toFileString() + "," + managedDept + "," + teamSize;
    }
}

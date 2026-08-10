public class Worker extends Employee {

    private double hourlyRate;
    private int hoursPerMonth;

    public Worker(String employeeId, String name, String department,
                  String contactNumber, String jobType, double hourlyRate, int hoursPerMonth) {
        super(employeeId, name, department, contactNumber);
        this.hourlyRate = hourlyRate;
        this.hoursPerMonth = hoursPerMonth;
    }

    @Override
    public double calculateSalary() {
        return hourlyRate * hoursPerMonth;
    }

    @Override
    public String getRole() {
        return "Worker";
    }

    public double getHourlyRate()             { return hourlyRate; }
    public int getHoursPerMonth()             { return hoursPerMonth; }
    public void setHourlyRate(double r)        { this.hourlyRate = r; }
    public void setHoursPerMonth(int h)        { this.hoursPerMonth = h; }
    @Override
    public String toFileString() {
        return "Worker," + super.toFileString() + "," + "," + hourlyRate + "," + hoursPerMonth;
    }
}

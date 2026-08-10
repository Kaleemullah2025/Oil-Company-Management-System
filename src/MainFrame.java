import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;

public class MainFrame extends JFrame {

    // ArrayList to store all employees in memory - Demonstrates ArrayList
    private ArrayList<Employee> employeeList;

    // Table model for the employee display table
    private DefaultTableModel tableModel;
    private JTable employeeTable;

    // Search field
    private JTextField searchField;

    public MainFrame() {
        setTitle(" Oil Company - Management System");
        setSize(950, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load employees from file when app opens
        employeeList = FileHandler.loadAllEmployees();

        buildUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

    private void buildUI() {
        // Top header bar
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(20, 30, 48));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel companyName = new JLabel(" OIL COMPANY MANAGEMENT SYSTEM");
        companyName.setFont(new Font("Arial", Font.BOLD, 16));
        companyName.setForeground(new Color(255, 200, 50));

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBackground(new Color(200, 50, 50));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFocusPainted(false);
        exitBtn.addActionListener(e -> exitApplication());

        headerPanel.add(companyName, BorderLayout.WEST);
        headerPanel.add(exitBtn, BorderLayout.EAST);

        // Tabbed pane for navigation
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.setBackground(Color.WHITE);

        // Build each tab
        tabs.addTab("View All", buildViewPanel());
        tabs.addTab("Add Employee", buildAddPanel());
        tabs.addTab("Update Employee", buildUpdatePanel());
        tabs.addTab("Delete Employee", buildDeletePanel());
        tabs.addTab("Search", buildSearchPanel());
        tabs.addTab("Report", buildReportPanel());

        // Layout
        add(headerPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }


    // VIEW ALL EMPLOYEES TAB
    // ====================================================
    private JPanel buildViewPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table columns
        String[] columns = {"ID", "Name", "Role", "Department", "Contact", "Salary (PKR)"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.setRowHeight(25);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 12));
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        employeeTable.getTableHeader().setBackground(new Color(20, 30, 48));
        employeeTable.getTableHeader().setForeground(Color.WHITE);
        employeeTable.setSelectionBackground(new Color(255, 200, 50));

        JScrollPane scrollPane = new JScrollPane(employeeTable);

        // Refresh button
        JButton refreshBtn = new JButton("Refresh Table");
        refreshBtn.setBackground(new Color(20, 30, 48));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> refreshTable());

        JLabel countLabel = new JLabel("Total Employees: " + employeeList.size());
        countLabel.setFont(new Font("Arial", Font.BOLD, 13));

        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.add(countLabel, BorderLayout.WEST);
        bottomBar.add(refreshBtn, BorderLayout.EAST);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomBar, BorderLayout.SOUTH);

        // Load data immediately
        refreshTable();
        return panel;
    }

    // Reload table from employeeList ArrayList
    public void refreshTable() {
        tableModel.setRowCount(0); // Clear existing rows
        for (Employee emp : employeeList) {
            tableModel.addRow(emp.toTableRow());
        }
    }

    // ====================================================
    // ADD EMPLOYEE TAB
    // ====================================================
    private JPanel buildAddPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel heading = new JLabel("Add New Employee", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Form fields
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

        JTextField idField       = new JTextField();
        JTextField nameField     = new JTextField();
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Engineer", "Manager", "Worker"});
        JComboBox<String> deptBox = new JComboBox<>(new String[]{
            "Exploration", "Production", "Refining", "HR", "Finance", "Safety", "Logistics"
        });
        JTextField contactField  = new JTextField();
        JTextField extra1Field   = new JTextField(); // spec / managedDept / jobType
        JTextField extra2Field   = new JTextField(); // exp / teamSize / hourlyRate
        JTextField extra3Field   = new JTextField(); // (Worker only: hoursPerMonth)


        JLabel extra2Label = new JLabel("Experience (Years):");
        JLabel extra3Label = new JLabel("(Not Required):");
        extra3Field.setEnabled(false);

        // Update labels when role changes
        roleBox.addActionListener(e -> {
            String role = (String) roleBox.getSelectedItem();
            if (role.equals("Engineer")) {
                extra2Label.setText("Experience (Years):");
                extra3Label.setText("(Not Required):");
                extra3Field.setEnabled(false);
                extra3Field.setText("");
            } else if (role.equals("Manager")) {
                extra2Label.setText("Team Size:");
                extra3Field.setEnabled(false);
                extra3Field.setText("");
            } else { // Worker
                extra2Label.setText("Hourly Rate (PKR):");
                extra3Label.setText("Hours Per Month:");
                extra3Field.setEnabled(true);
            }
        });

        form.add(new JLabel("Employee ID:"));  form.add(idField);
        form.add(new JLabel("Full Name:"));    form.add(nameField);
        form.add(new JLabel("Role:"));         form.add(roleBox);
        form.add(new JLabel("Department:"));   form.add(deptBox);
        form.add(new JLabel("Contact No:"));   form.add(contactField);
       // form.add(extra1Label);                  form.add(extra1Field);
        form.add(extra2Label);                  form.add(extra2Field);
        form.add(extra3Label);                  form.add(extra3Field);

        JLabel statusLbl = new JLabel("", SwingConstants.CENTER);
        statusLbl.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton addBtn = new JButton("Add Employee");
        addBtn.setBackground(new Color(0, 130, 50));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addBtn.setFocusPainted(false);

        // Add button logic
        addBtn.addActionListener(e -> {
            try {
                String id      = idField.getText().trim();
                String name    = nameField.getText().trim();
                String role    = (String) roleBox.getSelectedItem();
                String dept    = (String) deptBox.getSelectedItem();
                String contact = contactField.getText().trim();

                if (id.isEmpty() || name.isEmpty() || contact.isEmpty()) {
                    statusLbl.setForeground(Color.RED);
                    statusLbl.setText("Please fill all required fields.");
                    return;
                }

                // Check for duplicate ID
                for (Employee emp : employeeList) {
                    if (emp.getEmployeeId().equals(id)) {
                        statusLbl.setForeground(Color.RED);
                        statusLbl.setText("Employee ID already exists!");
                        return;
                    }
                }

                Employee newEmp = null;
                if (role.equals("Engineer")) {
                    String spec = extra1Field.getText().trim();
                    int exp = Integer.parseInt(extra2Field.getText().trim());
                    newEmp = new Engineer(id, name, dept, contact, spec, exp);
                } else if (role.equals("Manager")) {
                    String managedDept = extra1Field.getText().trim();
                    int team = Integer.parseInt(extra2Field.getText().trim());
                    newEmp = new Manager(id, name, dept, contact, managedDept, team);
                } else {
                    String jobType = extra1Field.getText().trim();
                    double rate    = Double.parseDouble(extra2Field.getText().trim());
                    int hours      = Integer.parseInt(extra3Field.getText().trim());
                    newEmp = new Worker(id, name, dept, contact, jobType, rate, hours);
                }

                employeeList.add(newEmp);
                FileHandler.saveAllEmployees(employeeList);
                refreshTable();

                // Clear fields
                idField.setText(""); nameField.setText(""); contactField.setText("");
                extra1Field.setText(""); extra2Field.setText(""); extra3Field.setText("");

                statusLbl.setForeground(new Color(0, 130, 0));
                statusLbl.setText("Employee added successfully!");

            } catch (NumberFormatException ex) {
                statusLbl.setForeground(Color.RED);
                statusLbl.setText("Please enter valid numbers in numeric fields.");
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new GridLayout(2, 1));
        southPanel.add(btnPanel);
        southPanel.add(statusLbl);
        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ====================================================
    // UPDATE EMPLOYEE TAB
    // ====================================================
    private JPanel buildUpdatePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel heading = new JLabel("Update Employee", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLbl = new JLabel("Enter Employee ID: ");
        JTextField searchId = new JTextField(15);
        JButton findBtn = new JButton("Find");
        findBtn.setBackground(new Color(20, 30, 48));
        findBtn.setForeground(Color.WHITE);
        findBtn.setFocusPainted(false);

        searchBar.add(searchLbl);
        searchBar.add(searchId);
        searchBar.add(findBtn);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        JTextField nameField    = new JTextField();
        JTextField contactField = new JTextField();
        JComboBox<String> deptBox = new JComboBox<>(new String[]{
            "Exploration", "Production", "Refining", "HR", "Finance", "Safety", "Logistics"
        });

        JLabel roleInfoLabel = new JLabel("Role: -");
        roleInfoLabel.setFont(new Font("Arial", Font.BOLD, 12));

        formPanel.add(new JLabel("Name:"));    formPanel.add(nameField);
        formPanel.add(new JLabel("Department:")); formPanel.add(deptBox);
        formPanel.add(new JLabel("Contact:")); formPanel.add(contactField);
        formPanel.add(roleInfoLabel);          formPanel.add(new JLabel(""));

        JLabel statusLbl = new JLabel("", SwingConstants.CENTER);
        JButton updateBtn = new JButton("Save Changes");
        updateBtn.setBackground(new Color(0, 100, 200));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFocusPainted(false);
        updateBtn.setEnabled(false);

        // Store found employee reference using array trick (for inner class access)
        Employee[] foundEmp = {null};

        findBtn.addActionListener(e -> {
            String id = searchId.getText().trim();
            foundEmp[0] = null;
            for (Employee emp : employeeList) {
                if (emp.getEmployeeId().equals(id)) {
                    foundEmp[0] = emp;
                    break;
                }
            }
            if (foundEmp[0] != null) {
                nameField.setText(foundEmp[0].getName());
                contactField.setText(foundEmp[0].getContactNumber());
                deptBox.setSelectedItem(foundEmp[0].getDepartment());
                roleInfoLabel.setText("Role: " + foundEmp[0].getRole());
                updateBtn.setEnabled(true);
                statusLbl.setForeground(new Color(0, 130, 0));
                statusLbl.setText("Employee found. Edit fields and click Save.");
            } else {
                statusLbl.setForeground(Color.RED);
                statusLbl.setText("Employee ID not found.");
                updateBtn.setEnabled(false);
                nameField.setText(""); contactField.setText("");
                roleInfoLabel.setText("Role: -");
            }
        });

        updateBtn.addActionListener(e -> {
            if (foundEmp[0] == null) return;
            foundEmp[0].setName(nameField.getText().trim());
            foundEmp[0].setDepartment((String) deptBox.getSelectedItem());
            foundEmp[0].setContactNumber(contactField.getText().trim());
            FileHandler.saveAllEmployees(employeeList);
            refreshTable();
            statusLbl.setForeground(new Color(0, 130, 0));
            statusLbl.setText("Employee updated successfully!");
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(updateBtn);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(searchBar, BorderLayout.NORTH);

        JPanel centerSection = new JPanel(new BorderLayout(10, 10));
        centerSection.add(heading, BorderLayout.NORTH);
        centerSection.add(searchBar, BorderLayout.CENTER);
        centerSection.add(formPanel, BorderLayout.SOUTH);

        panel.add(centerSection, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new GridLayout(2, 1));
        southPanel.add(btnPanel);
        southPanel.add(statusLbl);
        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ====================================================
    // DELETE EMPLOYEE TAB
    // ====================================================
    private JPanel buildDeletePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        JLabel heading = new JLabel("Delete Employee", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel idLbl = new JLabel("Employee ID to Delete:");
        idLbl.setFont(new Font("Arial", Font.PLAIN, 13));
        JTextField idField = new JTextField(18);
        idField.setFont(new Font("Arial", Font.PLAIN, 13));

        form.add(idLbl);
        form.add(idField);

        JLabel infoLabel = new JLabel("", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel statusLbl = new JLabel("", SwingConstants.CENTER);
        statusLbl.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton findBtn = new JButton("Find Employee");
        findBtn.setBackground(new Color(20, 30, 48));
        findBtn.setForeground(Color.WHITE);
        findBtn.setFocusPainted(false);

        JButton deleteBtn = new JButton("Delete Permanently");
        deleteBtn.setBackground(new Color(200, 30, 30));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 13));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setEnabled(false);

        Employee[] foundEmp = {null};

        findBtn.addActionListener(e -> {
            foundEmp[0] = null;
            String id = idField.getText().trim();
            for (Employee emp : employeeList) {
                if (emp.getEmployeeId().equals(id)) {
                    foundEmp[0] = emp;
                    break;
                }
            }
            if (foundEmp[0] != null) {
                infoLabel.setForeground(new Color(0, 80, 0));
                infoLabel.setText("Found: " + foundEmp[0].getName() + " | " +
                                   foundEmp[0].getRole() + " | " + foundEmp[0].getDepartment());
                deleteBtn.setEnabled(true);
                statusLbl.setText("");
            } else {
                infoLabel.setForeground(Color.RED);
                infoLabel.setText("No employee found with this ID.");
                deleteBtn.setEnabled(false);
            }
        });

        deleteBtn.addActionListener(e -> {
            if (foundEmp[0] == null) return;
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete " + foundEmp[0].getName() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                employeeList.remove(foundEmp[0]);
                FileHandler.saveAllEmployees(employeeList);
                refreshTable();
                infoLabel.setText("");
                idField.setText("");
                deleteBtn.setEnabled(false);
                statusLbl.setForeground(new Color(0, 130, 0));
                statusLbl.setText("Employee deleted successfully.");
                foundEmp[0] = null;
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.add(findBtn);
        btnPanel.add(deleteBtn);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);

        JPanel southSection = new JPanel(new GridLayout(3, 1, 5, 5));
        southSection.add(infoLabel);
        southSection.add(btnPanel);
        southSection.add(statusLbl);
        panel.add(southSection, BorderLayout.SOUTH);

        return panel;
    }

    // ====================================================
    // SEARCH TAB
    // ====================================================
    private JPanel buildSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBorder(BorderFactory.createTitledBorder("Search Employees"));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));

        JComboBox<String> filterBox = new JComboBox<>(new String[]{
            "By Name", "By ID", "By Department", "By Role"
        });

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(20, 30, 48));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setFocusPainted(false);

        topBar.add(new JLabel("Search:"));
        topBar.add(searchField);
        topBar.add(filterBox);
        topBar.add(searchBtn);
        topBar.add(clearBtn);

        // Search results table
        String[] columns = {"ID", "Name", "Role", "Department", "Contact", "Salary (PKR)"};
        DefaultTableModel searchModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable searchTable = new JTable(searchModel);
        searchTable.setRowHeight(25);
        searchTable.setFont(new Font("Arial", Font.PLAIN, 12));
        searchTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        searchTable.getTableHeader().setBackground(new Color(20, 30, 48));
        searchTable.getTableHeader().setForeground(Color.WHITE);

        JLabel resultCountLbl = new JLabel("Results: 0");

        searchBtn.addActionListener(e -> {
            searchModel.setRowCount(0);
            String keyword = searchField.getText().trim().toLowerCase();
            String filter  = (String) filterBox.getSelectedItem();
            int count = 0;
            for (Employee emp : employeeList) {
                boolean match = false;
                if (filter.equals("By Name") && emp.getName().toLowerCase().contains(keyword)) match = true;
                else if (filter.equals("By ID") && emp.getEmployeeId().toLowerCase().contains(keyword)) match = true;
                else if (filter.equals("By Department") && emp.getDepartment().toLowerCase().contains(keyword)) match = true;
                else if (filter.equals("By Role") && emp.getRole().toLowerCase().contains(keyword)) match = true;

                if (match) {
                    searchModel.addRow(emp.toTableRow());
                    count++;
                }
            }
            resultCountLbl.setText("Results: " + count);
        });

        clearBtn.addActionListener(e -> {
            searchField.setText("");
            searchModel.setRowCount(0);
            resultCountLbl.setText("Results: 0");
        });

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(new JScrollPane(searchTable), BorderLayout.CENTER);
        panel.add(resultCountLbl, BorderLayout.SOUTH);

        return panel;
    }

    // REPORT TAB
    // ====================================================
    private JPanel buildReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel heading = new JLabel("Company Report", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JTextArea reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setEditable(false);
        reportArea.setBackground(new Color(245, 245, 245));

        JButton generateBtn = new JButton("Generate Report");
        generateBtn.setBackground(new Color(20, 30, 48));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setFont(new Font("Arial", Font.BOLD, 13));
        generateBtn.setFocusPainted(false);

        generateBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("============================================\n");
            sb.append("    OIL COMPANY - EMPLOYEE REPORT    \n");
            sb.append("============================================\n\n");

            int engineers = 0, managers = 0, workers = 0;
            double totalSalary = 0;

            // Count by role
            for (Employee emp : employeeList) {
                if (emp instanceof Engineer) engineers++;
                else if (emp instanceof Manager) managers++;
                else if (emp instanceof Worker) workers++;
                totalSalary += emp.calculateSalary();
            }

            sb.append("Total Employees   : ").append(employeeList.size()).append("\n");
            sb.append("  Engineers       : ").append(engineers).append("\n");
            sb.append("  Managers        : ").append(managers).append("\n");
            sb.append("  Workers         : ").append(workers).append("\n\n");
            sb.append(String.format("Total Monthly Salary Expense: PKR %.2f%n", totalSalary));
            sb.append(String.format("Average Salary Per Employee : PKR %.2f%n",
                employeeList.isEmpty() ? 0 : totalSalary / employeeList.size()));

            sb.append("\n--------------------------------------------\n");
            sb.append("SALARY BREAKDOWN BY EMPLOYEE:\n");
            sb.append("--------------------------------------------\n");
            for (Employee emp : employeeList) {
                sb.append(String.format("%-6s %-20s %-10s PKR %.2f%n",
                    emp.getEmployeeId(), emp.getName(), emp.getRole(), emp.calculateSalary()));
            }
            sb.append("\n============================================\n");

            reportArea.setText(sb.toString());
        });

        panel.add(heading, BorderLayout.NORTH);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        panel.add(generateBtn, BorderLayout.SOUTH);

        return panel;
    }

    // Exit application
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }
}

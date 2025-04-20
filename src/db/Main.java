import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try{
            Connection conn = DBConnection.getConnection();
            Employeedao dao = new Employeedao(conn);

            Employee newemp = new Employee();
            newemp.setEmpId(1000);
            newemp.setFname("Lily");
            newemp.setLname("Okoye");
            newemp.setemail("lily.okoye@gmail.com");
            newemp.setSSN("999-99-9999");
            newemp.setSalary(62000.00);
            newemp.setHireDate("2025-04-19");

        if (dao.addEmployee(newemp)) {
            System.out.println("Employee added successfully");
        } else {
            System.out.println("Failed to add employee.");
        }
        newemp.setSalary(68000.00);
        if (dao.updateEmployee(newemp)) {
            System.out.println("Employee updated successfully");
        } else{
            System.out.println("Failed to update employee");
        }

        Employee retrieved = dao.getemployeebyId(1000);
        if (retrieved != null) {
            System.out.println(retrieved.getFname()+" "+ retrieved.getLame()+ "Retrieved "+ ",Salary: $"+ retrieved.getSalary());
        } else{
            System.out.println("Employee not found");
        }

        if (dao.deleteEmployee(1000)) {
            System.out.println("Employee deleted successfully");
        } else{
            System.out.println("Failed to delete employee");
        }
        DBConnection.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
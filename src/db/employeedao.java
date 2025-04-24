package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Employeedao {
    private Connection connection;

    public Employeedao(Connection connection) {
        this.connection = connection;
    }

    public Employee getemployeebyId(int empid) throws SQLException {
        String query = "SELECT * FROM employees WHERE empid = ?";
        PreparedStatement stmt = connection.prepareStatement(query);  
        stmt.setInt(1, empid);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return mapRowEmplyee(rs);
        }
        return null;
    }

    public List<Employee> searchbyName(String name) throws SQLException {
        String query = "SELECT * FROM employees WHERE Fname LIKE ? OR Lname LIKE ?";
        PreparedStatement stmt = connection.prepareStatement(query);  
        stmt.setString(1, "%" + name + "%");
        stmt.setString(2, "%" + name + "%");
        ResultSet rs = stmt.executeQuery();

        List<Employee> results = new ArrayList<>();
        while (rs.next()) {
            results.add(mapRowEmplyee(rs));
        }
        return results;
    }

    private Employee mapRowEmplyee(ResultSet rs) throws SQLException {
        Employee e = new Employee();  
        e.setEmpId(rs.getInt("empid"));
        e.setFname(rs.getString("Fname"));
        e.setLname(rs.getString("Lname"));
        e.setemail(rs.getString("email"));
        e.setSSN(rs.getString("SSN"));
        e.setSalary(rs.getDouble("Salary"));
        e.setHireDate(rs.getString("HireDate"));  
        return e;
    }

    public boolean addEmployee(Employee e) throws SQLException {
        String query = "INSERT INTO employees (empid, Fname, Lname, email,SSN, Salary, HireDate) VALUES (?, ?, ?, ?, ?, ?,?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, e.getEmpId());
        stmt.setString(2, e.getFname());
        stmt.setString(3, e.getLame());
        stmt.setString(4, e.getemail());
        stmt.setString(5, e.getSSN());
        stmt.setDouble(6, e.getSalary());
        stmt.setString(7, e.getHireDate());

        int rows = stmt.executeUpdate();
        return rows > 0;
    }

    public boolean updateEmployee(Employee e) throws SQLException{
        String query = "UPDATE employees SET Fname = ?, Lname=?, email=?, SSN=?, Salary=?, HireDate=? WHERE empid =?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, e.getFname());
        stmt.setString(2, e.getLame());
        stmt.setString(3, e.getemail());
        stmt.setString(4, e.getSSN());
        stmt.setDouble(5, e.getSalary());
        stmt.setString(6, e.getHireDate()); 
        stmt.setInt(7, e.getEmpId());

        int rows = stmt.executeUpdate();
        return rows > 0;
    }

    public boolean deleteEmployee(int empid) throws SQLException {
        String query = "DELETE FROM employees WHERE empid = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, empid);
    
        int rows = stmt.executeUpdate();
        return rows > 0;
    }
    
}

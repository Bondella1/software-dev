
import java.sql.Connection;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class WebServer {
    public static void main(String[] args) {
        port(8080);

        // Serve your static HTML/CSS
        staticFiles.externalLocation("frontend");

        get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });

        
        // CREATE: handle form submission from add.html
        post("/create", (req, res) -> {
            // pull fieldrs from the form
            Employee emp = new Employee();
            emp.setFname(req.queryParams("fname"));
            emp.setLname(req.queryParams("lname"));
            emp.setemail(req.queryParams("email"));
            emp.setSSN(req.queryParams("ssn"));
            emp.setSalary(Double.parseDouble(req.queryParams("salary")));
            emp.setHireDate(req.queryParams("hireDate"));

            try (Connection conn = DBConnection.getConnection()) {
                new Employeedao(conn).addEmployee(emp);
            }
            // redirect back to a confirmation or to the list page
            res.redirect("/admin_dashboard.html");
            return "";
        });

        // LIST: dynamically generate a plain-HTML list
        get("/doList", (req, res) -> {
            res.type("text/html");
            StringBuilder html = new StringBuilder("<h1>All employees</h1><ul>");
            try (Connection conn = DBConnection.getConnection()) {
                List<Employee> all = new Employeedao(conn).getAllEmployees();
                for (Employee e : all) {
                    html.append("<li>")
                        .append(e.getFname()).append(" ")
                        .append(e.getLname())
                        .append(" &mdash; $").append(e.getSalary())
                        .append("</li>");
                }
            }
            html.append("</ul><p><a href=\"index.html\">Home</a></p>");
            return html.toString();
        });

        get("/list", (req,res) ->{
            res.type("text/html");
            List<Employee> employees;
            try (Connection conn = DBConnection.getConnection()) {
                employees = new Employeedao(conn).getAllEmployees();
            }
            StringBuilder html = new StringBuilder()
            .append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>All Employees</title></head><body>")
              .append("<h1>All Employees</h1>")
              .append("<table border='1'><tr>")
              .append("<th>ID</th><th>First</th><th>Last</th><th>Email</th><th>Salary</th><th>Hire Date</th>")
              .append("</tr>");
            for (Employee e: employees) {
                html.append("<tr>")
                .append("<td>").append(e.getEmpId()).append("</td>")
                .append("<td>").append(e.getFname()).append("</td>")
                .append("<td>").append(e.getLname()).append("</td>")
                .append("<td>").append(e.getemail()).append("</td>")
                .append("<td>$").append(e.getSalary()).append("</td>")
                .append("<td>").append(e.getHireDate()).append("</td>")
                .append("</tr>");
            }
            html.append("</table>")
            .append("<p><a href='index.html'>← Home</a></p>")
            .append("</body></html>");
            return html.toString();
        });

        get("/search", (req, res)-> {
            res.type("text/html");
            String idParam = req.queryParams("empid");
            String nameParam = req.queryParams("fullname");
            List<Employee> results;

            try (Connection conn = DBConnection.getConnection()) {
                Employeedao dao = new Employeedao(conn);
                if (idParam != null && !idParam.isBlank()) {
                    Employee e = dao.getemployeebyId(Integer.parseInt(idParam));
                    results = e == null ? List.of(): List.of(e);
                } else {
                    results= dao.searchbyName(nameParam ==null ?"": nameParam);
                }
            }
            StringBuilder html = new StringBuilder()
            .append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Search Results</title></head><body>")
            .append("<h1>Search Results</h1>")
            .append("<table border='1'><tr>")
            .append("<th>Emp ID</th><th>Name</th><th>Email</th><th>Salary</th><th>Hire Date</th><th>Actions</th>")
            .append("</tr>");

            for (Employee e: results) {
                html.append("<tr>")
                .append("<td>").append(e.getEmpId()).append("</td>")
                .append("<td>").append(e.getFname()).append(" ").append(e.getLname()).append("</td>")
                .append("<td>").append(e.getemail()).append("</td>")
                .append("<td>$").append(e.getSalary()).append("</td>")
                .append("<td>").append(e.getHireDate()).append("</td>")
                .append("<td>")
                    .append("<a href=\"/edit?empid=").append(e.getEmpId()).append("\">Edit</a> | ")
                    .append("<a href=\"/delete?empid=").append(e.getEmpId()).append("\">Delete</a>")
                .append("</td>")
                .append("</tr>");
            }
            html.append("</table>")
            .append("<p><a href=\"/admin_dashboard.html\">← Dashboard</a></p>")
            .append("</body></html>");

            return html.toString();
        });

        post("/update", (req, res)-> {
            Employee e = new Employee();
            e.setEmpId(  Integer.parseInt(req.queryParams("empid")) );
            e.setFname(  req.queryParams("fname") );
            e.setLname(  req.queryParams("lname") );
            e.setemail(  req.queryParams("email") );
            e.setSSN(    req.queryParams("ssn") );
            e.setSalary( Double.parseDouble(req.queryParams("salary")) );
            e.setHireDate(req.queryParams("hireDate"));

            try (Connection conn = DBConnection.getConnection()) {
                new Employeedao(conn).updateEmployee(e);
            }
            res.redirect("/list");
            return null;
        });

        get("/delete", (req, res) -> {
            int id = Integer.parseInt(req.queryParams("empid"));
            try (Connection conn = DBConnection.getConnection()) {
                Employeedao dao = new Employeedao(conn);
                if (!dao.deleteEmployee(id)) {
                    return  "Employee not found";
                }
            }
            res.redirect("/list");
            return null;
        });

        post("/login", (req, res)-> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            String adminUsername = "admin";
            String adminPassword = "Admin@123";
            String employeeUsername = "employee";
            String employeePassword = "employee123";

            if (username.equals(adminUsername) && password.equals(adminPassword)) {
                req.session().attribute("role", "admin");
                res.redirect("/admin_dashboard.html");
                return null;
            } else if (username.equals(employeeUsername) && password.equals(employeePassword)){
                req.session().attribute("role", "employee");
                res.status(401);
                return null;
            } else {
                return "<h2>Invalid credentials! Please try again.</h2>";
            }
        });
        
        get("/admin_dashboard", (req, res) -> {
            String role =  req.session().attribute("role");
            if ("admin".equals(role)) {
                return "<h1>Welcome to the Admin Dashboard</h1><p>Admin responsibilities...</p>";
            } else {
                res.redirect("/login");
                return null;
            }
        });

        get("/employee_dashboard", (req, res) -> {
            String role = req.session().attribute("role");
            if ("employee".equals(role)) {
                return "<h1>Welcome to the Employee Dashboard</h1><p>Employee responsibilities...</p>";
            } else {
                res.redirect("/login"); 
                return null;
            }
        });

        get("/logout", (req, res) -> {
            req.session().invalidate();  
            res.redirect("/login");  
            return null;
        });
    }
}

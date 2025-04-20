public class Employee{
    private int empid;
    private String Fname;
    private String Lname;
    public String email;
    private String SSN;
    private double Salary;
    private String HireDate;

    public int getEmpId() {return empid;}
    public void setEmpId(int empid) {this.empid= empid;}

    public String getFname() {return  Fname;}
    public void setFname(String Fname) {this.Fname =Fname;}

    public String getLame() {return Lname;}
    public void setLname(String Lname) {this.Lname =Lname; }

    public String getemail() {return email; }
    public void setemail(String email) {this.email= email;}

    public String getSSN() {return SSN;}
    public void setSSN(String SSN) {this.SSN =SSN;}

    public double getSalary() {return Salary;}
    public void setSalary(double Salary) {this.Salary = Salary;}

    public String getHireDate() {return HireDate;}
    public void setHireDate(String HireDate) {this.HireDate =HireDate;}
}
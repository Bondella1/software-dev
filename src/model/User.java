package model;

public class User {
    private Integer empId;
    private Boolean isAdmin;

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "User{empId = " + empId + " isAdmin = " + isAdmin + "}";
    }
}

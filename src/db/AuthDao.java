import java.sql.*;

import model.Auth;
import model.User;

public class AuthDao {

	public AuthDao() {

	}

    public User validateUser(Auth auth, Connection myConn) {
        User user = null;
        String sqlcommand1 = "SELECT user_id, is_admin" +
                 " FROM user" +
                 " WHERE username = '" + auth.getUsername() + "' AND password = '" + auth.getPassword() + "'";

        try {
            Statement myStmt = myConn.createStatement();
            ResultSet myRS1 = myStmt.executeQuery(sqlcommand1);

            if(!myRS1.next()) {
                return user;
            }
            else {
                user = new User();
                user.setEmpId(myRS1.getInt("user_id"));
                user.setIsAdmin(myRS1.getBoolean("is_admin"));
            }
        } catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
        return user;
                 
    }

}

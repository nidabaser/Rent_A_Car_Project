/**
 * @author Nida Başer
 * April 2024
 */

//import java.sql.Connection;
//import java.sql.DriverManager;
//import core.Db;

    // DAO -> Data Access Object
    // Entity
    // Business
    // View

import core.Helper;
import view.AdminView;
import view.LoginView;
public class App {
    public static void main(String[] args) {

        Helper.setTheme();
        LoginView loginView = new LoginView();

        //UserManager userManager = new UserManager();
        //AdminView adminView = new AdminView(userManager.findByLogin("admin","1234"));




    }
}

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

import business.UserManager;
import core.Db;
import view.AdminView;
import view.LoginView;

import java.sql.Connection;
import java.sql.DriverManager;
public class App {
    public static void main(String[] args) {

        // VERİTABANI BAĞLANTI KONTROLU
        // Singleton design pattern olsun diye bu işlemi core>Db sınıfında yaptık
//        try {
//            Connection connection = DriverManager.getConnection(
//                    "jdbc:postgresql://localhost:5432/rentacar",
//                    "postgres",
//                    "postgres"
//            );
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

       // Connection conn = Db.getInstance();


        //Helper.setTheme();

        UserManager userManager = new UserManager();
        AdminView adminView = new AdminView(userManager.findByLogin("admin","1234"));




    }
}

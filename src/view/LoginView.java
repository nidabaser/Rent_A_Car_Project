/**
 * @author Nida Başer
 * April 2024
 */

package view;

import business.UserManager;
import core.Helper;
import entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends Layout{
    private JPanel container;
    private JPanel w_top;
    private JButton button_login;
    private JPasswordField field_password;
    private JLabel label_password;
    private JTextField field_username;
    private JLabel label_username;
    private JLabel label_welcome2;
    private JLabel label_welcome;
    private JLabel lbl_welcome;
    private JLabel lbl_welcome2;
    private JPanel w_bottom;
    private JTextField fld_username;
    private JTextField fld_pass;
    private JButton btn_login;
    private JLabel lbl_username;
    private JLabel lbl_pass;
    private final UserManager userManager;

    public LoginView() {

        this.userManager = new UserManager();

        this.add(container);
        this.guiInitialize(500, 500);

        button_login.addActionListener(e -> {
            if (Helper.isFieldEmpty(this.field_username) || Helper.isFieldEmpty(this.field_password)) {
                Helper.showMsg("Please fill in all fields !");
            } else {
                User loginUser = this.userManager.findByLogin(this.field_username.getText(), this.field_password.getText());
                if (loginUser == null) {
                    Helper.showMsg("User not found");
                } else {
                    AdminView adminView = new AdminView(loginUser);
                    dispose();
                }
            }


        });

//        this.userManager = new UserManager();
//        this.add(container);
//        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        this.setTitle("RENT A CAR");
//        this.setSize(400,400);
//        this.setLocation(Helper.getLocationPoint("x", this.getSize()), Helper.getLocationPoint("y", this.getSize()));
//        this.setVisible(true);
//
//        btn_login.addActionListener(e -> {
//            JTextField[] checkFieldList = {this.fld_username, this.fld_pass};
//            if (Helper.isFieldListEmpty(checkFieldList)) {
//                Helper.showMsg("fill");
//            } else {
//                User loginUser = this.userManager.findByLogin(this.fld_username.getText(), this.fld_pass.getText());
//                if (loginUser == null) {
//                    Helper.showMsg("notFound");
//                } else {
//                    System.out.println(loginUser.toString());
//                }
//            }
//        });


    }
}

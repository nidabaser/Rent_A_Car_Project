/**
 * @author Nida Başer
 * April 2024
 */

package core;

import javax.swing.*;

public class Helper {

    public static void setTheme() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static boolean isFieldEmpty (JTextField field) {
        return field.getText().trim().isEmpty();
    }

    public static void showMsg(String message) {
        JOptionPane.showMessageDialog(null,
                message,
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String string) {
        optionPaneTR();
        String message;
        if (string.equals("sure")) {
            message = "Are you sure ?";
        } else {
            message = string;
        }
        return JOptionPane.showConfirmDialog(null,message,"Confirm Delete", JOptionPane.YES_NO_OPTION) == 0;
    }


    public static void optionPaneTR() {
        UIManager.put("OptionPane.okButtonText" , "OK");
        UIManager.put("OptionPane.yesButtonText" , "Yes");
        UIManager.put("OptionPane.noButtonText" , "No");
    }

    public static boolean isFieldListEmpty(JTextField[] jTextFields) {
        for (JTextField field : jTextFields) {
            if (isFieldEmpty(field)) {
                return true;
            }
        }
        return false;
    }



//    public static void showMsg(String str) {
//        String msg;
//        String title;
//
//        switch (str) {
//            case "fill":
//                msg = "Please fill all areas ! ";
//                title = "Error";
//                break;
//            case "done":
//                msg = "Successfully Done !";
//                title = "Result";
//                break;
//            case "notFound":
//                msg = "User is not found";
//                title = "Not Found";
//                break;
//            default:
//                msg = str;
//                title = "Message";
//        }
//        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    public static boolean isFieldEmpty(JTextField field) {
//        return field.getText().trim().isEmpty();
//    }
//
//    public static boolean isFieldListEmpty(JTextField[] fieldList) {
//        for (JTextField field : fieldList) {
//            if (isFieldEmpty(field))
//                return true;
//        }
//        return false;
//    }
//
//    public static void showMessage(String message) {
//        JOptionPane.showMessageDialog(null,
//                message,
//                "Info",
//                JOptionPane.INFORMATION_MESSAGE);
//    }
//
//
//    public static int getLocationPoint(String type, Dimension size) {
//        return switch (type) {
//            case "x" -> (Toolkit.getDefaultToolkit().getScreenSize().width - size.width) / 2;
//            case "y" -> (Toolkit.getDefaultToolkit().getScreenSize().height - size.height) / 2;
//            default -> 0;
//        };
//    }

}
/**
 * @author Nida Başer
 * April 2024
 */
package view;

import business.BookManager;
import business.BrandManager;
import business.CarManager;
import business.ModelManager;
import core.ComboItem;
import core.Helper;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.ArrayList;

public class AdminView extends Layout {
    private JPanel container;
    private JLabel label_welcome;
    private JTabbedPane panel_top;
    private JButton button_logout;
    private JPanel panel_brand;
    private JScrollPane scroll_brand;
    private JTable table_brand;
    private JScrollPane scroll_model;
    private JTable table_model;
    private JTable table_car;
    private JComboBox<ComboItem> cmb_s_model_brand;
    private JComboBox<Model.Type> cmb_s_model_type;
    private JComboBox<Model.Fuel> cmb_s_model_fuel;
    private JComboBox<Model.Gear> cmb_s_model_gear;
    private JButton btn_search_model;
    private JButton button_cancel_model;
    private JPanel panel_car;
    private JScrollPane scroll_car;
    private JPanel panel_model;
    private JPanel panel_booking;
    private JScrollPane scrl_booking;
    private JTable tbl_booking;
    private JPanel pnl_booking_search;
    private JComboBox cmb_booking_type;
    private JComboBox cmb_booking_fuel;
    private JComboBox cmb_booking_gear;
    private JFormattedTextField fld_strt_date;
    private JFormattedTextField fld_fnsh_date;
    private javax.swing.JLabel JLabel;
    private JButton btn_booking_search;
    private JButton btn_cncl_booking;
    private JPanel pnl_booked;
    private JTable tbl_booked;
    private JScrollPane scrl_booked;
    private JComboBox cmb_booked_plate;
    private JButton btn_booked_search;
    private JButton btn_booked_clear;

    private User user;

    private DefaultTableModel tableModel_brand;
    private DefaultTableModel tableModel_model;
    private DefaultTableModel tableModel_car;
    private DefaultTableModel tableModel_booking;
    private DefaultTableModel tableModel_booked;

    private BrandManager brandManager;
    private ModelManager modelManager;
    private CarManager carManager;
    private BookManager bookManager;

    private JPopupMenu brand_menu;
    private JPopupMenu model_menu;
    private JPopupMenu car_menu;
    private JPopupMenu booking_menu;
    private JPopupMenu booked_menu;

    private Object[] col_model;
    private Object[] col_car;
    private Object[] col_booked_list;

    public AdminView(User user) {
        this.tableModel_brand = new DefaultTableModel();
        this.tableModel_model = new DefaultTableModel();
        this.tableModel_car = new DefaultTableModel();
        this.tableModel_booking = new DefaultTableModel();
        this.tableModel_booked = new DefaultTableModel();

        this.brandManager = new BrandManager();
        this.modelManager = new ModelManager();
        this.carManager = new CarManager();
        this.bookManager = new BookManager();

        this.add(container);
        this.user = user;
        this.guiInitialize(1000, 500);

        if (this.user == null) {
            dispose();
        }
        this.label_welcome.setText(" Welcome " + this.user.getUsername());

        //General Code
        loadComponent();

        //Car Tab Menu
        loadCarTable();
        loadCarComponent();

        // Brand Tab Menu
        loadBrandTable();
        loadBrandComponent();

        // Model Tab Menu
        loadModelTable(null);
        loadModelComponent();
        loadModelFilter();

        // Booking tab Menu
        loadBookingTable(null);
        loadBookingComponent();
        loadBookingFilter();

        // Booked tab menu
        loadBookingInfoTable(null);
        loadBookedComponent();
        loadBookedFilterPlate(this.bookManager.findAll());

    }

    public void loadComponent(){
        this.button_logout.addActionListener(e -> {
            dispose();
            LoginView loginView = new LoginView();
        });
    };

    public void loadBookingComponent(){
        tableRowSelect(this.tbl_booking);
        this.booking_menu = new JPopupMenu();
        this.booking_menu.add("Make Reservation").addActionListener(e -> {
            int selectedCarId = this.getTableSelectedRow(this.tbl_booking, 0);
            BookView bookView = new BookView(
                    this.carManager.getById(selectedCarId),
                    this.fld_strt_date.getText(),
                    this.fld_fnsh_date.getText()
            );
            bookView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBookingTable(null);
                    loadBookingFilter();
                }
            });
        });
        this.tbl_booking.setComponentPopupMenu(booking_menu);

        btn_booking_search.addActionListener(e -> {
            ArrayList<Car> carList = this.carManager.searchForBooking(
                    fld_strt_date.getText(),
                    fld_fnsh_date.getText(),
                    (Model.Type) cmb_booking_type.getSelectedItem(),
                    (Model.Gear) cmb_booking_gear.getSelectedItem(),
                    (Model.Fuel) cmb_booking_fuel.getSelectedItem()
            );

            ArrayList<Object[]> carBookingRow = this.carManager.getForTable(this.col_car.length, carList);
            loadBookingTable(carBookingRow);
        });
        btn_cncl_booking.addActionListener(e -> {
            loadBookingFilter();
        });
    }



    private void loadBookingTable(ArrayList<Object[]> carList){
        Object[] col_booking_list = {"ID", "Brand", "Model", "Plate", "Color", "KM", "Year", "Type", "Fuel", "Gear"};
        createTable(this.tableModel_booking, this.tbl_booking, col_booking_list, carList);
    }

    public void loadBookingFilter() {
        this.cmb_booking_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.cmb_booking_type.setSelectedItem(null);
        this.cmb_booking_gear.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
        this.cmb_booking_gear.setSelectedItem(null);
        this.cmb_booking_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_booking_fuel.setSelectedItem(null);
    }

    public void loadBookedComponent() {
        tableRowSelect(this.tbl_booked);

        this.booked_menu = new JPopupMenu();

        this.booked_menu.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")){
                int selectBooktId = this.getTableSelectedRow(tbl_booked,0);
                if (this.bookManager.delete(selectBooktId)){
                    loadBookingInfoTable(null);
                }else{
                    Helper.showMsg("error");
                }
            }
        });
        this.tbl_booked.setComponentPopupMenu(booked_menu);

        this.btn_booked_search.addActionListener(e -> {
            ComboItem selectedPlate = (ComboItem) this.cmb_booked_plate.getSelectedItem();
            ArrayList<Book> filteredByPlateBooks = new ArrayList<>();
            if (selectedPlate != null) {
                for (Book book : this.bookManager.findAll()) {
                    if (book.getCar().getPlate().equals(selectedPlate.getValue())) {
                        filteredByPlateBooks.add(book);
                    }
                }
            }

            ArrayList<Object[]> bookRowListBySearch = this.bookManager.getForTable(this.col_booked_list.length, filteredByPlateBooks);
            loadBookingInfoTable(bookRowListBySearch);
        });

        btn_booked_clear.addActionListener(e -> {
            this.cmb_booked_plate.setSelectedItem(null);
            loadBookingInfoTable(null);
        });
    }

    public void loadBookingInfoTable (ArrayList<Object[]> bookingInfoList ) {
        col_booked_list = new Object[]{"ID", "Car ID", "Customer Name", "Customer ID", "Customer Phone Number", "Customer Mail", "Start Date", "Finish Date", "Booking Price", "Note", "State"};

        if (bookingInfoList == null) {
            bookingInfoList = this.bookManager.getForTable(col_booked_list.length,this.bookManager.findAll());
        }
        this.createTable(this.tableModel_booked, this.tbl_booked, col_booked_list, bookingInfoList);
    }

    public void loadBookedFilterPlate(ArrayList<Book> books) {
        this.cmb_booked_plate.removeAllItems();
        ArrayList<String> plateList = new ArrayList<>();
        for (Book obj :books) {
            String plate = obj.getCar().getPlate();
            if(!plateList.contains(plate)){
                this.cmb_booked_plate.addItem(new ComboItem(obj.getId(), obj.getCar().getPlate()));
                plateList.add(plate);
            }
        }
        this.cmb_booked_plate.setSelectedItem(null);
    }


    private void loadCarComponent() {
        tableRowSelect(this.table_car);
        this.car_menu = new JPopupMenu();

        this.car_menu.add("New").addActionListener(e -> {
            CarView carView = new CarView(new Car());
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCarTable();
                }
            });
        });
        this.car_menu.add("Update").addActionListener(e -> {
            int selectModelId = this.getTableSelectedRow(table_car, 0);
            CarView carView = new CarView(this.carManager.getById(selectModelId));
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCarTable();
                }
            });
            loadBookingTable(null);
        });
        this.car_menu.add("Delete").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectCarId = this.getTableSelectedRow(table_car, 0);
                if (this.carManager.delete(selectCarId)) {
                    Helper.showMsg("Done");
                    loadCarTable();
                } else {
                    Helper.showMsg("Error");
                }
            }
        });
        this.table_car.setComponentPopupMenu(car_menu);
    }

    public void loadCarTable(){
        col_car = new Object[]{"ID", "Brand", "Model", "Plate", "Color", "KM", "Year", "Type", "Fuel", "Gear"};
        ArrayList<Object[]> carList = this.carManager.getForTable(col_car.length, this.carManager.findAll());
        createTable(this.tableModel_car, this.table_car, col_car, carList);
    }



    private void loadModelComponent() {
        tableRowSelect(this.table_model);

        this.model_menu = new JPopupMenu();

        this.model_menu.add("New").addActionListener(e -> {
            ModelView modelView = new ModelView(new Model());
            modelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadModelTable(null);
                }
            });

        });

        this.model_menu.add("Update").addActionListener(e -> {
            int selectModelId = this.getTableSelectedRow(table_model, 0);
            ModelView modelView = new ModelView(this.modelManager.getById(selectModelId));
            modelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadModelTable(null);
                    loadBookingTable(null);
                }
            });
        });

        this.model_menu.add("Delete").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectModelId = this.getTableSelectedRow(table_model, 0);
                if (this.modelManager.delete(selectModelId)) {
                    Helper.showMsg("Succeed !");
                    loadModelTable(null);
                } else {
                    Helper.showMsg("Error !");
                }
            }
        });

        this.table_model.setComponentPopupMenu(this.model_menu);

        this.btn_search_model.addActionListener(e -> {
            ComboItem selectedBrand = (ComboItem) this.cmb_s_model_brand.getSelectedItem();
            int brandId = 0;
            if (selectedBrand != null) {
                brandId = selectedBrand.getKey();
            }
            ArrayList<Model> modelListBySearch = this.modelManager.searchForTable(
                    brandId,
                    (Model.Fuel) cmb_s_model_fuel.getSelectedItem(),
                    (Model.Type) cmb_s_model_type.getSelectedItem(),
                    (Model.Gear) cmb_s_model_gear.getSelectedItem()
            );


            ArrayList<Object[]> modelRowListBySearch = this.modelManager.getForTable(this.col_model.length, modelListBySearch);
            loadModelTable(modelRowListBySearch);
        });

        this.button_cancel_model.addActionListener(e -> {
            this.cmb_s_model_type.setSelectedItem(null);
            this.cmb_s_model_gear.setSelectedItem(null);
            this.cmb_s_model_fuel.setSelectedItem(null);
            this.cmb_s_model_brand.setSelectedItem(null);
            loadModelTable(null);
        });
    }




    public void loadModelTable(ArrayList<Object[]> modelList) {
        this.col_model = new Object[]{"Model ID", "Brand", "Name", "Type", "Year", "Fuel", "Gear"};
        if (modelList == null) {
            modelList = this.modelManager.getForTable(this.col_model.length, this.modelManager.findAll());
        }
        createTable(tableModel_model, this.table_model, col_model, modelList);

    }

    public void loadModelTable() {
        Object[] column_model = {"Model ID", "Brand", "Name", "Type", "Year", "Fuel", "Gear"};
        ArrayList<Object[]> modelList = this.modelManager.getForTable(column_model.length, this.modelManager.findAll());
        createTable(this.tableModel_model, this.table_model, column_model, modelList);
    }





    public void loadBrandTable() {
        Object[] column_brand = {"Brand ID", "Brand Name"};
        ArrayList<Object[]> brandList = this.brandManager.getForTable(column_brand.length);
        this.createTable(this.tableModel_brand, this.table_brand, column_brand, brandList);
    }




    public void loadBrandComponent() {
        tableRowSelect(this.table_brand);
        this.brand_menu = new JPopupMenu();

        this.brand_menu.add("New").addActionListener(e -> {
            BrandView brandView = new BrandView(null);
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                }
            });
        });

        this.brand_menu.add("Update").addActionListener(e -> {
            int selectBrandId = this.getTableSelectedRow(table_brand, 0);
            BrandView brandView = new BrandView(this.brandManager.getById(selectBrandId));
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                    loadBookingTable(null);
                }
            });
        });
        this.brand_menu.add("Delete").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectBrandId = this.getTableSelectedRow(table_brand, 0);
                if (this.brandManager.delete(selectBrandId)) {
                    Helper.showMsg("Succeed !");
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                } else {
                    Helper.showMsg("Error !");
                }
            }
        });
        this.table_brand.setComponentPopupMenu(this.brand_menu);
    }

    public void loadModelFilter() {
        this.cmb_s_model_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.cmb_s_model_type.setSelectedItem(null);
        this.cmb_s_model_gear.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
        this.cmb_s_model_gear.setSelectedItem(null);
        this.cmb_s_model_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_s_model_fuel.setSelectedItem(null);
        loadModelFilterBrand();

    }





    public void loadModelFilterBrand() {
        this.cmb_s_model_brand.removeAllItems();
        for (Brand obj : brandManager.findAll()) {
            this.cmb_s_model_brand.addItem(new ComboItem(obj.getId(), obj.getName()));
        }
        this.cmb_s_model_brand.setSelectedItem(null);
    }

    private void createUIComponents() throws ParseException {
        // TODO: place custom component creation code here
        this.fld_strt_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.fld_strt_date.setText("08/04/2024");
        this.fld_fnsh_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.fld_fnsh_date.setText("14/04/2024");
    }
}
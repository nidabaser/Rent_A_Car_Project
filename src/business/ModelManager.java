/**
 * @author Nida Başer
 * April 2024
 */

package business;

import core.Helper;
import dao.ModelDao;
import entity.Brand;
import entity.Model;

import java.util.ArrayList;
import java.util.Objects;

public class ModelManager {
    private final ModelDao modelDao = new ModelDao();

    public Model getById(int id) {
        return this.modelDao.getById(id);
    }

    public ArrayList<Model> findAll() {
        return this.modelDao.findAll();
    }

    public ArrayList<Object[]> getForTable(int size, ArrayList<Model> modelList) {
        ArrayList<Object[]> modelObjList = new ArrayList<>();
        for (Model object : modelList) {
            int i = 0;
            Object[] rowObject = new Object[size];
            rowObject[i++] = object.getId();
            rowObject[i++] = object.getBrand().getName();
            rowObject[i++] = object.getName();
            rowObject[i++] = object.getType();
            rowObject[i++] = object.getYear();
            rowObject[i++] = object.getFuel();
            rowObject[i++] = object.getGear();
            modelObjList.add(rowObject);
        }
        return modelObjList;
    }

    public boolean save(Model model) {
        if (this.getById(model.getId()) != null) {
            Helper.showMsg("Error: Model with ID " + model.getId() + " already exists.");
            return false;
        }
        return this.modelDao.save(model);
    }

    public boolean update(Model model) {
        if (this.getById(model.getId()) == null) {
            Helper.showMsg(model.getId() + " This model not found !");
            return false;
        }
        return this.modelDao.update(model);
    }

    public boolean delete(int id) {
        if (this.getById(id) == null) {
            Helper.showMsg(id + " This model not found !");
            return false;
        }
        return this.modelDao.delete(id);
    }

    public ArrayList<Model> getByListBrandId(int brandId) {
        return this.modelDao.getByListBrandId(brandId);
    }

    public ArrayList<Model> searchForTable(int brand_id, Model.Fuel fuel, Model.Type type, Model.Gear gear) {
        String select = "SELECT * FROM public.model";
        ArrayList<String> whereList = new ArrayList<>();

        if (brand_id != 0) {
            whereList.add("model_brand_id = " + brand_id);

        }

        if (fuel != null) {
            whereList.add("model_fuel = '" + fuel.toString() + "'");
        }
        if (gear != null) {
            whereList.add("model_gear = '" + gear.toString() + "'");
        }

        if (type != null) {
            whereList.add("model_type = '" + type.toString() + "'");
        }

        String whereStr = String.join(" AND ", whereList);
        String query = select;
        if (!whereStr.isEmpty()) {
            query += " WHERE " + whereStr;
        }
        return this.modelDao.selectByQuery(query);
    }


}
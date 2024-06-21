package com.mibodega.mystore.models.Responses;

import java.util.ArrayList;

public class CategoryResponseWithProducts {
    private String _id;
    private String name;
    private ArrayList<String> subcategories = new ArrayList<>();

    public CategoryResponseWithProducts(String _id, String name, ArrayList<String> subcategories) {
        this._id = _id;
        this.name = name;
        this.subcategories = subcategories;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<String> subcategories) {
        this.subcategories = subcategories;
    }
}

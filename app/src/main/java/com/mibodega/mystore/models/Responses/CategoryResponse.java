package com.mibodega.mystore.models.Responses;

import java.util.ArrayList;

public class CategoryResponse {
    private String _id;
    private String name;
    private String createdAt;
    private String updateAt;
    private ArrayList<SubCategoryResponse> subcategories = new ArrayList<>();
    private int __v;

    public CategoryResponse(String _id, String name, String createdAt, String updateAt, ArrayList<SubCategoryResponse> subcategories) {
        this._id = _id;
        this.name = name;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public ArrayList<SubCategoryResponse> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<SubCategoryResponse> subcategories) {
        this.subcategories = subcategories;
    }
}

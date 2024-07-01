package com.mibodega.mystore.models.Responses;

public class ProductResponseSupplier {
    private String _id;
    private String name;
    private double cost;
    private String code;
    private double price;
    private int stock;
    private String image_url;
    private int sales;
    private boolean weight;
    private CategoryProduct category;
    private SubCategoryResponse subcategory;
    private String shop;
    private String createdAt;
    private String updatedAt;
    private int __v;

    public ProductResponseSupplier(String _id, String name, double cost, String code, double price, int stock, String image_url, int sales, boolean weight, CategoryProduct category, SubCategoryResponse subcategory, String shop, String createdAt, String updatedAt) {
        this._id = _id;
        this.name = name;
        this.cost = cost;
        this.code = code;
        this.price = price;
        this.stock = stock;
        this.image_url = image_url;
        this.sales = sales;
        this.weight = weight;
        this.category = category;
        this.subcategory = subcategory;
        this.shop = shop;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public boolean isWeight() {
        return weight;
    }

    public void setWeight(boolean weight) {
        this.weight = weight;
    }

    public CategoryProduct getCategory() {
        return category;
    }

    public void setCategory(CategoryProduct category) {
        this.category = category;
    }

    public SubCategoryResponse getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(SubCategoryResponse subcategory) {
        this.subcategory = subcategory;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

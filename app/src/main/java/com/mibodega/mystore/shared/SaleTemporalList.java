package com.mibodega.mystore.shared;

import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.SaleResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SaleTemporalList {
    private static ArrayList<ProductResponse> arrayList = new ArrayList<>();
    private static Map<String,Double> mapAmountProduct = new HashMap<>();
    private static Double totalPrice=0.0;

    private static SaleResponse saleCurrent=null;

    public SaleResponse getSaleCurrent() {
        return saleCurrent;
    }

    public void setSaleCurrent(SaleResponse saleCurrent) {
        SaleTemporalList.saleCurrent = saleCurrent;
    }


    public ArrayList<ProductResponse> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<ProductResponse> arrayList) {
        SaleTemporalList.arrayList = arrayList;
    }

    public Map<String, Double> getMapAmountProduct() {
        return mapAmountProduct;
    }

    public static void setMapAmountProduct(Map<String, Double> mapAmountProduct) {
        SaleTemporalList.mapAmountProduct = mapAmountProduct;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        SaleTemporalList.totalPrice = totalPrice;
    }
    // Función para calcular el precio total de todos los productos
    public void calculateTotalPrice() {
        totalPrice = 0.0;
        for (ProductResponse product : arrayList) {
            String productCode = product.getCode();
            Double amount = mapAmountProduct.getOrDefault(productCode, 0.0);
            totalPrice += product.getPrice() * amount;
        }
    }

    // Función para agregar un producto a la lista
    public void addProduct(ProductResponse product, Double amount) {
        String productCode = product.getCode();
        if (mapAmountProduct.containsKey(productCode)) {
            Double currentAmount = mapAmountProduct.get(productCode);
            System.out.println("actualiza old amount "+currentAmount);
            Double newAmount = currentAmount + amount;
            mapAmountProduct.put(productCode, newAmount);
            System.out.println("actualiza new amount "+newAmount);
        } else {
            System.out.println("agrega "+product.getName());
            System.out.println("agrega "+amount);
            arrayList.add(product);
            mapAmountProduct.put(productCode, amount);
        }
        calculateTotalPrice();
    }

    // Función para eliminar un producto de la lista
    public void removeProduct(ProductResponse product) {
        String productCode = product.getCode();
        if (arrayList.contains(product) && mapAmountProduct.containsKey(productCode)) {
            arrayList.remove(product);
            mapAmountProduct.remove(productCode);
            calculateTotalPrice();
        }
    }
    public void removeProductByCode(String productCode) {
        Optional<ProductResponse> productOptional = arrayList.stream()
                .filter(p -> p.getCode().equals(productCode))
                .findFirst();

        if (productOptional.isPresent()) {
            ProductResponse product = productOptional.get();
            arrayList.remove(product);
            mapAmountProduct.remove(productCode);
            calculateTotalPrice();
        }
    }




    public void updateAmountProduct(String productCode, Double newAmount) {
        if (mapAmountProduct.containsKey(productCode)) {
            mapAmountProduct.put(productCode, newAmount);
            calculateTotalPrice();
        }
    }

    public void cleanAll() {
        arrayList.clear();
        mapAmountProduct.clear();
        totalPrice = 0.0;
    }

}

package com.framework.model;

import com.google.gson.annotations.SerializedName;

public class ProductData {

    // Product Details
    @SerializedName("productCategory")
    private String productCategory;

    private String productName;
    private String brand;
    private String productCode;
    private String rewardPoints;
    private String availability;

    // Pricing Details
    private double price;
    private String currency;
    private double exTax;
    private String priceInRewardPoints;

    // Product Description
    private String productDescription;

    // Delivery Date Details
    private String deliveryDay;
    private String deliveryMonth;
    private String deliveryYear;
    private String deliveryDate;

    // Shipping Details
    private String country;
    private String region;
    private String zipcode;

    // ---------------- GETTERS ---------------- //

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public String getBrand() {
        return brand;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getRewardPoints() {
        return rewardPoints;
    }

    public String getAvailability() {
        return availability;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public double getExTax() {
        return exTax;
    }

    public String getPriceInRewardPoints() {
        return priceInRewardPoints;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getDeliveryDay() {
        return deliveryDay;
    }

    public String getDeliveryMonth() {
        return deliveryMonth;
    }

    public String getDeliveryYear() {
        return deliveryYear;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getZipcode() {
        return zipcode;
    }

    @Override
    public String toString() {
        return "ProductData {" +
                "category='" + productCategory + '\'' +
                ", name='" + productName + '\'' +
                ", brand='" + brand + '\'' +
                ", code='" + productCode + '\'' +
                ", rewardPoints='" + rewardPoints + '\'' +
                ", availability='" + availability + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", exTax=" + exTax +
                ", priceInRewardPoints='" + priceInRewardPoints + '\'' +
                ", description='" + productDescription + '\'' +
                ", delivery=" + deliveryDay + "-" + deliveryMonth + "-" + deliveryYear +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}

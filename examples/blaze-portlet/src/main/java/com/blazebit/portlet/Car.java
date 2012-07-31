package com.blazebit.portlet;

import java.io.Serializable;

public class Car implements Serializable {

    public String model;
    public int year;
    public String manufacturer;
    public String color;
    public int price;

    public Car(String model, int year, String manufacturer, String color) {
        this.model = model;
        this.year = year;
        this.manufacturer = manufacturer;
        this.color = color;
    }

    public Car(String model, int year, String manufacturer, String color, int price) {
        this.model = model;
        this.year = year;
        this.manufacturer = manufacturer;
        this.color = color;
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Car)) {
            return false;
        }

        Car compare = (Car) obj;

        return compare.model.equals(this.model);
    }

    @Override
    public int hashCode() {
        int hash = 1;

        return hash * 31 + model.hashCode();
    }

    @Override
    public String toString() {
        return "Car{" + "model=" + model + ", year=" + year + ", manufacturer=" + manufacturer + ", color=" + color + ", price=" + price + '}';
    }
}

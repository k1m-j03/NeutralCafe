package com.example.neutralcafe;

import com.google.gson.annotations.SerializedName;

public class ReservationData {
    private String customerName;        // Maps to "CustomerName"
    private String customerPhoneNumber; // Maps to "CustomerPhoneNumber"
    private String meal;                // Maps to "Meal"
    private String seatingArea;         // Maps to "SeatingArea"
    private int tableSize;              // Maps to "TableSize"
    private String date;                // Maps to "Date"

    // Constructor
    public ReservationData(String customerName, String customerPhoneNumber, String date, String meal, String seatingArea, int tableSize) {
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.meal = meal;
        this.seatingArea = seatingArea;
        this.tableSize = tableSize;
        this.date = date;
    }

    // Getters and Setters
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhoneNumber() { return customerPhoneNumber; }
    public void setCustomerPhoneNumber(String customerPhoneNumber) { this.customerPhoneNumber = customerPhoneNumber; }

    public String getMeal() { return meal; }
    public void setMeal(String meal) { this.meal = meal; }

    public String getSeatingArea() { return seatingArea; }
    public void setSeatingArea(String seatingArea) { this.seatingArea = seatingArea; }

    public int getTableSize() { return tableSize; }
    public void setTableSize(int tableSize) { this.tableSize = tableSize; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }


}

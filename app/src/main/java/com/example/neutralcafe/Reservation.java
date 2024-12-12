package com.example.neutralcafe;

public class Reservation {
    private int id;
    private String customerName;
    private String customerPhoneNumber;
    private String date;
    private String meal;
    private String seatingArea;
    private int tableSize;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerPhoneNumber() { return customerPhoneNumber; }
    public void setCustomerPhoneNumber(String customerPhoneNumber) { this.customerPhoneNumber = customerPhoneNumber; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getMeal() { return meal; }
    public void setMeal(String meal) { this.meal = meal; }
    public String getSeatingArea() { return seatingArea; }
    public void setSeatingArea(String seatingArea) { this.seatingArea = seatingArea; }
    public int getTableSize() { return tableSize; }
    public void setTableSize(int tableSize) { this.tableSize = tableSize; }
}

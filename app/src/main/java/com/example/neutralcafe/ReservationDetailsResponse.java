package com.example.neutralcafe;

public class ReservationDetailsResponse {
    private String reservationId;
    private String bookingDate;
    private String name;
    private String phone;
    private String date;
    private String meal;
    private String tableNumber;
    private int pax;
    private String settingArea;

    // Getters
    public String getReservationId() {
        return reservationId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDate() {
        return date;
    }

    public String getMeal() {
        return meal;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public int getPax() {
        return pax;
    }

    public String getSettingArea() {
        return settingArea;
    }
}

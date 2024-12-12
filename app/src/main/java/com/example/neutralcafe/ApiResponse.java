package com.example.neutralcafe;

public class ApiResponse {
    private int reservationId; // or `String reservationId` depending on your API
    private String message;

    public int getReservationId() {
        return reservationId;
    }

    public String getMessage() {
        return message;
    }
}


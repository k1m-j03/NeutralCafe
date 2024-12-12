package com.example.neutralcafe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReservationApi {
    @POST("Reservations")
    Call<Reservation> submitReservation(@Body ReservationData reservation);

    @GET("Reservations/{id}")
    Call<Reservation> getReservationById(@Path("id") int reservationId);

    @GET("reservations")
    Call<List<Reservation>> getAllReservations();

    @PUT("Reservations/{id}")
    Call<Reservation> updateReservation(@Path("id") int id, @Body Reservation reservation);


    @DELETE("Reservations/{id}")
    Call<Void> deleteReservation(@Path("id") int reservationId);

    // Wrapper class for the "reservation" key
    class WrappedReservation {
        private ReservationData reservation;

        public WrappedReservation(ReservationData reservation) {
            this.reservation = reservation;
        }

        public WrappedReservation(String customerName, String customerPhoneNumber, String formattedDate, String meal, String seatingArea, int pax) {
        }

        public ReservationData getReservation() {
            return reservation;
        }
    }
}

package com.example.neutralcafe;

import retrofit2.Call;

public interface MyCallback {
    void onFailure(Call<Object> call, Throwable throwable);
}

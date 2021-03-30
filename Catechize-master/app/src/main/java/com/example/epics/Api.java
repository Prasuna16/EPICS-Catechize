package com.example.epics;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("upload_file.php")
    Call<ResponsePOJO> uploadDocument(
            @Field("PDF") String encodedPDF,
            @Field("fileName") String fileName
    );

}
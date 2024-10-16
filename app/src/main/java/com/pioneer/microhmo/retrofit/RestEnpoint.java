package com.pioneer.microhmo.retrofit;

import com.pioneer.microhmo.objects.Address;
import com.pioneer.microhmo.objects.AssessmentRequest;
import com.pioneer.microhmo.objects.AssessmentResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestEnpoint {
    @Headers("Content-Type: application/json")
    @GET("/api/v1/nanai/provinces")
    Call<List<Address>> getProvinces(
            @Header("Authorization") String apiToken
    );

    @Headers("Content-Type: application/json")
    @POST("/api/v1/nanai/city")
    Call<List<Address>> getCity(
            @Body Address address,
            @Header("Authorization") String apiToken

    );

    @Headers("Content-Type: application/json")
    @POST("/api/v1/nanai/brgy")
    Call<List<Address>> getBrgy(
            @Body Address address,
            @Header("Authorization") String apiToken
    );



    @POST("v1/projects/{projectId}/assessments")
    Call<AssessmentResponse> createAssessment(
            @Path("projectId") String projectId, // This will be the project ID
            @Body AssessmentRequest request
    );



}

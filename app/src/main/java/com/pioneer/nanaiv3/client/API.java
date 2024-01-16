package com.pioneer.nanaiv3.client;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface API {

    @Multipart
    @POST("{path}/{id}/{coc}")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image ,
                                   @Path("path") String path,
                                   @Path("id") String id,
                                   @Path("coc") String coc);
}
package com.example.android.schoolfinder.notifications;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    String BASE_URL = "https://fcm.googleapis.com/";

    @Headers({"Authorization: key=AAAAtKKzHXo:APA91bEiaHa85XtkWVskDIlgIw98QiBHjezoYq-8Neqsiyj8tvZPOABsOrexdaMpuAlMswTuOo20QduugQNFd87rc6xc90qhxfR0De3ZAuI_AbYy-Ocf8WGIC7AHs9ix45hebQVA67HJ",
            "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body RequestNotification requestNotification);

    @POST("fcm/send")
    @Headers({"Authorization: key=AAAAtKKzHXo:APA91bEiaHa85XtkWVskDIlgIw98QiBHjezoYq-8Neqsiyj8tvZPOABsOrexdaMpuAlMswTuOo20QduugQNFd87rc6xc90qhxfR0De3ZAuI_AbYy-Ocf8WGIC7AHs9ix45hebQVA67HJ",
            "Content-Type:application/json"})
    @FormUrlEncoded
    Call<ResponseBody> sendNotification(@Field("to") String to, @Field("data") Config config);
}

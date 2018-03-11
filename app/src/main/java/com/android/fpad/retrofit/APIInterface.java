package com.android.fpad.retrofit;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface APIInterface {

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterList> doGetRegisterList(@Field("username") String username, @Field("email") String email,@Field("namalengkap") String namalengkap, @Field("password") String password);

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginList> doGetLoginList(@Field("email") String email, @Field("password") String password, @Field("android_id") String android_id);


    @FormUrlEncoded
    @POST("login.php")
    Call<LoginList> doGetLoginSocialList(@Field("email") String email, @Field("namalengkap") String namalengkap, @Field("android_id") String android_id);


    @FormUrlEncoded
    @POST("user.php")
    Call<List<UserList>> doGetUserList(@Field("email") String email);

    @GET("kategori.php")
    Call<List<KategoriList>> doGetKategoriList();


    @FormUrlEncoded
    @POST("getstory.php")
    Call<List<StoryList>> doGetStoryById(@Field("story_id") String story_id);

    @FormUrlEncoded
    @POST("getstory.php")
    Call<List<StoryList>> doGetStoryUserList(@Field("email") String email,@Field("status") String status);

    @FormUrlEncoded
    @POST("getstory.php")
    Call<List<StoryList>> doGetStoryUserLibrary(@Field("email") String email,@Field("type") String type);

    @FormUrlEncoded
    @POST("getstory.php")
    Call<List<StoryList>> doGetStoryType(@Field("type") String type);

    @FormUrlEncoded
    @POST("getstory.php")
    Call<List<StoryList>> doGetStoryKategori(@Field("type") String type,@Field("kategori") String kategori);

    @FormUrlEncoded
    @POST("getstory.php")
    Call<List<StoryList>> doGetStorySearch(@Field("type") String type,@Field("text") String text);

    @FormUrlEncoded
    @POST("poststory.php")
    Call<List<StoryList>> doPostStoryUserList(@Field("email") String email,@Field("title") String title,@Field("description") String description,@Field("image") String image,@Field("content") String content,@Field("status") String status);

    /*
    @Multipart
    @POST("poststory.php?drafts")
    Call<StoryRespond> uploadwithimage(@Part MultipartBody.Part file, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("content") RequestBody content, @Part("email") RequestBody email, @Part("kategori") RequestBody kategori);
   */

    @Multipart
    @POST("setting.php")
    Call<StoryRespond> simpanuser(@Part("type") RequestBody type, @Part("email") RequestBody email, @Part("value") RequestBody value);

    @Multipart
    @POST("setting.php")
    Call<StoryRespond> gantipassword(@Part("type") RequestBody type, @Part("email") RequestBody email, @Part("value1") RequestBody value1, @Part("value2") RequestBody value2);



    @Multipart
    @POST("poststory.php?drafts")
    Call<StoryRespond> upload(@Part("title") RequestBody title, @Part("description") RequestBody description, @Part("content") RequestBody content, @Part("email") RequestBody email, @Part("kategori") RequestBody kategori);

    @Multipart
    @POST("poststory.php?published")
    Call<StoryRespond> upload2(@Part("title") RequestBody title, @Part("description") RequestBody description, @Part("content") RequestBody content, @Part("email") RequestBody email, @Part("kategori") RequestBody kategori);



    @Multipart
    @POST("editstory.php")
    Call<StoryRespond> simpan(@Part("title") RequestBody title, @Part("description") RequestBody description, @Part("content") RequestBody content, @Part("email") RequestBody email, @Part("kategori") RequestBody kategori, @Part("id") RequestBody id, @Part("status") RequestBody status);

    @Multipart
    @POST("deletestory.php")
    Call<StoryRespond> delete(@Part("id") RequestBody id,@Part("uid") RequestBody uid,@Part("email") RequestBody email);

    @Multipart
    @POST("deletelibrary.php")
    Call<StoryRespond> deletelib(@Part("id") RequestBody id,@Part("email") RequestBody email);

    @Multipart
    @POST("etc.php")
    Call<StoryRespond> vote(@Part("type") RequestBody type,@Part("email") RequestBody email,@Part("id") RequestBody id);

    @Multipart
    @POST("etc.php")
    Call<StoryRespond> comment(@Part("type") RequestBody type,@Part("email") RequestBody email,@Part("comment") RequestBody comment,@Part("id") RequestBody id);

    @Multipart
    @POST("etc.php")
    Call<StoryRespond> etc(@Part("type") RequestBody type,@Part("value") RequestBody value);


    @FormUrlEncoded
    @POST("getcomment.php")
    Call<List<CommentList>> doGetCommentList(@Field("story_id") String story_id);
}

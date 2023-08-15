package vn.fpt.ph26439.lab56_retrofit_app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.fpt.ph26439.lab56_retrofit_app.Models.ListUser;
import vn.fpt.ph26439.lab56_retrofit_app.Models.User;

public interface UserInterface {
    @GET("list")
    Call<ListUser> getAllUser();
    @POST("adduser")
    Call<User> postData(@Body User data);

    @PUT("edituser/{id}")
    Call<User> editById(@Path("id") String id, @Body User data);

    @DELETE("deleteuser/{id}")
    Call<User> delById(@Path("id") String id);

}

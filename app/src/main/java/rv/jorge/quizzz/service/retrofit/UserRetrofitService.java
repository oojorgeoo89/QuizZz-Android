package rv.jorge.quizzz.service.retrofit;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rv.jorge.quizzz.model.User;

public interface UserRetrofitService {

    @GET("/api/users/login")
    Observable<User> login(@Header("Authorization") String authToken);

    @GET("/api/users/logout")
    Observable<Response<Void>> logout();

    @GET("/api/users/forgotPassword")
    Observable<Response<User>> forgotMyPassword(@Query("email") String email);

    @FormUrlEncoded
    @POST("/api/users/registration")
    Observable<Response<User>> signup(@Field("username") String username, @Field("email") String email, @Field("password") String password);

}

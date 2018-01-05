package rv.jorge.quizzz.service.retrofit;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rv.jorge.quizzz.model.User;

/**
 * Created by jorgerodriguez on 01/01/18.
 */

public interface UserRetrofitService {
    @GET(RetrofitService.URL_PREFIX + "/users/login")
    Observable<User> login();

    @GET(RetrofitService.URL_PREFIX + "/users/forgotPassword")
    Observable<Response<User>> forgotMyPassword(@Query("email") String email);
}

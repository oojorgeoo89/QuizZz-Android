package rv.jorge.quizzz.service.support;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by jorgerodriguez on 01/01/18.
 */

public class BasicAuthInterceptor implements Interceptor {

    private String authToken;

    public BasicAuthInterceptor() {
        authToken = null;
    }

    public String setUsernameAndPassword(String username, String password) {
        authToken = Credentials.basic(username, password);
        return authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void clearAuthToken() {
        authToken = null;
    }

    public void setAuthToken(String newAuthToken) {
        authToken = newAuthToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (authToken != null) {
            Request original = chain.request();

            Request.Builder builder = original.newBuilder()
                    .header("Authorization", authToken);

            Request request = builder.build();
            return chain.proceed(request);
        }

        return chain.proceed(chain.request());
    }
}

package rv.jorge.quizzz.service.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * Intercepts responses looking for Cookie Sets for the Session ID.
 * If one is found, it intercepts requests to add the Session ID to the request.
 *
 * To use it, add it as an Interceptor to the OkHttp client and attempt a login.
 * The interceptor will automatically pick up the Session ID and store in SharedPreferences.
 *
 * The interceptor can also be enabled/disabled at runtime to maintain the same state as
 * the rest of the application.
 *
 */

public class CookieAuthenticationInterceptor implements Interceptor, AuthenticationInterceptor {

    private static final String TAG = "CookieInterceptor";

    private static final String PREFS_COOKIES_SESSIONID_KEY = "PREFS_COOKIES_SESSIONID_KEY";
    private static final String COOKIES_SESSIONID_KEY = "JSESSIONID";

    SharedPreferences prefs;
    HttpCookie httpCookie;
    Boolean isEnabled;

    String sessionId;

    public CookieAuthenticationInterceptor(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        isEnabled = false;
        loadSessionFromPrefs();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        /*--- ADDING COOKIES TO REQUEST ---*/
        Request request = addCookieToRequest(chain.request());

        /*--- SENDING REQUEST ---*/
        Response response = chain.proceed(request);

        /*--- PARSING RESPONSE FOR COOKIES ---*/
        parseResponseCookies(response);

        return response;
    }

    private void parseResponseCookies(Response response) {
        List<String> headers = response.headers("Set-Cookie");

        for (String header: headers) {
            List<HttpCookie> cookies = HttpCookie.parse(header);

            for (HttpCookie cookie: cookies) {
                Log.d(TAG, "Cookie name=" + cookie.getName() + " value=" + cookie.getValue());
                if (cookie.getName().equals(COOKIES_SESSIONID_KEY)) {
                    sessionId = cookie.getValue();
                    updateSessionInPrefs();
                }
            }
        }
    }

    private Request addCookieToRequest(Request request) {
        if (httpCookie != null && isEnabled) {
            request = request.newBuilder()
                    .header("Cookie", httpCookie.toString())
                    .build();
        }
        return request;
    }

    private void loadSessionFromPrefs() {
        sessionId = prefs.getString(PREFS_COOKIES_SESSIONID_KEY, null);
        updateCookie();
    }

    private void updateSessionInPrefs() {
        if (sessionId.equals("")) {
            deleteSessionFromPrefs();
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_COOKIES_SESSIONID_KEY, sessionId);
        editor.commit();
        updateCookie();
    }

    private void deleteSessionFromPrefs() {
        prefs.edit().remove(PREFS_COOKIES_SESSIONID_KEY);
        sessionId = null;
        updateCookie();
    }

    private void updateCookie() {
        if (sessionId == null) {
            httpCookie = null;
            return;
        }

        httpCookie = new HttpCookie(COOKIES_SESSIONID_KEY, sessionId);
    }

    @Override
    public void enable() {
        isEnabled = true;
    }

    @Override
    public void disable() {
        isEnabled = false;
    }
}

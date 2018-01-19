package rv.jorge.quizzz.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;
import retrofit2.Response;
import rv.jorge.quizzz.R;
import rv.jorge.quizzz.model.User;
import rv.jorge.quizzz.screens.support.InternalStatus;
import rv.jorge.quizzz.service.retrofit.UserRetrofitService;
import rv.jorge.quizzz.service.support.AuthenticationInterceptor;
import rv.jorge.quizzz.service.support.HttpConstants;

/**
 * User Management.
 *
 * When a user tries to log in, we need to:
 *   - Send a request to the login endpoint using Basic Auth.
 *   - If the request is successful:
 *      - The Http Interceptor will pick up the session information automatically.
 *        The interceptor is responsible to maintain and persist the session information.
 *      - UserService will call Enable on the interceptor.
 *      - UserService will get a callback, upon which we'll update the Current User's
 *        information on Shared Preferences.
 *      - The rest of the application can subscribe to changes in Shared Preferences
 *        to react to a successful login.
 *   - If it is not successful:
 *      - Nothing to do, just report back an error.
 *
 * When a user tries to log out, we need to:
 *   - Send a request to the logout endpoint to invalidate the current session.
 *   - If it is successful:
 *      - UserService should disable the interceptor.
 *      - UserService should update Shared Preferences to reflect the logout.
 *      - The rest of the application can subscribe to changes in Shared Preferences
 *        to react to a successful login.
 *   - If it is not successful:
 *      - Nothing to do, just report back an error.
 */

public class UserService {

    private Context context;
    private UserRetrofitService userRetrofitService;
    private AuthenticationInterceptor authenticationInterceptor;
    private SharedPreferences prefs;

    public UserService(Context context, UserRetrofitService userRetrofitService, AuthenticationInterceptor authenticationInterceptor) {
        this.context = context;
        this.userRetrofitService = userRetrofitService;
        this.authenticationInterceptor = authenticationInterceptor;

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getBoolean(context.getString(R.string.prefs_is_logged_in), false))
            this.authenticationInterceptor.enable();
    }

    public Observable<User> login(String username, String password) {

        return userRetrofitService.login(Credentials.basic(username, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(user -> {
                    authenticationInterceptor.enable();

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong(context.getString(R.string.prefs_user_id), user.getId());
                    editor.putString(context.getString(R.string.prefs_username), user.getUsername());
                    editor.putString(context.getString(R.string.prefs_email), user.getEmail());
                    editor.putBoolean(context.getString(R.string.prefs_is_logged_in), true);
                    editor.commit();
                    return user;
                });

    }


    public Observable<Response<User>> signup(String username, String email, String password) {
        return userRetrofitService.signup(username, email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<InternalStatus> logout() {
        return userRetrofitService.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {

                    switch (response.code()) {
                        case HttpConstants.OK:
                            authenticationInterceptor.disable();

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(context.getString(R.string.prefs_is_logged_in), false);
                            editor.commit();
                            return InternalStatus.OK;
                        default:
                            return InternalStatus.UNKNOWN_ERROR;
                    }

                });
    }

    public Observable<Response<User>> forgotMyPassword(String email) {
        return userRetrofitService.forgotMyPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

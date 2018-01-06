package rv.jorge.quizzz.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import rv.jorge.quizzz.R;
import rv.jorge.quizzz.model.User;
import rv.jorge.quizzz.service.retrofit.UserRetrofitService;
import rv.jorge.quizzz.service.support.BasicAuthInterceptor;

/**
 * Created by jorgerodriguez on 01/01/18.
 */

public class UserService {

    Context context;
    UserRetrofitService userRetrofitService;
    BasicAuthInterceptor basicAuthInterceptor;

    static User currentUser = null;
    SharedPreferences prefs;

    public UserService(Context context, UserRetrofitService userRetrofitService, BasicAuthInterceptor interceptor) {
        this.context = context;
        this.userRetrofitService = userRetrofitService;
        this.basicAuthInterceptor = interceptor;

        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        loadCurrentUser();
    }

    private void loadCurrentUser() {
        boolean isLogged = prefs.getBoolean(context.getString(R.string.prefs_is_logged_in), false);
        if (isLogged) {
            currentUser = new User();
            currentUser.setId(prefs.getLong(context.getString(R.string.prefs_user_id), 0));
            currentUser.setUsername(prefs.getString(context.getString(R.string.prefs_username), ""));
            currentUser.setEmail(prefs.getString(context.getString(R.string.prefs_email), ""));
            basicAuthInterceptor.setAuthToken(prefs.getString(context.getString(R.string.prefs_basic_auth), null));
        }
    }

    public Observable<User> login(String username, String password) {
        String authToken = basicAuthInterceptor.setUsernameAndPassword(username, password);

        return userRetrofitService.login()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(user -> {
                    currentUser = user;
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong(context.getString(R.string.prefs_user_id), user.getId());
                    editor.putString(context.getString(R.string.prefs_basic_auth), getAuthToken());
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

    public void logout() {
        basicAuthInterceptor.clearAuthToken();
        currentUser = null;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(context.getString(R.string.prefs_is_logged_in), false);
        editor.commit();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getAuthToken() {
        return basicAuthInterceptor.getAuthToken();
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public Observable<Response<User>> forgotMyPassword(String email) {
        return userRetrofitService.forgotMyPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

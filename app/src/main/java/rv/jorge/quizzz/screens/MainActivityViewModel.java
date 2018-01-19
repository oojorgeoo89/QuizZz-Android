package rv.jorge.quizzz.screens;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import rv.jorge.quizzz.R;
import rv.jorge.quizzz.model.User;

/**
 *
 * ViewModel to handle the Main Activity.
 *
 * It listen to changes in SharedPreferences to accomodate the UI to whether
 * the user is logged in or not.
 *
 */

public class MainActivityViewModel extends AndroidViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {

    private MutableLiveData<User> loggedUser;
    SharedPreferences prefs;

    public MainActivityViewModel(Application application) {
        super(application);
        loggedUser = new MutableLiveData<>();

        prefs = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    public LiveData<User> getIsUserLoggedInObservable() {
        return loggedUser;
    }

    public void checkIsUserLoggedIn() {
        this.loggedUser.postValue(getUserFromPrefs());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getApplication().getString(R.string.prefs_is_logged_in))) {
            this.loggedUser.postValue(getUserFromPrefs());
        }
    }

    public User getUserFromPrefs() {
        Boolean isUserLoggedIn = prefs.getBoolean(getApplication().getString(R.string.prefs_is_logged_in), false);

        if (!isUserLoggedIn)
            return null;

        User user = new User();
        user.setId(prefs.getLong(getApplication().getString(R.string.prefs_user_id), 0));
        user.setEmail(prefs.getString(getApplication().getString(R.string.prefs_email), null));
        user.setUsername(prefs.getString(getApplication().getString(R.string.prefs_username), null));

        return user;
    }

}

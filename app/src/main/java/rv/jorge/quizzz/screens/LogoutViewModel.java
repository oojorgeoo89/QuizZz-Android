package rv.jorge.quizzz.screens;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import rv.jorge.quizzz.screens.support.InternalStatus;
import rv.jorge.quizzz.service.UserService;

/**
 * ViewModel to handle Logout Use Cases.
 */

public class LogoutViewModel extends ViewModel {

    private static final String TAG = "LogoutViewModel";

    private UserService userService;

    public LogoutViewModel(UserService userService) {
        this.userService = userService;
    }

    public void logout() {
        userService.logout()
                .subscribe(status -> {
                    if (status == InternalStatus.OK)
                        Log.d(TAG, "Logout successful");
                    else
                        Log.d(TAG, "Logout unsuccessful");
                }, throwable -> {
                    Log.d(TAG, "Logout unsuccessful");
                });
    }
}

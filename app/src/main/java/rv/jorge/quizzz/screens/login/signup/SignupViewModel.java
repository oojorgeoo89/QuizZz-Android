package rv.jorge.quizzz.screens.login.signup;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import rv.jorge.quizzz.screens.support.InternalStatus;
import rv.jorge.quizzz.service.UserService;
import rv.jorge.quizzz.service.support.HttpConstants;

/**
 * Created by jorgerodriguez on 18/01/18.
 */

public class SignupViewModel extends ViewModel {

    private UserService userService;
    private MutableLiveData<InternalStatus> isSignupSuccessful;

    public SignupViewModel(UserService userService) {
        this.userService = userService;

        isSignupSuccessful = new MutableLiveData<>();
    }

    public MutableLiveData<InternalStatus> getIsSignupSuccessfulObservable() {
        return isSignupSuccessful;
    }

    public void signup(String username, String email, String password) {
        userService.signup(username,
                email,
                password)
                .subscribe(response -> {

                    switch (response.code()) {
                        case HttpConstants.OK:
                        case HttpConstants.CREATED:
                            isSignupSuccessful.postValue(InternalStatus.OK);
                            break;
                        case HttpConstants.CONFLICT:
                            isSignupSuccessful.postValue(InternalStatus.CONFLICT);
                            break;
                        default:
                            isSignupSuccessful.postValue(InternalStatus.UNKNOWN_ERROR);
                    }

                }, throwable -> {
                    isSignupSuccessful.postValue(InternalStatus.UNKNOWN_ERROR);
                });
    }

}

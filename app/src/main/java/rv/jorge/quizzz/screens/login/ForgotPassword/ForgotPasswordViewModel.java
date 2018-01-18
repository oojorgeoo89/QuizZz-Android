package rv.jorge.quizzz.screens.login.ForgotPassword;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import rv.jorge.quizzz.screens.support.InternalStatus;
import rv.jorge.quizzz.service.UserService;
import rv.jorge.quizzz.service.support.HttpConstants;

/**
 * Created by jorgerodriguez on 18/01/18.
 */

public class ForgotPasswordViewModel extends ViewModel {

    private UserService userService;
    private MutableLiveData<InternalStatus> isRequestSuccessful;

    public ForgotPasswordViewModel(UserService userService) {
        this.userService = userService;

        isRequestSuccessful = new MutableLiveData<>();
    }

    public LiveData<InternalStatus> getIsRequestSuccessfulObservable() {
        return isRequestSuccessful;
    }

    public void sendForgotPasswordRequest(String email) {
        userService.forgotMyPassword(email)
                .subscribe(response -> {
                    switch (response.code()) {
                        case HttpConstants.OK:
                            isRequestSuccessful.postValue(InternalStatus.OK);
                            break;
                        case HttpConstants.NOT_FOUND:
                            isRequestSuccessful.postValue(InternalStatus.NOT_FOUND);
                            break;
                        default:
                            isRequestSuccessful.postValue(InternalStatus.UNKNOWN_ERROR);
                    }


                }, throwable -> {
                    isRequestSuccessful.postValue(InternalStatus.UNKNOWN_ERROR);
                });
    }
}

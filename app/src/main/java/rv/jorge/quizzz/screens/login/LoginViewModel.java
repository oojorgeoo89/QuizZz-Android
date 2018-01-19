package rv.jorge.quizzz.screens.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import rv.jorge.quizzz.screens.support.InternalStatus;
import rv.jorge.quizzz.service.UserService;

public class LoginViewModel extends ViewModel {

    private UserService userService;
    private MutableLiveData<InternalStatus> isLoginSuccessful;

    public LoginViewModel(UserService userService) {
        this.userService = userService;

        isLoginSuccessful = new MutableLiveData<>();
    }

    public LiveData<InternalStatus> getIsLoginSuccessfulObservable() {
        return isLoginSuccessful;
    }

    public void login(String username, String password) {
        userService.login(username, password)
                .subscribe(user -> {
                    isLoginSuccessful.postValue(InternalStatus.OK);
                }, throwable -> {
                    isLoginSuccessful.postValue(InternalStatus.UNKNOWN_ERROR);
                });
    }
}

package rv.jorge.quizzz.screens;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import rv.jorge.quizzz.screens.editquiz.EditQuizViewModel;
import rv.jorge.quizzz.screens.quizlists.home.HomeViewModel;
import rv.jorge.quizzz.screens.login.ForgotPassword.ForgotPasswordViewModel;
import rv.jorge.quizzz.screens.login.LoginViewModel;
import rv.jorge.quizzz.screens.login.signup.SignupViewModel;
import rv.jorge.quizzz.screens.quizlists.myquizzes.MyQuizzesViewModel;
import rv.jorge.quizzz.service.QuizService;
import rv.jorge.quizzz.service.UserService;

/**
 * Created by jorgerodriguez on 18/01/18.
 */

public class QuizViewModelFactory implements ViewModelProvider.Factory {

    UserService userService;
    QuizService quizService;

    public QuizViewModelFactory(UserService userService, QuizService quizService) {
        this.userService = userService;
        this.quizService = quizService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class))
            return (T) new LoginViewModel(userService);
        else if (modelClass.isAssignableFrom(SignupViewModel.class))
            return (T) new SignupViewModel(userService);
        else if (modelClass.isAssignableFrom(ForgotPasswordViewModel.class))
            return (T) new ForgotPasswordViewModel(userService);
        else if (modelClass.isAssignableFrom(EditQuizViewModel.class))
            return (T) new EditQuizViewModel(quizService);
        else if (modelClass.isAssignableFrom(HomeViewModel.class))
            return (T) new HomeViewModel(quizService);
        else if (modelClass.isAssignableFrom(MyQuizzesViewModel.class))
            return (T) new MyQuizzesViewModel(quizService);

        throw new IllegalArgumentException("Unexpected ViewModel request");
    }
}

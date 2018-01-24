package rv.jorge.quizzz.di;

import dagger.Component;
import rv.jorge.quizzz.screens.MainActivity;
import rv.jorge.quizzz.screens.editquiz.EditQuizFragment;
import rv.jorge.quizzz.screens.login.ForgotPassword.ForgotPasswordFragment;
import rv.jorge.quizzz.screens.login.LoginFragment;
import rv.jorge.quizzz.screens.login.signup.SignupFragment;
import rv.jorge.quizzz.screens.playquiz.PlayQuestionFragment;
import rv.jorge.quizzz.screens.playquiz.PlayQuizActivity;
import rv.jorge.quizzz.screens.playquiz.PlayQuizProgressFragment;
import rv.jorge.quizzz.screens.playquiz.PlayQuizQuestionsFragment;
import rv.jorge.quizzz.screens.quizlists.QuizListFragment;
import rv.jorge.quizzz.screens.quizlists.home.HomeFragment;
import rv.jorge.quizzz.screens.quizlists.myquizzes.MyQuizzesFragment;

@QuizApplicationScope
@Component(modules = {QuizApplicationModule.class, ViewModelModule.class})
public interface QuizApplicationComponent {

    void inject(MainActivity mainActivity);
    void inject(PlayQuizActivity playQuizActivity);

    void inject(LoginFragment loginFragment);
    void inject(HomeFragment homeFragment);
    void inject(QuizListFragment quizListFragment);
    void inject(MyQuizzesFragment myQuizzesFragment);
    void inject(SignupFragment signupFragment);
    void inject(ForgotPasswordFragment forgotPasswordFragment);
    void inject(EditQuizFragment editQuizFragment);
    void inject(PlayQuizProgressFragment playQuizProgressFragment);
    void inject(PlayQuizQuestionsFragment playQuizQuestionsFragment);
    void inject(PlayQuestionFragment playQuestionFragment);

}

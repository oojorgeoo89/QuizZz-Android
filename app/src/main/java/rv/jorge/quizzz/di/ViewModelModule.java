package rv.jorge.quizzz.di;

import android.arch.lifecycle.ViewModelProvider;

import dagger.Module;
import dagger.Provides;
import rv.jorge.quizzz.screens.QuizViewModelFactory;
import rv.jorge.quizzz.service.QuizService;
import rv.jorge.quizzz.service.UserService;

/**
 * Created by jorgerodriguez on 18/01/18.
 */

@Module
public class ViewModelModule {

    @Provides
    @QuizApplicationScope
    ViewModelProvider.Factory getViewModelFactory(UserService userService, QuizService quizService) {
        return new QuizViewModelFactory(userService, quizService);
    }

}

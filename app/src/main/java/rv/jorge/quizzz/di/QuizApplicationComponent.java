package rv.jorge.quizzz.di;

import dagger.Component;
import rv.jorge.quizzz.service.QuizService;
import rv.jorge.quizzz.service.UserService;

/**
 * Created by jorgerodriguez on 28/12/17.
 */
@QuizApplicationScope
@Component(modules = {QuizApplicationModule.class})
public interface QuizApplicationComponent {
    QuizService getQuizService();
    UserService getUserService();
}

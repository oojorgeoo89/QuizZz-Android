package rv.jorge.quizzz.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import rv.jorge.quizzz.service.QuizService;
import rv.jorge.quizzz.service.UserService;
import rv.jorge.quizzz.service.retrofit.QuizRetrofitService;
import rv.jorge.quizzz.service.retrofit.UserRetrofitService;
import rv.jorge.quizzz.service.support.BasicAuthInterceptor;

/**
 * Created by jorgerodriguez on 28/12/17.
 */

@Module(includes = {NetworkModule.class})
public class QuizApplicationModule {

    private final Context context;

    public QuizApplicationModule (Context context) {
        this.context = context;
    }

    @Provides
    Context getContext() {
        return context;
    }

    @QuizApplicationScope
    @Provides
    QuizService getQuizService(QuizRetrofitService quizRetrofitService) {
        return new QuizService(quizRetrofitService);
    }

    @QuizApplicationScope
    @Provides
    UserService getUserService(Context context, UserRetrofitService userRetrofitService, BasicAuthInterceptor basicAuthInterceptor) {
        return new UserService(context, userRetrofitService, basicAuthInterceptor);
    }

}

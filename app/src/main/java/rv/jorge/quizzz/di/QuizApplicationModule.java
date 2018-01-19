package rv.jorge.quizzz.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import rv.jorge.quizzz.service.QuizService;
import rv.jorge.quizzz.service.UserService;
import rv.jorge.quizzz.service.retrofit.QuizRetrofitService;
import rv.jorge.quizzz.service.retrofit.UserRetrofitService;
import rv.jorge.quizzz.service.support.AuthenticationInterceptor;

@Module(includes = {NetworkModule.class})
public class QuizApplicationModule {

    private final Context context;

    public QuizApplicationModule (Context context) {
        this.context = context;
    }

    @QuizApplicationScope
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
    UserService getUserService(Context context, UserRetrofitService userRetrofitService, AuthenticationInterceptor authenticationInterceptor) {
        return new UserService(context, userRetrofitService, authenticationInterceptor);
    }

}

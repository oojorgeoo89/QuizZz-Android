package rv.jorge.quizzz.di;

import dagger.Module;
import dagger.Provides;
import rv.jorge.quizzz.service.retrofit.QuizRetrofitService;
import rv.jorge.quizzz.service.retrofit.RetrofitService;
import rv.jorge.quizzz.service.retrofit.UserRetrofitService;
import rv.jorge.quizzz.service.support.BasicAuthInterceptor;

/**
 * Created by jorgerodriguez on 28/12/17.
 */

@Module
public class NetworkModule {

    @Provides
    @QuizApplicationScope
    BasicAuthInterceptor getBasicAuthInterceptor() {
        return new BasicAuthInterceptor();
    }

    @Provides
    @QuizApplicationScope
    RetrofitService getRetrofitService(BasicAuthInterceptor basicAuthInterceptor) {
        return new RetrofitService(basicAuthInterceptor);
    }

    @Provides
    @QuizApplicationScope
    QuizRetrofitService getMyQuizzesRetrofitService(RetrofitService retrofitService) {
        return retrofitService.getRetrofitInstance().create(QuizRetrofitService.class);
    }

    @Provides
    @QuizApplicationScope
    UserRetrofitService getUserRetrofitService(RetrofitService retrofitService) {
        return retrofitService.getRetrofitInstance().create(UserRetrofitService.class);
    }

}

package rv.jorge.quizzz.di;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rv.jorge.quizzz.service.retrofit.QuizRetrofitService;
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
    //TODO: Move the Base URL to a config file
    Retrofit getRetrofitClient(BasicAuthInterceptor basicAuthInterceptor) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(basicAuthInterceptor).build();

        return retrofitBuilder
                .client(httpClient)
                .build();
    }

    @Provides
    @QuizApplicationScope
    QuizRetrofitService getMyQuizzesRetrofitService(Retrofit retrofit) {
        return retrofit.create(QuizRetrofitService.class);
    }

    @Provides
    @QuizApplicationScope
    UserRetrofitService getUserRetrofitService(Retrofit retrofit) {
        return retrofit.create(UserRetrofitService.class);
    }

}

package rv.jorge.quizzz.di;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rv.jorge.quizzz.service.retrofit.QuizRetrofitService;
import rv.jorge.quizzz.service.retrofit.UserRetrofitService;
import rv.jorge.quizzz.service.support.AuthenticationInterceptor;
import rv.jorge.quizzz.service.support.CookieAuthenticationInterceptor;

@Module
public class NetworkModule {

    @Provides
    @QuizApplicationScope
    CookieAuthenticationInterceptor getCookieAuthenticationInterceptor(Context context) {
        return new CookieAuthenticationInterceptor(context);
    }

    @Provides
    @QuizApplicationScope
    AuthenticationInterceptor getAuthenticationInterceptor(CookieAuthenticationInterceptor cookieAuthenticationInterceptor) {
        return cookieAuthenticationInterceptor;
    }

    @Provides
    @QuizApplicationScope
    //TODO: Move the Base URL to a config file
    Retrofit getRetrofitClient(CookieAuthenticationInterceptor cookieAuthenticationInterceptor) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(cookieAuthenticationInterceptor).build();

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

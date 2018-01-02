package rv.jorge.quizzz.service.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rv.jorge.quizzz.service.support.BasicAuthInterceptor;

/**
 * Created by jorgerodriguez on 19/08/17.
 */

public class RetrofitService {

    public final static String URL_PREFIX = "/api";
    private Retrofit retrofit = null;

    BasicAuthInterceptor basicAuthInterceptor;

    public RetrofitService(BasicAuthInterceptor basicAuthInterceptor) {
        this.basicAuthInterceptor = basicAuthInterceptor;

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(basicAuthInterceptor).build();

        retrofit = retrofitBuilder
                .client(httpClient)
                .build();
    }

    public Retrofit getRetrofitInstance() {
        return retrofit;
    }

}

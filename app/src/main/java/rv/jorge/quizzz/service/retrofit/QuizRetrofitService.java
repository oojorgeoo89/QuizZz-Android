package rv.jorge.quizzz.service.retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rv.jorge.quizzz.model.Quiz;
import rv.jorge.quizzz.model.support.Page;

/**
 * Created by jorgerodriguez on 31/12/17.
 */

public interface QuizRetrofitService {

    @GET(RetrofitService.URL_PREFIX + "/quizzes/{quiz_id}")
    Observable<Quiz> getQuiz(@Path("quiz_id") long quizId);

    @GET(RetrofitService.URL_PREFIX + "/users/{user_id}/quizzes")
    Observable<Page<Quiz>> getMyQuizzes(@Path("user_id") long userId, @Query("page") int page);

    @GET(RetrofitService.URL_PREFIX + "/quizzes")
    Observable<Page<Quiz>> getPublicQuizzes(@Query("page") int page, @Query("published") boolean published);

    @FormUrlEncoded
    @POST(RetrofitService.URL_PREFIX + "/quizzes")
    Observable<Quiz> createQuiz(@Field("name") String name, @Field("Description") String description);
}

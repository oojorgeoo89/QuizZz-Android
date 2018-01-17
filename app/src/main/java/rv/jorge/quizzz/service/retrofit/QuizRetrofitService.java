package rv.jorge.quizzz.service.retrofit;

import io.reactivex.Observable;
import retrofit2.Response;
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

    @GET("/api/quizzes/{quiz_id}")
    Observable<Response<Quiz>> getQuiz(@Path("quiz_id") long quizId);

    @GET("/api/users/myQuizzes")
    Observable<Page<Quiz>> getMyQuizzes(@Query("page") int page);

    @GET("/api/quizzes")
    Observable<Page<Quiz>> getPublicQuizzes(@Query("page") int page, @Query("published") boolean published);

    @FormUrlEncoded
    @POST("/api/quizzes")
    Observable<Response<Quiz>> createQuiz(@Field("name") String name, @Field("Description") String description);
}

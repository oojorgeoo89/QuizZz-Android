package rv.jorge.quizzz.service.retrofit;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rv.jorge.quizzz.model.Question;
import rv.jorge.quizzz.model.Quiz;
import rv.jorge.quizzz.model.support.AnswerBundle;
import rv.jorge.quizzz.model.support.Page;
import rv.jorge.quizzz.model.support.QuizResults;

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

    @GET("/api/quizzes/{quiz_id}/questions")
    Observable<List<Question>> getQuestionsByQuizId(@Path("quiz_id") long quizId, @Query("onlyValid") boolean onlyValid);

    @POST("/api/quizzes/{quiz_id}/submitAnswers")
    Observable<QuizResults> submitAnswers(@Path("quiz_id") long quizId, @Body List<AnswerBundle> answers);

}

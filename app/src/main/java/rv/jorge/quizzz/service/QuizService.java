package rv.jorge.quizzz.service;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import rv.jorge.quizzz.model.Quiz;
import rv.jorge.quizzz.model.support.Page;
import rv.jorge.quizzz.service.retrofit.QuizRetrofitService;

/**
 * Created by jorgerodriguez on 29/12/17.
 */

public class QuizService {

    private final QuizRetrofitService quizRetrofitService;

    public QuizService(QuizRetrofitService quizRetrofitService) {
        this.quizRetrofitService = quizRetrofitService;
    }

    public Observable<Response<Quiz>> createQuiz(String name, String description) {
        return quizRetrofitService.createQuiz(name, description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public QuizPageIterator getQuizPageIterator(QueryType queryType) {
        switch (queryType) {
            case PUBLIC_ALL_QUIZZES:
                return new PublicQuizzesPageIterator(quizRetrofitService);
            case MY_QUIZZES:
                return new MyQuizzesPageIterator(quizRetrofitService);
            default:
                throw new IllegalArgumentException();
        }
    }

    public enum QueryType {
        PUBLIC_ALL_QUIZZES,
        MY_QUIZZES
    }

    public interface QuizPageIterator {
        Observable<List<Quiz>> getFirstPage();
        Observable<List<Quiz>> getNextPage();
        Observable<List<Quiz>> getPageAt(int index);
        boolean isLastPage();
    }

    private abstract class QuizPageIteratorRetrofit implements QuizPageIterator {

        private final QuizRetrofitService quizRetrofitService;

        private boolean hasNext = true;
        private int nextPage = 0;

        public QuizPageIteratorRetrofit(QuizRetrofitService quizRetrofitService) {
            this.quizRetrofitService = quizRetrofitService;
        }

        public Observable<List<Quiz>> getFirstPage() {
            nextPage = 0;
            return getNextPage();
        }

        public Observable<List<Quiz>> getNextPage() {
            return getPageAt(nextPage);
        }

        public Observable<List<Quiz>> getPageAt(int index) {
            return getObservableQuery(index)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(quizPage -> {
                        this.hasNext = !quizPage.getLast();
                        this.nextPage = quizPage.getNumber() + 1;
                        return  quizPage.getContent();
                    });
        }

        public boolean isLastPage() {
            return !hasNext;
        }

        protected abstract Observable<Page<Quiz>> getObservableQuery(int index);

    }

    private class PublicQuizzesPageIterator extends QuizPageIteratorRetrofit {

        public PublicQuizzesPageIterator(QuizRetrofitService quizRetrofitService) {
            super(quizRetrofitService);
        }

        @Override
        protected Observable<Page<Quiz>> getObservableQuery(int index) {
            return quizRetrofitService.getPublicQuizzes(index, true);
        }
    }

    private class MyQuizzesPageIterator extends QuizPageIteratorRetrofit {

        public MyQuizzesPageIterator(QuizRetrofitService quizRetrofitService) {
            super(quizRetrofitService);
        }

        @Override
        protected Observable<Page<Quiz>> getObservableQuery(int index) {
            return quizRetrofitService.getMyQuizzes(index);
        }
    }

}

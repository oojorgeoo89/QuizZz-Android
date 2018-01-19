package rv.jorge.quizzz.screens.quizlists;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import rv.jorge.quizzz.model.Quiz;
import rv.jorge.quizzz.screens.support.InternalStatus;
import rv.jorge.quizzz.service.QuizService;

/**
 *
 * Abstract ViewModel containing the common code to create a list of Quizzes.
 *
 * To use it, extend it and override getQueryType so we can use the appropriate iterator
 * from QuizService.
 *
 */

public abstract class QuizListViewModel extends ViewModel {
    private QuizService quizService;
    private QuizService.QuizPageIterator quizPageIterator;

    private MutableLiveData<List<Quiz>> quizListObservable;
    private MutableLiveData<InternalStatus> requestStatus;

    public QuizListViewModel(QuizService quizService) {
        this.quizService = quizService;
        quizPageIterator = quizService.getQuizPageIterator(getQueryType());

        quizListObservable = new MutableLiveData<>();
        requestStatus = new MutableLiveData<>();
    }

    public LiveData<List<Quiz>> getQuizListObservable() {
        return quizListObservable;
    }

    public LiveData<InternalStatus> getRequestStatus() {
        return requestStatus;
    }

    public void initializeQuizList() {
        quizPageIterator.getFirstPage()
                .subscribe(quizzes -> {
                    quizListObservable.postValue(quizzes);
                }, throwable -> {
                    requestStatus.postValue(InternalStatus.UNKNOWN_ERROR);
                });
    }

    public void arrivedToEndOfList() {
        if (quizPageIterator.isLastPage())
            return;

        quizPageIterator.getNextPage()
                .subscribe(quizzes -> {
                    quizListObservable.postValue(quizzes);
                }, throwable -> {
                    requestStatus.postValue(InternalStatus.UNKNOWN_ERROR);
                });
    }

    protected abstract QuizService.QueryType getQueryType();
}

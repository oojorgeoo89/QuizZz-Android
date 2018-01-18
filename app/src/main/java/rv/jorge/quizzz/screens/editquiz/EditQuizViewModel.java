package rv.jorge.quizzz.screens.editquiz;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import rv.jorge.quizzz.screens.support.InternalStatus;
import rv.jorge.quizzz.service.QuizService;
import rv.jorge.quizzz.service.support.HttpConstants;

/**
 * Created by jorgerodriguez on 18/01/18.
 */

public class EditQuizViewModel extends ViewModel {

    private QuizService quizService;
    private MutableLiveData<InternalStatus> isQuizSavedSuccessfully;

    public EditQuizViewModel(QuizService quizService) {
        this.quizService = quizService;

        isQuizSavedSuccessfully = new MutableLiveData<>();
    }

    public LiveData<InternalStatus> getIsQuizSavedSuccessfully() {
        return isQuizSavedSuccessfully;
    }


    public void saveQuiz(String title, String description) {
        quizService.createQuiz(title, description)
                .subscribe(populatedQuizResponse -> {
                    if (populatedQuizResponse.code() == HttpConstants.CREATED) {
                        isQuizSavedSuccessfully.postValue(InternalStatus.OK);
                    } else {
                        isQuizSavedSuccessfully.postValue(InternalStatus.UNKNOWN_ERROR);
                    }

                }, throwable -> {
                    isQuizSavedSuccessfully.postValue(InternalStatus.UNKNOWN_ERROR);
                });
    }
}

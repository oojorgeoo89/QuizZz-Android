package rv.jorge.quizzz.screens.playquiz;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import rv.jorge.quizzz.model.Question;
import rv.jorge.quizzz.model.support.AnswerBundle;
import rv.jorge.quizzz.model.support.QuizResults;
import rv.jorge.quizzz.service.QuizService;

/**
 *
 * ViewModel used to play a Quiz.
 *
 * In order to start a Quiz:
 *      - Subscribe to the observables.
 *      - Call startGame() providing the ID of the Quiz you want to play.
 *
 * It communicates back to the View via:
 *
 *      - questionsLiveData: Provides a List with the questions for a particular Quiz. Data
 *                           should be expected after calling startGame()
 *
 *      - quizResultsLiveData: Provides the QuizResults gathered from the backend once the
 *                             ame has finished.
 *
 *      - questionHasBeenAnsweredLiveData: Indicates that a question has been answered. Used
 *                                         for inter-fragment communication.
 *
 */
public class PlayQuizViewModel extends ViewModel {

    private QuizService quizService;
    private long quizId;

    private MutableLiveData<List<Question>> questionsLiveData;
    private MutableLiveData<QuizResults> quizResultsLiveData;
    private MutableLiveData<Boolean> questionHasBeenAnsweredLiveData;

    private List<Question> questions;
    private int currentQuestionNumber;

    private List<AnswerBundle> answers;

    public PlayQuizViewModel(QuizService quizService) {
        this.quizService = quizService;
        this.answers = new ArrayList<>();

        questionsLiveData = new MutableLiveData<>();
        quizResultsLiveData = new MutableLiveData<>();
        questionHasBeenAnsweredLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Question>> getQuestionsAvailableObservable() {
        return questionsLiveData;
    }

    public LiveData<QuizResults> getQuizResultsObservable() {
        return quizResultsLiveData;
    }

    public LiveData<Boolean> getQuestionHasBeenAnsweredObservable() {
        return questionHasBeenAnsweredLiveData;
    }

    public void startGame(long quizId) {
        this.quizId = quizId;

        quizService.getQuestionsByQuiz(quizId)
                .subscribe(questions -> {
                    this.questions = questions;
                    currentQuestionNumber = 0;
                    questionsLiveData.postValue(questions);
                }, throwable -> {
                    questionsLiveData.postValue(null);
                });
    }

    public void answerQuestion(int selectedAnswerNumber) {
        answers.add(new AnswerBundle(questions.get(currentQuestionNumber).getId(),
                questions.get(currentQuestionNumber).getAnswers().get(selectedAnswerNumber).getId()));

        currentQuestionNumber++;

        if (currentQuestionNumber == getQuestionCount()) {
            finishGame();
            return;
        }

        questionHasBeenAnsweredLiveData.postValue(true);
    }

    private void finishGame() {
        quizService.submitAnswers(quizId, answers)
                .subscribe(quizResults -> {
                   quizResultsLiveData.postValue(quizResults);
                }, throwable -> {
                    //TODO: Handle error condition
                });
    }

    public int getQuestionCount() {
        return questions.size();
    }

    public int getCurrentQuestionNumber() {
        return currentQuestionNumber;
    }
}

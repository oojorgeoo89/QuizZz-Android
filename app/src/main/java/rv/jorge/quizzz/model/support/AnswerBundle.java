package rv.jorge.quizzz.model.support;

/**
 * Created by jorgerodriguez on 23/01/18.
 */

public class AnswerBundle {

    private long question;
    private long selectedAnswer;

    public AnswerBundle(long question, long selectedAnswer) {
        this.question = question;
        this.selectedAnswer = selectedAnswer;
    }

    public long getQuestion() {
        return question;
    }

    public void setQuestion(long question) {
        this.question = question;
    }

    public long getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(long selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }
}

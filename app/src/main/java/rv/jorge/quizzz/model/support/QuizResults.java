package rv.jorge.quizzz.model.support;

/**
 * Quiz results returned from the backend
 */

public class QuizResults {

    private int totalQuestions;
    private int correctQuestions;

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectQuestions() {
        return correctQuestions;
    }

    public void setCorrectQuestions(int correctQuestions) {
        this.correctQuestions = correctQuestions;
    }

    @Override
    public String toString() {
        return correctQuestions + "/" + totalQuestions;
    }
}

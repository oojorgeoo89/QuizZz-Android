package rv.jorge.quizzz.screens.playquiz;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import rv.jorge.quizzz.R;
import rv.jorge.quizzz.model.support.QuizResults;


/**
 *
 * This fragment is responsible for displaying the results of a Quiz, once
 * the user has finished playing it.
 *
 * The results can be set using Fragment arguments, or calling setResults() if
 * the results are not available at the time of Fragment creation.
 *
 */

public class PlayQuizResultsFragment extends Fragment {

    public static final String ARGS_QUIZ_TITLE = "ARGS_QUIZ_TITLE";
    public static final String ARGS_TOTAL_QUESTIONS = "ARGS_TOTAL_QUESTIONS";
    public static final String ARGS_NUM_CORRECT_ANSWERS = "ARGS_NUM_CORRECT_ANSWERS";

    private TextView quizTitle;
    private TextView resultsAbsolute;
    private TextView resultsPercentage;
    private Button finishButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_quiz_results, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String quizTitleVal = args.getString(ARGS_QUIZ_TITLE);
        Integer totalQuestions = args.getInt(ARGS_TOTAL_QUESTIONS, 0);
        Integer numberCorrectAnswers = args.getInt(ARGS_NUM_CORRECT_ANSWERS, 0);

        quizTitle = (TextView) view.findViewById(R.id.txt_quiz_title);
        resultsAbsolute = (TextView) view.findViewById(R.id.txt_results_absolute);
        resultsPercentage = (TextView) view.findViewById(R.id.txt_results_percentage);
        finishButton = (Button) view.findViewById(R.id.btn_finish_game);

        quizTitle.setText(quizTitleVal);
        setResults(totalQuestions, numberCorrectAnswers);

        finishButton.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void setResults(Integer totalQuestions, int numberCorrectAnswers) {
        resultsAbsolute.setText(numberCorrectAnswers + "/" + totalQuestions);
        resultsPercentage.setText(Integer.toString(Math.round((float) numberCorrectAnswers /totalQuestions)*100) + "%");
    }

    public void setResults(QuizResults results) {
        setResults(results.getTotalQuestions(), results.getCorrectQuestions());
    }

}

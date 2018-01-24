package rv.jorge.quizzz.screens.playquiz;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import rv.jorge.quizzz.QuizApplication;
import rv.jorge.quizzz.R;

/**
 *
 * PlayQuestionFragment is responsible to display a Question to the user with the
 * possible answers. Once the user selects and submits an answer, the View should report
 * the action to the ViewModel, which will notify other Fragments that the action has
 * taken place.
 *
 */

public class PlayQuestionFragment extends Fragment {

    public static final String ARGS_QUESTION_TITLE = "ARGS_QUESTION_TITLE";
    public static final String ARGS_ANSWERS = "ARGS_ANSWERS";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    PlayQuizViewModel playQuizViewModel;

    TextView questionTitleView;
    RadioGroup answersRadioGroup;
    Button submitAnswer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Binding views
        questionTitleView = (TextView) view.findViewById(R.id.txt_question_title);
        answersRadioGroup = (RadioGroup) view.findViewById(R.id.rg_answers);
        submitAnswer = (Button) view.findViewById(R.id.btn_submit_answer);

        // Injecting dependencies in Fragment
        ((QuizApplication) getActivity().getApplication())
                .getComponent()
                .inject(this);

        // Binding View Models
        playQuizViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(PlayQuizViewModel.class);

        // Fetching arguments
        populateUIFromArguments();

        setOnSubmitListener();

    }

    private void populateUIFromArguments() {
        String questionTitle = getArguments().getString(ARGS_QUESTION_TITLE, "");
        String[] answers = getArguments().getStringArray(ARGS_ANSWERS);

        questionTitleView.setText(questionTitle);

        for (int i=0; i<answers.length; i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(answers[i]);
            radioButton.setId(i);

            answersRadioGroup.addView(radioButton);
        }
    }

    private void setOnSubmitListener() {
        submitAnswer.setOnClickListener(v -> {
            int checkedAnswer = answersRadioGroup.getCheckedRadioButtonId();

            if (checkedAnswer == -1) {
                Toast.makeText(getActivity(), getString(R.string.play_no_answer_selected), Toast.LENGTH_LONG).show();
                return;
            }

            submitAnswer.setEnabled(false);
            playQuizViewModel.answerQuestion(checkedAnswer);
        });
    }
}

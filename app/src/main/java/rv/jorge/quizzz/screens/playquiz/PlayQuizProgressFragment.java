package rv.jorge.quizzz.screens.playquiz;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import rv.jorge.quizzz.QuizApplication;
import rv.jorge.quizzz.R;

/**
 *
 * This fragment listens on {@link PlayQuizViewModel} and displays progress
 * information to the user.
 *
 */

public class PlayQuizProgressFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    PlayQuizViewModel playQuizViewModel;

    ProgressBar progressBar;
    TextView progressDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_quiz_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Binding UI
        progressBar = (ProgressBar) view.findViewById(R.id.pbar_play_progress);
        progressDetails = (TextView) view.findViewById(R.id.txt_progress_details);

        // Injecting dependencies in Fragment
        ((QuizApplication) getActivity().getApplication())
                .getComponent()
                .inject(this);

        // Binding View Models
        playQuizViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(PlayQuizViewModel.class);

        // Setting up behavior
        observeNewQuestions();
        observeQuestionHasBeenAnswered();

    }

    private void observeNewQuestions() {
        playQuizViewModel.getQuestionsAvailableObservable()
                .observe(this, questions -> {
                    if (questions != null)
                        updateProgress();
                });
    }

    private void observeQuestionHasBeenAnswered() {
        playQuizViewModel.getQuestionHasBeenAnsweredObservable()
                .observe(this, isQuestionAnswered -> {
                    updateProgress();
                });
    }

    private void updateProgress() {
        progressBar.setProgress(calculateProgress());
        progressDetails.setText(playQuizViewModel.getCurrentQuestionNumber()+1 + "/" + playQuizViewModel.getQuestionCount());
    }

    private int calculateProgress() {
        return Math.round((((float) playQuizViewModel.getCurrentQuestionNumber()+1) / playQuizViewModel.getQuestionCount()) * 100);
    }

}

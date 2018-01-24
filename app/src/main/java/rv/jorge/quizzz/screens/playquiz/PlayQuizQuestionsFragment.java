package rv.jorge.quizzz.screens.playquiz;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import rv.jorge.quizzz.QuizApplication;
import rv.jorge.quizzz.R;
import rv.jorge.quizzz.model.Question;
import rv.jorge.quizzz.model.support.QuizResults;


/**
 *
 * Contains a ViewPager to iterate through the Questions. It makes use of
 * {@link PlayQuestionFragment} to do so. The last Page of the ViewPager will display the
 * Quiz Results using {@link PlayQuizResultsFragment}.
 *
 * This fragment listens on {@link PlayQuizViewModel} to know when a new Question
 * should be displayed to the user.
 *
 */


public class PlayQuizQuestionsFragment extends Fragment {

    public static final String ARGS_QUIZ_TITLE = "ARGS_QUIZ_TITLE";
    public static final String TAG = "QuizProgressFragment";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    PlayQuizViewModel playQuizViewModel;

    ViewPager questionsViewPager;
    ScreenSlidePagerAdapter pagerAdapter;

    String quizTitle;
    private List<Question> questions;

    public static PlayQuizQuestionsFragment getInstance(String quizTitle) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_QUIZ_TITLE, quizTitle);

        PlayQuizQuestionsFragment playQuizQuestionsFragment = new PlayQuizQuestionsFragment();
        playQuizQuestionsFragment.setArguments(bundle);

        return playQuizQuestionsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_quiz_questions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        quizTitle = getArguments().getString(ARGS_QUIZ_TITLE, "");

        // Binding UI
        questionsViewPager = (ViewPager) view.findViewById(R.id.vpgr_questions_view);

        // Injecting dependencies in Fragment
        ((QuizApplication) getActivity().getApplication())
                .getComponent()
                .inject(this);

        // Binding View Models
        playQuizViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(PlayQuizViewModel.class);

        // Setting up behavior
        observeNewQuestions();
        observeResults();
        observeQuestionHasBeenAnswered();

    }

    private void observeResults() {
        playQuizViewModel.getQuizResultsObservable()
                .observe(this, results -> {
                    Log.d(TAG, "Game completed! " + results.toString() + " answers right");
                    pagerAdapter.setResults(results);
                    loadNextPage();
                });
    }

    private void observeNewQuestions() {
        playQuizViewModel.getQuestionsAvailableObservable()
                .observe(this, questions -> {

                    if (questions == null) {
                        Toast.makeText(getActivity(), getString(R.string.unknown_error), Toast.LENGTH_LONG).show();
                    }

                    this.questions = questions;

                    initializeQuestionsFragment();
                });
    }

    private void observeQuestionHasBeenAnswered() {
        playQuizViewModel.getQuestionHasBeenAnsweredObservable()
                .observe(this, isQuestionAnswered -> {
                    loadNextPage();
                });
    }

    private void initializeQuestionsFragment() {
        pagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager(), questions);
        questionsViewPager.setAdapter(pagerAdapter);
    }

    private void loadNextPage() {
        questionsViewPager.setCurrentItem(questionsViewPager.getCurrentItem() + 1);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        List<Question> questions;
        PlayQuizResultsFragment resultsFragment;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<Question> questions) {
            super(fm);
            this.questions = questions;
        }

        @Override
        public Fragment getItem(int position) {
            if (position < questions.size()) {
                return createPlayQuestionFragmentForPosition(position);
            } else {

                Bundle bundle = new Bundle();
                bundle.putString(PlayQuizResultsFragment.ARGS_QUIZ_TITLE, quizTitle);

                resultsFragment = new PlayQuizResultsFragment();
                resultsFragment.setArguments(bundle);

                return resultsFragment;
            }
        }

        @NonNull
        private Fragment createPlayQuestionFragmentForPosition(int position) {
            Question question = questions.get(position);
            String[] answers = new String[question.getAnswers().size()];

            for (int i=0; i<question.getAnswers().size(); i++) {
                answers[i] = question.getAnswers().get(i).getText();
            }

            Bundle bundle = new Bundle();
            bundle.putString(PlayQuestionFragment.ARGS_QUESTION_TITLE, question.getText());
            bundle.putStringArray(PlayQuestionFragment.ARGS_ANSWERS, answers);

            Fragment playQuestionFragment = new PlayQuestionFragment();
            playQuestionFragment.setArguments(bundle);

            return playQuestionFragment;
        }

        /**
         * One Page per Question plus a page for the results
         */
        @Override
        public int getCount() {
            return questions.size() + 1;
        }

        public void setResults(QuizResults results) {
            resultsFragment.setResults(results);
        }

    }

}

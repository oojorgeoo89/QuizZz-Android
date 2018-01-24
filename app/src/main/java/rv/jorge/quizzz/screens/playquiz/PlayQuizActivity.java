package rv.jorge.quizzz.screens.playquiz;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import rv.jorge.quizzz.QuizApplication;
import rv.jorge.quizzz.R;

/**
 *
 * PlayQuizActivity initializes the Progress ({@link PlayQuizProgressFragment} and
 * Questions {@link PlayQuizQuestionsFragment} and triggers the start of the quiz.
 *
 * Both fragments will be listening on the observables available in
 * {@link PlayQuizViewModel} to follow along.
 *
 */

public class PlayQuizActivity extends AppCompatActivity {

    public static final String ARGS_QUIZ_ID = "ARGS_QUIZ_ID";
    public static final String ARGS_QUIZ_TITLE = "ARGS_QUIZ_TITLE";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    PlayQuizViewModel playQuizViewModel;

    private long quizId;
    private String quizTitle;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);

        fragmentManager = getSupportFragmentManager();

        // Injecting dependencies in Fragment
        ((QuizApplication) getApplication())
                .getComponent()
                .inject(this);

        playQuizViewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayQuizViewModel.class);

        // Reading arguments
        quizId = getIntent().getLongExtra(ARGS_QUIZ_ID, 0);
        quizTitle = getIntent().getStringExtra(ARGS_QUIZ_TITLE);

        // Initialize UI
        initializeProgressFragment();
        initializeQuestionsFragment();

        // Once everything is initialized, we can start the game
        playQuizViewModel.startGame(quizId);

    }

    private void initializeProgressFragment() {
        fragmentManager
                .beginTransaction()
                .add(R.id.progress_fragment, new PlayQuizProgressFragment())
                .commit();
    }

    private void initializeQuestionsFragment() {
        fragmentManager
                .beginTransaction()
                .add(R.id.questions_fragment, PlayQuizQuestionsFragment.getInstance(quizTitle))
                .commit();
    }

}

package rv.jorge.quizzz.screens.quizlists;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rv.jorge.quizzz.QuizApplication;
import rv.jorge.quizzz.R;
import rv.jorge.quizzz.model.Quiz;
import rv.jorge.quizzz.screens.support.EndlessScrollListener;
import rv.jorge.quizzz.screens.support.FragmentUmbrella;
import rv.jorge.quizzz.screens.support.InternalStatus;

/**
 *
 * Abstract class that contains the common code to create a list of Quizzes.
 *
 * In order to function, it needs to know what type of quizzes we are interested in. We can provide
 * that by extending the class and giving it the appropiate QuizListViewModel.
 *
 */

public abstract class QuizListFragment extends Fragment {

    public static final String KEY_SAVED_INSTANCE = "rv.jorge.quizzz.controller.QuizListFragment.KEY_SAVED_INSTANCE";

    QuizListViewModel quizListViewModel;
    protected FragmentUmbrella fragmentUmbrella;

    RecyclerView recyclerQuizzes;

    private List<Quiz> quizzes;
    private QuizListRecyclerAdapter quizListRecyclerAdapter;
    private LinearLayoutManager quizzesLayoutManager;

    private LiveData<List<Quiz>> quizListObservable;
    private LiveData<InternalStatus> internalStatusOnservable;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Binding UI
        recyclerQuizzes = (RecyclerView) view.findViewById(R.id.quiz_list);

        // Injecting dependencies in Fragment
        ((QuizApplication) getActivity().getApplication())
                .getComponent()
                .inject(this);

        // Binding View Models
        quizListViewModel = getViewModel();

        // Setting up behavior
        if (quizListObservable == null) {
            quizListObservable = quizListViewModel.getQuizListObservable();
            quizListObservable.observe(this, quizzes -> {
                this.quizzes.addAll(quizzes);
                quizListRecyclerAdapter.onDataUpdate();
            });
        }

        if (internalStatusOnservable == null) {
            internalStatusOnservable = quizListViewModel.getRequestStatus();
            internalStatusOnservable.observe(this, status -> {
                Toast.makeText(getActivity(), getString(R.string.load_quizzes_error), Toast.LENGTH_LONG).show();
            });
        }

        initializeBoxList(savedInstanceState);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Parcelable state = quizzesLayoutManager.onSaveInstanceState();
        outState.putParcelable(KEY_SAVED_INSTANCE, state);
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    private void initializeBoxList(@Nullable Bundle savedInstanceState) {

        /* Checking whether the Fragment has already been initialized */
        if (quizzes == null) {
            quizzes = new ArrayList<>();
            quizListViewModel.initializeQuizList();
        }

        quizzesLayoutManager = new LinearLayoutManager(getActivity());
        recyclerQuizzes.setLayoutManager(quizzesLayoutManager);
        quizListRecyclerAdapter = new QuizListRecyclerAdapter(getActivity(), quizzes, getOnClickListener());
        recyclerQuizzes.setAdapter(quizListRecyclerAdapter);

        if (savedInstanceState != null) {
            Parcelable state = savedInstanceState.getParcelable(KEY_SAVED_INSTANCE);
            quizzesLayoutManager.onRestoreInstanceState(state);
        }

        recyclerQuizzes.addOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onScrolledToEnd() {
                Log.d("Scroll listener", "I have reached the end of the list. Load more data!");
                quizListViewModel.arrivedToEndOfList();
            }
        });

    }

    // For API < 24
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FragmentUmbrella) {
            fragmentUmbrella = (FragmentUmbrella) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement SuccessfulLoginListener");
        }
    }

    // For API >= 24
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentUmbrella) {
            fragmentUmbrella = (FragmentUmbrella) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SuccessfulLoginListener");
        }
    }

    protected abstract QuizListViewModel getViewModel();
    protected abstract QuizListRecyclerAdapter.OnClickListener getOnClickListener();
}

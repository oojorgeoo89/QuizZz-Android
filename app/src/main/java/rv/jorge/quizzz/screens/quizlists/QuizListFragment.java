package rv.jorge.quizzz.screens.quizlists;

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

public abstract class QuizListFragment extends Fragment {

    public static final String KEY_SAVED_INSTANCE = "rv.jorge.quizzz.controller.QuizListFragment.KEY_SAVED_INSTANCE";

    QuizListViewModel quizListViewModel;

    RecyclerView recyclerQuizzes;

    private List<Quiz> quizzes;
    private QuizListRecyclerAdapter quizListRecyclerAdapter;
    private LinearLayoutManager quizzesLayoutManager;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Binding UI
        recyclerQuizzes = (RecyclerView) getView().findViewById(R.id.quiz_list);

        // Injecting dependencies in Fragment
        ((QuizApplication) getActivity().getApplication())
                .getComponent()
                .inject(this);

        // Binding View Models
        quizListViewModel = getViewModel();

        // Setting up behavior
        quizListViewModel.getQuizListObservable().observe(this, quizzes -> {
            this.quizzes.addAll(quizzes);
            quizListRecyclerAdapter.onDataUpdate();
        });

        quizListViewModel.getRequestStatus().observe(this, status -> {
            Toast.makeText(getActivity(), getString(R.string.load_quizzes_error), Toast.LENGTH_LONG).show();
        });

        initializeBoxList(savedInstanceState);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Parcelable state = quizzesLayoutManager.onSaveInstanceState();
        outState.putParcelable(KEY_SAVED_INSTANCE, state);
    }


    private void initializeBoxList(@Nullable Bundle savedInstanceState) {
        quizzesLayoutManager = new LinearLayoutManager(getActivity());
        recyclerQuizzes.setLayoutManager(quizzesLayoutManager);

        if (savedInstanceState == null) {
            quizzes = new ArrayList<>();
            quizListRecyclerAdapter = new QuizListRecyclerAdapter(getActivity(), quizzes, getOnClickListener());
            recyclerQuizzes.setAdapter(quizListRecyclerAdapter);
            quizListViewModel.initializeQuizList();
        } else {
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

    protected abstract QuizListViewModel getViewModel();
    protected abstract QuizListRecyclerAdapter.OnClickListener getOnClickListener();
}

package rv.jorge.quizzz.screens;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rv.jorge.quizzz.QuizApplication;
import rv.jorge.quizzz.R;
import rv.jorge.quizzz.model.Quiz;
import rv.jorge.quizzz.screens.support.EndlessScrollListener;
import rv.jorge.quizzz.service.QuizService;

public class QuizListFragment extends Fragment {

    public static final String ARGS_QUERY_TYPE = "rv.jorge.quizzz.controller.QuizListFragment.ARGS_QUERY_TYPE";
    public static final String KEY_SAVED_INSTANCE = "rv.jorge.quizzz.controller.QuizListFragment.KEY_SAVED_INSTANCE";

    private QuizService.QuizPageIterator quizPageIterator;

    RecyclerView recyclerQuizzes;

    private List<Quiz> quizzes;
    private QuizRecyclerAdapter quizRecyclerAdapter;
    private LinearLayoutManager quizzesLayoutManager;

    public QuizListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerQuizzes = (RecyclerView) getView().findViewById(R.id.quiz_list);
        Bundle args = getArguments();

        QuizService quizService = QuizApplication.get(getActivity()).getQuizService();
        QuizService.QueryType queryType = (QuizService.QueryType) args.getSerializable(ARGS_QUERY_TYPE);
        quizPageIterator = quizService.getQuizPageIterator(queryType);

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
            quizRecyclerAdapter = new QuizRecyclerAdapter(getActivity(), quizzes);
            recyclerQuizzes.setAdapter(quizRecyclerAdapter);
            loadFirstPage();
        } else {
            Parcelable state = savedInstanceState.getParcelable(KEY_SAVED_INSTANCE);
            quizzesLayoutManager.onRestoreInstanceState(state);
        }

        recyclerQuizzes.addOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onScrolledToEnd() {
                Log.d("Scroll listener", "I have reached the end of the list. Load more data!");
                loadNextPage();
            }
        });


    }

    private void loadNextPage() {
        if (!quizPageIterator.isLastPage()) {
            quizPageIterator.getNextPage()
                    .subscribe(quizzes -> {
                        this.quizzes.addAll(quizzes);
                        quizRecyclerAdapter.onDataUpdate();
                    });
        }
    }

    private void loadFirstPage() {
        quizPageIterator.getFirstPage()
                .subscribe(quizzes -> {
                    this.quizzes.addAll(quizzes);
                    quizRecyclerAdapter.onDataUpdate();
                });
    }

    private void displayError(String error) {
        Toast errorToast = Toast.makeText(getActivity(), error, Toast.LENGTH_LONG);
        errorToast.show();
    }

}

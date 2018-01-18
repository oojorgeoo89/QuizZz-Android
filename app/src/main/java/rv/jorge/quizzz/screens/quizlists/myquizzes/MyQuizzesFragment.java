package rv.jorge.quizzz.screens.quizlists.myquizzes;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import rv.jorge.quizzz.QuizApplication;
import rv.jorge.quizzz.R;
import rv.jorge.quizzz.screens.quizlists.QuizListFragment;
import rv.jorge.quizzz.screens.quizlists.QuizListRecyclerAdapter;
import rv.jorge.quizzz.screens.quizlists.QuizListViewModel;

public class MyQuizzesFragment extends QuizListFragment {

    private static final String TAG = "MyQuizzes";
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Injecting dependencies in Fragment
        ((QuizApplication) getActivity().getApplication())
                .getComponent()
                .inject(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected QuizListViewModel getViewModel() {
        return  ViewModelProviders.of(this, viewModelFactory).get(MyQuizzesViewModel.class);
    }

    @Override
    protected QuizListRecyclerAdapter.OnClickListener getOnClickListener() {
        return position -> {
            Log.d(TAG, "Got a click on My Quizzes Fragment on position " + position);
        };
    }

}

package rv.jorge.quizzz.screens;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rv.jorge.quizzz.R;
import rv.jorge.quizzz.service.QuizService;

public class HomeFragment extends Fragment {

    private Fragment fragment;
    private FragmentManager fragmentManager;

    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeBoxesView();
    }


    private void initializeBoxesView() {
        Log.d("HomeFragment", "Loading the QuizList fragment");

        fragment = new QuizListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(QuizListFragment.ARGS_QUERY_TYPE, QuizService.QueryType.PUBLIC_ALL_QUIZZES);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_main_fragment, fragment);
        fragmentTransaction.commit();
    }

}

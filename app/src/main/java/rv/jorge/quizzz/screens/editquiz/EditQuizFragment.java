package rv.jorge.quizzz.screens.editquiz;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import rv.jorge.quizzz.QuizApplication;
import rv.jorge.quizzz.R;
import rv.jorge.quizzz.screens.support.InternalStatus;

public class EditQuizFragment extends Fragment {

    public static final String TAG = "EditQuizFragment";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    EditQuizViewModel editQuizViewModel;

    EditText quizTitle;
    EditText quizDescription;
    Button submitQuiz;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Binding UI
        quizTitle = (EditText) view.findViewById(R.id.quiz_title);
        quizDescription = (EditText) view.findViewById(R.id.quiz_description);
        submitQuiz = (Button) view.findViewById(R.id.submit_quiz);

        // Injecting dependencies in Fragment
        ((QuizApplication) getActivity().getApplication())
                .getComponent()
                .inject(this);

        // Binding View Models
        editQuizViewModel = ViewModelProviders.of(this, viewModelFactory).get(EditQuizViewModel.class);

        // Setting up behavior
        editQuizViewModel.getIsQuizSavedSuccessfully().observe(this, status -> {
            if (status == InternalStatus.OK) {
                Toast.makeText(getActivity(), getString(R.string.saved_quiz), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), getString(R.string.general_failure), Toast.LENGTH_LONG).show();
            }
        });

        submitQuiz.setOnClickListener(v -> {
            editQuizViewModel.saveQuiz(quizTitle.getText().toString(), quizDescription.getText().toString());
        });
    }
}

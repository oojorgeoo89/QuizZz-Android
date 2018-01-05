package rv.jorge.quizzz.screens;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rv.jorge.quizzz.QuizApplication;
import rv.jorge.quizzz.R;
import rv.jorge.quizzz.service.QuizService;
import rv.jorge.quizzz.service.support.HttpConstants;

public class EditQuiz extends Fragment {

    public static final String TAG = "EditQuiz";
    EditText quizTitle;
    EditText quizDescription;
    Button submitQuiz;

    QuizService quizService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        quizTitle = (EditText) view.findViewById(R.id.quiz_title);
        quizDescription = (EditText) view.findViewById(R.id.quiz_description);
        submitQuiz = (Button) view.findViewById(R.id.submit_quiz);

        quizService = QuizApplication.get(getActivity()).getQuizService();

        submitQuiz.setOnClickListener(v -> {
            quizService.createQuiz(quizTitle.getText().toString(), quizDescription.getText().toString())
                    .subscribe(populatedQuizResponse -> {
                        if (populatedQuizResponse.code() == HttpConstants.CREATED) {
                            Toast.makeText(getActivity(), getString(R.string.saved_quiz), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.general_failure), Toast.LENGTH_LONG).show();
                        }

                    }, throwable -> {
                        Log.d(TAG, "Request Error: " + throwable.getMessage());
                    });
        });
    }
}

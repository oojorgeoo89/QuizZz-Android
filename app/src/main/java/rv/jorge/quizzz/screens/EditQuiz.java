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

public class EditQuiz extends Fragment {

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
                    .subscribe(populatedQuiz -> {
                        Toast.makeText(getActivity(), "Quiz created with ID " + populatedQuiz.getId(), Toast.LENGTH_LONG).show();
                    }, throwable -> {
                        Log.d("EditQuiz", "Request Error: " + throwable.getMessage());
                    });
        });
    }
}

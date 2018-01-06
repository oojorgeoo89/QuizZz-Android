package rv.jorge.quizzz.screens;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rv.jorge.quizzz.QuizApplication;
import rv.jorge.quizzz.R;
import rv.jorge.quizzz.service.UserService;
import rv.jorge.quizzz.service.support.HttpConstants;

public class SignupFragment extends Fragment {

    EditText username;
    EditText email;
    EditText password;
    EditText repeatPassword;
    Button submitSignup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserService userService = QuizApplication.get(getActivity()).getUserService();

        username = (EditText) view.findViewById(R.id.sign_up_username);
        email = (EditText) view.findViewById(R.id.sign_up_email);
        password = (EditText) view.findViewById(R.id.sign_up_password);
        repeatPassword = (EditText) view.findViewById(R.id.sign_up_repeat_password);
        submitSignup = (Button) view.findViewById(R.id.submit_sign_up);

        submitSignup.setOnClickListener(v -> {
            if (username.getText().toString().equals("")) {
                Toast.makeText(getActivity(), getString(R.string.empty_username_warning), Toast.LENGTH_LONG).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                Toast.makeText(getActivity(), getString(R.string.email_wrong_format_warning), Toast.LENGTH_LONG).show();
                return;
            }

            if (!password.getText().toString().equals(repeatPassword.getText().toString())) {
                Toast.makeText(getActivity(), getString(R.string.passwords_dont_match_warning), Toast.LENGTH_LONG).show();
                return;
            }

            userService.signup(username.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString())
            .subscribe(response -> {

                switch (response.code()) {
                    case HttpConstants.OK:
                    case HttpConstants.CREATED:
                        Toast.makeText(getActivity(), getString(R.string.signup_successful), Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                        break;
                    case HttpConstants.CONFLICT:
                        Toast.makeText(getActivity(), getString(R.string.signup_user_exists), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getActivity(), getString(R.string.unknown_error), Toast.LENGTH_LONG).show();
                }

            }, throwable -> {
                Toast.makeText(getActivity(), getString(R.string.unknown_error), Toast.LENGTH_LONG).show();
            });

        });
    }
}

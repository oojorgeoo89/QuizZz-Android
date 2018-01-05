package rv.jorge.quizzz.screens;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import rv.jorge.quizzz.QuizApplication;
import rv.jorge.quizzz.R;
import rv.jorge.quizzz.service.UserService;

public class LoginFragment extends Fragment {

    EditText username;
    EditText password;
    Button submitLoginButton;
    TextView signIn;
    TextView forgotPassword;

    SuccessfulLoginListener successfulLoginCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserService userService = QuizApplication.get(getActivity()).getUserService();

        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        submitLoginButton = (Button) view.findViewById(R.id.submit_login);
        signIn = (TextView) view.findViewById(R.id.sign_in);
        forgotPassword = (TextView) view.findViewById(R.id.forgot_password);

        submitLoginButton.setOnClickListener(v -> {
            userService.login(username.getText().toString(), password.getText().toString())
            .subscribe(user -> {
                successfulLoginCallback.userHasLoggedIn();
            }, throwable -> {
                Toast.makeText(getActivity(), getString(R.string.user_login_failed), Toast.LENGTH_LONG).show();
            });
        });

        signIn.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Sign In Screen not yet implemented", Toast.LENGTH_LONG).show();
        });

        forgotPassword.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Forgot Password Screen not yet implemented", Toast.LENGTH_LONG).show();
        });
    }

    // For API < 24
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        checkParentActivity(activity);
    }

    // For API >= 24
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        checkParentActivity(context);
    }

    private void checkParentActivity(Context context) {
        if (context instanceof SuccessfulLoginListener) {
            successfulLoginCallback = (SuccessfulLoginListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SuccessfulLoginListener");
        }
    }

    public interface SuccessfulLoginListener {
        void userHasLoggedIn();
    }
}

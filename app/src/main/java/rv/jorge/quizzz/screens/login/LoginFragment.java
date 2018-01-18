package rv.jorge.quizzz.screens.login;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import rv.jorge.quizzz.QuizApplication;
import rv.jorge.quizzz.R;
import rv.jorge.quizzz.screens.login.ForgotPassword.ForgotPasswordFragment;
import rv.jorge.quizzz.screens.login.signup.SignupFragment;
import rv.jorge.quizzz.screens.support.FragmentUmbrella;
import rv.jorge.quizzz.screens.support.InternalStatus;

//import android.app.Fragment;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    LoginViewModel loginViewModel;

    EditText username;
    EditText password;
    Button submitLoginButton;
    TextView signIn;
    TextView forgotPassword;

    private FragmentUmbrella fragmentUmbrella;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Binding UI
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        submitLoginButton = (Button) view.findViewById(R.id.submit_login);
        signIn = (TextView) view.findViewById(R.id.sign_in);
        forgotPassword = (TextView) view.findViewById(R.id.forgot_password);

        // Injecting dependencies in Fragment
        ((QuizApplication) getActivity().getApplication())
                .getComponent()
                .inject(this);

        // Binding View Models
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);

        // Setting up behavior
        loginViewModel.getIsLoginSuccessfulObservable()
                .observe(this, isLoginSuccessful -> {
                    if (isLoginSuccessful == InternalStatus.OK) {
                        Log.d(TAG, "Logged in successfully");
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.user_login_failed), Toast.LENGTH_LONG).show();
                    }
                });

        submitLoginButton.setOnClickListener(v -> {
            loginViewModel.login(username.getText().toString(), password.getText().toString());
        });

        signIn.setOnClickListener(v -> {
            fragmentUmbrella.addFragmentToStack(new SignupFragment());
        });

        forgotPassword.setOnClickListener(v -> {
            fragmentUmbrella.addFragmentToStack(new ForgotPasswordFragment());
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

}

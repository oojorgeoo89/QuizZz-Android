package rv.jorge.quizzz.screens.login.ForgotPassword;

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

public class ForgotPasswordFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    ForgotPasswordViewModel forgotPasswordViewModel;

    EditText email;
    Button submitEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Binding UI
        email = (EditText) view.findViewById(R.id.email);
        submitEmail = (Button) view.findViewById(R.id.submit_forgot_password);

        // Injecting dependencies in Fragment
        ((QuizApplication) getActivity().getApplication())
                .getComponent()
                .inject(this);

        // Binding View Models
        forgotPasswordViewModel = ViewModelProviders.of(this, viewModelFactory).get(ForgotPasswordViewModel.class);

        // Setting up behavior
        forgotPasswordViewModel.getIsRequestSuccessfulObservable().observe(this, status -> {
            switch (status) {
                case OK:
                    Toast.makeText(getActivity(), R.string.forgot_password_success, Toast.LENGTH_LONG).show();
                    break;
                case NOT_FOUND:
                    Toast.makeText(getActivity(), R.string.forgot_password_user_not_exist, Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(getActivity(), R.string.forgot_password_faiulre, Toast.LENGTH_LONG).show();
            }
        });

        submitEmail.setOnClickListener(v -> {
            forgotPasswordViewModel.sendForgotPasswordRequest(email.getText().toString());
        });
    }
}

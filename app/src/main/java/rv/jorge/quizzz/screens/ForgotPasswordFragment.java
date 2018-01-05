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

public class ForgotPasswordFragment extends Fragment {

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

        email = (EditText) view.findViewById(R.id.email);
        submitEmail = (Button) view.findViewById(R.id.submit_forgot_password);

        UserService userService = QuizApplication.get(getActivity()).getUserService();

        submitEmail.setOnClickListener(v -> {
            userService.forgotMyPassword(email.getText().toString())
                    .subscribe(response -> {
                        switch (response.code()) {
                            case HttpConstants.OK:
                                Toast.makeText(getActivity(), R.string.forgot_password_success, Toast.LENGTH_LONG).show();
                                break;
                            case HttpConstants.NOT_FOUND:
                                Toast.makeText(getActivity(), R.string.forgot_password_user_not_exist, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(getActivity(), R.string.forgot_password_faiulre, Toast.LENGTH_LONG).show();
                        }


                    }, throwable -> {
                        Toast.makeText(getActivity(), R.string.forgot_password_faiulre, Toast.LENGTH_LONG).show();
                    });
        });
    }
}

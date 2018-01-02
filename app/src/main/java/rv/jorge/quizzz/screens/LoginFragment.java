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
import rv.jorge.quizzz.service.UserService;

public class LoginFragment extends Fragment {

    EditText username;
    EditText password;
    Button submitLoginButton;

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

        submitLoginButton.setOnClickListener(v -> {
            userService.login(username.getText().toString(), password.getText().toString())
            .subscribe(user -> {
                Toast.makeText(getActivity(), "Logged in as " + user.getUsername(), Toast.LENGTH_LONG).show();
            }, throwable -> {
                Log.d("LoginFragment", "Request Error: " + throwable.getMessage());
            });
        });

    }
}

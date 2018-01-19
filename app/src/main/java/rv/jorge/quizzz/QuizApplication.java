package rv.jorge.quizzz;

import android.app.Activity;
import android.app.Application;

import rv.jorge.quizzz.di.DaggerQuizApplicationComponent;
import rv.jorge.quizzz.di.QuizApplicationComponent;
import rv.jorge.quizzz.di.QuizApplicationModule;

public class QuizApplication extends Application {

    private QuizApplicationComponent component;


    public static QuizApplication get(Activity activity) {
        return (QuizApplication) activity.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerQuizApplicationComponent.builder()
                .quizApplicationModule(new QuizApplicationModule(getApplicationContext()))
                .build();
    }

    public QuizApplicationComponent getComponent() {
        return component;
    }

}

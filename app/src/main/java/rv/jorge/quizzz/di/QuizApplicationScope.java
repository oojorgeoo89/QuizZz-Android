package rv.jorge.quizzz.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by jorgerodriguez on 28/12/17.
 */
@Scope
@Retention(RetentionPolicy.CLASS)
public @interface QuizApplicationScope {
}

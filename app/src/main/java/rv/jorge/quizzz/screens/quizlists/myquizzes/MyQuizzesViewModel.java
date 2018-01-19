package rv.jorge.quizzz.screens.quizlists.myquizzes;

import rv.jorge.quizzz.screens.quizlists.QuizListViewModel;
import rv.jorge.quizzz.service.QuizService;

public class MyQuizzesViewModel extends QuizListViewModel {

    public MyQuizzesViewModel(QuizService quizService) {
        super(quizService);
    }

    @Override
    protected QuizService.QueryType getQueryType() {
        return QuizService.QueryType.MY_QUIZZES;
    }

}

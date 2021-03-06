package rv.jorge.quizzz.screens.quizlists.home;

import rv.jorge.quizzz.screens.quizlists.QuizListViewModel;
import rv.jorge.quizzz.service.QuizService;

public class HomeViewModel extends QuizListViewModel {

    public HomeViewModel(QuizService quizService) {
        super(quizService);
    }

    @Override
    protected QuizService.QueryType getQueryType() {
        return QuizService.QueryType.PUBLIC_ALL_QUIZZES;
    }

}

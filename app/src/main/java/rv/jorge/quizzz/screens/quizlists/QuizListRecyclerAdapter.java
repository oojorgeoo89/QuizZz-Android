package rv.jorge.quizzz.screens.quizlists;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rv.jorge.quizzz.R;
import rv.jorge.quizzz.model.Quiz;

/**
 * Created by jorgerodriguez on 19/08/17.
 */

public class QuizListRecyclerAdapter extends RecyclerView.Adapter<QuizListRecyclerAdapter.ViewHolder> {


    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<Quiz> quizzes;
    private final OnClickListener onClickListener;

    public QuizListRecyclerAdapter(Context context, List<Quiz> quizzes, OnClickListener onClickListener) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.quizzes = quizzes;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.quiz_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Quiz quiz = quizzes.get(position);
        holder.setName(quiz.getName());
        holder.setDescription(quiz.getDescription());
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    public void onDataUpdate() {
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameView;
        private final TextView descriptionView;
        private int quizPosition;

        public ViewHolder(View itemView) {
            super(itemView);

            nameView = (TextView) itemView.findViewById(R.id.text_quiz_name);
            descriptionView = (TextView) itemView.findViewById(R.id.text_quiz_description);

            itemView.setOnClickListener(v -> {
                onClickListener.onClick(this.getAdapterPosition());
            });
        }

        public void setName(String name) {
            this.nameView.setText(name);
        }

        public void setDescription(String description) {
            this.descriptionView.setText(description);
        }

        public void setPosition(int quizPosition) {
            this.quizPosition = quizPosition;
        }
    }

    public interface OnClickListener {
        void onClick(int position);
    }
}

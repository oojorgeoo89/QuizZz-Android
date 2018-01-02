package rv.jorge.quizzz.screens;

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

public class QuizRecyclerAdapter extends RecyclerView.Adapter<QuizRecyclerAdapter.ViewHolder> {


    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<Quiz> quizzes;

    public QuizRecyclerAdapter(Context context, List<Quiz> quizzes) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.quizzes = quizzes;
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    TODO: Handle clicks on items
                    Intent intent = new Intent(context, ShowBox.class);
                    intent.putExtra(ShowBox.BOX_POSITION, getQuizPosition());

                    context.startActivity(intent);
                    */
                }
            });
        }

        public TextView getNameView() {
            return nameView;
        }

        public void setName(String name) {
            this.nameView.setText(name);
        }

        public TextView getDescriptionView() {
            return descriptionView;
        }

        public void setDescription(String description) {
            this.descriptionView.setText(description);
        }

        public int getQuizPosition() {
            return quizPosition;
        }

        public void setPosition(int quizPosition) {
            this.quizPosition = quizPosition;
        }
    }

}

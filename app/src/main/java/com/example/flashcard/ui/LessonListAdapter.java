package com.example.flashcard.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcard.R;
import com.example.flashcard.data.Lesson;
public class LessonListAdapter extends ListAdapter<Lesson, LessonListAdapter.LessonViewHolder> {
    private OnItemClickListener listener;
    public LessonListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Lesson> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Lesson>() {
                @Override
                public boolean areItemsTheSame(@NonNull Lesson oldItem, @NonNull Lesson newItem) {
                    return oldItem.getLessonId() == newItem.getLessonId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Lesson oldItem, @NonNull Lesson newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }
            };

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson current = getItem(position);
        holder.textViewName.setText(current.getName());
    }
    class LessonViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewName;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewLessonName);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(pos));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Lesson lesson);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}

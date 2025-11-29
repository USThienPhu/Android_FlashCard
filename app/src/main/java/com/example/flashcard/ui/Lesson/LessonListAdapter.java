package com.example.flashcard.ui.Lesson;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.flashcard.LessonListActivity;
import com.example.flashcard.R;
import com.example.flashcard.data.Lesson;
public class LessonListAdapter extends ListAdapter<Lesson, LessonViewHolder> {
    private LessonClickListener listener;
    public LessonListAdapter(LessonClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Lesson> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Lesson oldItem, @NonNull Lesson newItem) {
                    return oldItem.getLessonId() == newItem.getLessonId();
                }
                @Override
                public boolean areContentsTheSame(@NonNull Lesson oldItem, @NonNull Lesson newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }
            };


    public void setOnItemClickListener(LessonClickListener listener) {
        this.listener = listener;
    }

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
        holder.bind(current, listener);
    }

}

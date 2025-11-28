package com.example.flashcard.ui.FlashCard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.flashcard.R;
import com.example.flashcard.data.Flashcard;

public class FlashcardListAdapter
        extends ListAdapter<Flashcard, FlashcardViewHolder> {

    private final FlashcardClickListener listener;

    public FlashcardListAdapter(FlashcardClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Flashcard> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Flashcard>() {
                @Override
                public boolean areItemsTheSame(@NonNull Flashcard oldItem, @NonNull Flashcard newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Flashcard oldItem, @NonNull Flashcard newItem) {
                    return oldItem.getFrontText().equals(newItem.getFrontText()) &&
                            oldItem.getBackText().equals(newItem.getBackText());
                }
            };

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new FlashcardViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}

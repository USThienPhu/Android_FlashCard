package com.example.flashcard.ui.FlashCard;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcard.R;
import com.example.flashcard.data.Flashcard;

public class FlashcardViewHolder extends RecyclerView.ViewHolder {

    private final TextView textViewFront;
    private boolean isShowingFront = true;

    public FlashcardViewHolder(View itemView, FlashcardClickListener listener) {
        super(itemView);
        textViewFront = itemView.findViewById(R.id.textViewFront);

        itemView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            listener.onItemClick(null); // Nếu sau này cần callback

            Flashcard card = (Flashcard) v.getTag();   // Adapter sẽ setTag()
            flipCard(itemView, card);
        });
    }

    public void bind(Flashcard card) {
        textViewFront.setText(card.getFrontText());
        itemView.setTag(card); // Gắn data vào itemView để dùng lại
        isShowingFront = true; // Reset trạng thái khi view được tái sử dụng
    }

    private void flipCard(View view, Flashcard card) {
        view.animate().rotationY(90f).setDuration(200)
                .withEndAction(() -> {
                    if (isShowingFront) {
                        textViewFront.setText(card.getBackText());
                    } else {
                        textViewFront.setText(card.getFrontText());
                    }

                    isShowingFront = !isShowingFront;
                    view.setRotationY(-90f);
                    view.animate().rotationY(0f).setDuration(200).start();
                }).start();
    }
}

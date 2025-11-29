package com.example.flashcard.ui.FlashCard;

import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_flashcard, popupMenu.getMenu());
                // (Mẹo nhỏ: Nếu bạn muốn hiện Icon trên PopupMenu, hãy xem phần Note bên dưới)
                // 3. Xử lý sự kiện click (Dùng ID thay vì so sánh chuỗi String)
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Lấy thẻ hiện tại đang được nhấn giữ
                        // (Bạn đã setTag trong hàm bind nên lấy ra rất dễ)
                        Flashcard currentCard = (Flashcard) v.getTag();

                        if (currentCard == null) return false; // Kiểm tra an toàn
                        int id = item.getItemId();
                        if (id == R.id.action_edit) {
                            // Báo cho Activity biết: "Sửa thẻ này đi"
                            listener.onEditClick(currentCard);
                            return true;
                        }
                        else if (id == R.id.action_delete) {
                            // Báo cho Activity biết: "Xóa thẻ này đi"
                            listener.onDeleteClick(currentCard);
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true; // Trả về true để báo là sự kiện đã được xử lý xong
            }
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

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
import com.example.flashcard.data.Flashcard;

// Kế thừa ListAdapter thay vì RecyclerView.Adapter thường
public class FlashcardListAdapter extends ListAdapter<Flashcard, FlashcardListAdapter.FlashcardViewHolder> {

    // Interface để xử lý sự kiện khi click vào một dòng
    private OnItemClickListener listener;

    public FlashcardListAdapter() {
        super(DIFF_CALLBACK);
    }

    // Bộ so sánh dữ liệu cũ và mới
    private static final DiffUtil.ItemCallback<Flashcard> DIFF_CALLBACK = new DiffUtil.ItemCallback<Flashcard>() {
        @Override
        public boolean areItemsTheSame(@NonNull Flashcard oldItem, @NonNull Flashcard newItem) {
            // So sánh ID (Khóa chính)
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Flashcard oldItem, @NonNull Flashcard newItem) {
            // So sánh nội dung (để xem có cần vẽ lại giao diện không)
            return oldItem.getFrontText().equals(newItem.getFrontText()) &&
                    oldItem.getBackText().equals(newItem.getBackText());
        }
    };

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp layout recyclerview_item.xml vào
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new FlashcardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        // Lấy flashcard tại vị trí hiện tại và đổ dữ liệu
        Flashcard currentFlashcard = getItem(position);
        holder.textViewFront.setText(currentFlashcard.getFrontText());
    }

    // Hàm để lấy Flashcard tại vị trí cụ thể (dùng khi vuốt để xóa)
    public Flashcard getFlashcardAt(int position) {
        return getItem(position);
    }

    // ViewHolder: Nắm giữ các thành phần giao diện
    class FlashcardViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewFront;

        public FlashcardViewHolder(View itemView) {
            super(itemView);
            textViewFront = itemView.findViewById(R.id.textViewFront);

            // Bắt sự kiện click vào cả cái thẻ
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    // Interface cho sự kiện click
    public interface OnItemClickListener {
        void onItemClick(Flashcard flashcard);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
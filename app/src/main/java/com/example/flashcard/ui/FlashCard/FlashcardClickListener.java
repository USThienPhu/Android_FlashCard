package com.example.flashcard.ui.FlashCard;

import com.example.flashcard.data.Flashcard;

public interface FlashcardClickListener {
    void onItemClick(Flashcard flashcard);
    void onEditClick(Flashcard flashcard);   // Khi chọn menu "Sửa"
    void onDeleteClick(Flashcard flashcard); // Khi chọn menu "Xóa"
}
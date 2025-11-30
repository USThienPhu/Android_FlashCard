package com.example.flashcard.data;
import java.util.List;
public class DatabaseInitializer {
    // Hàm static để chạy logic nạp dữ liệu
    public static void populateAsync(AppDatabase db) {
        LessonDao lessonDao = db.lessonDao();
        FlashcardDao flashcardDao = db.flashcardDao();

        // Lấy danh sách dữ liệu thô
        List<SampleData.LessonItem> allData = SampleData.getData();

        for (SampleData.LessonItem lessonItem : allData) {
            // 1. Chèn Lesson trước -> Room trả về ID của dòng vừa chèn
            Lesson lesson = new Lesson(lessonItem.name);
            long lessonId = lessonDao.insert(lesson);

            // 2. Dùng ID đó để chèn các Flashcard con
            for (SampleData.FlashcardItem cardItem : lessonItem.cards) {
                // Lưu ý ép kiểu (int) vì lessonOwnerId của bạn là int
                Flashcard card = new Flashcard(cardItem.front, cardItem.back, (int) lessonId);
                flashcardDao.insert(card);
            }
        }
    }
}

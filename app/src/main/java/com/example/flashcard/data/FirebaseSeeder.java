package com.example.flashcard.data;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
public class FirebaseSeeder {
    public static void  uploadDataToFirestore(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (SampleData.LessonItem lesson : SampleData.getData()){
            Map<String, Object> lessonData = new HashMap<>();
            lessonData.put("name", lesson.name);

            lessonData.put("order", lesson.order);

            db.collection("lessons")
                    .add(lessonData)
                    .addOnSuccessListener(documentReference -> {
                        String firebaseLessonId = documentReference.getId(); // Lấy ID chuỗi của Firebase
                        Log.d("Firebase", "Uploaded Lesson: " + lesson.name);

                        // 3. Đẩy Flashcard con của Lesson này lên
                        for (SampleData.FlashcardItem card : lesson.cards) {
                            Map<String, Object> cardMap = new HashMap<>();
                            cardMap.put("front", card.front);
                            cardMap.put("back", card.back);
                            cardMap.put("lessonId", firebaseLessonId); // Liên kết với ID cha trên mây

                            db.collection("flashcards").add(cardMap);
                        }
                    });
        }
    }
}

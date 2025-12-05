package com.example.flashcard.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlashcardRepository {
    // QUAN TRỌNG: Tag này khớp với bộ lọc Logcat của bạn
    private static final String TAG = "DEBUG_FIREBASE";

    private FlashcardDao mFlashcardDao;
    private LessonDao mLessonDao;
    private final FirebaseFirestore firestore;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private LiveData<List<Flashcard>> mAllFlashcards;

    public FlashcardRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.firestore = FirebaseFirestore.getInstance();
        mFlashcardDao = db.flashcardDao();
        mLessonDao = db.lessonDao();
        mAllFlashcards = mFlashcardDao.getAllFlashcards();

        // Gọi đồng bộ ngay khi khởi tạo
        syncDataFromFirebase();
    }

    public LiveData<List<Flashcard>> getAllFlashcards() {
        return mAllFlashcards;
    }

    public LiveData<List<Flashcard>> getFlashcardsByLessonId(int lessonId) {
        return mFlashcardDao.getFlashcardsByLessonId(lessonId);
    }

    public LiveData<List<Flashcard>> searchFlashcards(String query) {
        return mFlashcardDao.searchFlashcards(query);
    }

    // --- LOGIC ĐỒNG BỘ: Firebase (String ID) -> Room (Int ID) ---
    public void syncDataFromFirebase() {
        Log.d(TAG, ">>> 1. BẮT ĐẦU ĐỒNG BỘ (Sync started)...");

        firestore.collection("lessons").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int size = task.getResult().size();
                        Log.d(TAG, ">>> 2. Tìm thấy " + size + " bài học (Lessons) trên Firebase.");

                        if(size == 0) {
                            Log.w(TAG, "!!! CẢNH BÁO: Không có Lesson nào trên Firebase để tải!");
                            return;
                        }

                        executor.execute(() -> {
                            Map<String, Integer> mapFirebaseIdToLocalId = new HashMap<>();

                            for (DocumentSnapshot doc : task.getResult()) {
                                String firebaseId = doc.getId();
                                String name = doc.getString("name");

                                // Insert Lesson vào Room để lấy ID kiểu Int
                                Lesson lesson = new Lesson(name);
                                long localId = mLessonDao.insert(lesson); // Insert trả về ID mới

                                Log.d(TAG, "   + Đã lưu Lesson: " + name + " (ID máy: " + localId + ")");

                                mapFirebaseIdToLocalId.put(firebaseId, (int) localId);
                            }

                            // Sau khi có Lesson ID, tải tiếp Flashcards
                            fetchFlashcards(mapFirebaseIdToLocalId);
                        });
                    } else {
                        Log.e(TAG, "!!! LỖI: Không tải được Lessons: ", task.getException());
                    }
                });
    }

    private void fetchFlashcards(Map<String, Integer> mapIds) {
        Log.d(TAG, ">>> 3. Bắt đầu tải Flashcards...");

        firestore.collection("flashcards").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, ">>> 4. Tìm thấy " + task.getResult().size() + " thẻ trên Firebase.");

                        executor.execute(() -> {
                            int count = 0;
                            for (DocumentSnapshot doc : task.getResult()) {
                                String firebaseLessonId = doc.getString("lessonId");
                                String front = doc.getString("front");
                                String back = doc.getString("back");

                                // Tìm xem thẻ này thuộc bài học nào (đã convert sang Int chưa)
                                if (mapIds.containsKey(firebaseLessonId)) {
                                    int localOwnerId = mapIds.get(firebaseLessonId);

                                    Flashcard fc = new Flashcard(front, back, localOwnerId);
                                    mFlashcardDao.insert(fc);
                                    count++;
                                }
                            }
                            Log.d(TAG, ">>> 5. HOÀN TẤT: Đã lưu thành công " + count + " thẻ vào máy.");
                        });
                    } else {
                        Log.e(TAG, "!!! LỖI: Không tải được Flashcards", task.getException());
                    }
                });
    }

    public void insert(Flashcard fc) { executor.execute(() -> mFlashcardDao.insert(fc)); }
    public void update(Flashcard fc) { executor.execute(() -> mFlashcardDao.update(fc)); }
    public void delete(Flashcard fc) { executor.execute(() -> mFlashcardDao.delete(fc)); }
}
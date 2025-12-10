package com.example.flashcard.data;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlashcardRepository {
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

        checkDataFlashCard();

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

    // --- LOGIC ĐỒNG BỘ: Chỉ sync Flashcards (Lessons được sync bởi LessonRepository) ---
    public void syncDataFromFirebase() {
        Log.d(TAG, ">>> [FlashcardRepository] Starting flashcard sync from Firestore...");
        
        firestore.collection("flashcards").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int size = task.getResult().size();
                        Log.d(TAG, ">>> [FlashcardRepository] Found " + size + " flashcards on Firestore.");
                        
                        if (size == 0) {
                            Log.w(TAG, "!!! [FlashcardRepository] WARNING: No flashcards found on Firestore!");
                            return;
                        }
                        
                        executor.execute(() -> {
                            int count = 0;
                            for (DocumentSnapshot doc : task.getResult()) {
                                String lessonId = doc.getString("lessonId");
                                String front = doc.getString("front");
                                String back = doc.getString("back");
                                
                                // Tìm lesson trong database local bằng cách query Firestore để lấy tên
                                if (lessonId != null) {
                                    // Lấy thông tin lesson từ Firestore để tìm tên
                                    firestore.collection("lessons").document(lessonId).get()
                                            .addOnSuccessListener(lessonDoc -> {
                                                if (lessonDoc.exists()) {
                                                    String lessonName = lessonDoc.getString("name");
                                                    
                                                    // Tìm lesson trong Room database bằng tên
                                                    executor.execute(() -> {
                                                        Lesson localLesson = mLessonDao.getLessonByName(lessonName);
                                                        
                                                        if (localLesson != null) {
                                                            Flashcard fc = new Flashcard(front, back, localLesson.getLessonId());
                                                            mFlashcardDao.insert(fc);
                                                            Log.d(TAG, "   + [FlashcardRepository] Saved flashcard for lesson: " + lessonName);
                                                        } else {
                                                            Log.w(TAG, "   ! [FlashcardRepository] Lesson not found: " + lessonName);
                                                        }
                                                    });
                                                }
                                            });
                                    count++;
                                }
                            }
                            Log.d(TAG, ">>> [FlashcardRepository] Flashcard sync initiated for " + count + " flashcards.");
                        });
                    } else {
                        Log.e(TAG, "!!! [FlashcardRepository] ERROR: Failed to fetch flashcards", task.getException());
                    }
                });
    }

    public void checkDataFlashCard()
    {
        executor.execute(()-> {
            int no  = mFlashcardDao.noFlashcard();
            if (no == 0){
                try {
                    Thread.sleep(1000);
                    syncDataFromFirebase();
                } catch (InterruptedException e) {
                    Log.d(TAG, "Can not get data");
                }
            }
            else
            {
                Log.d(TAG, "Already");
            }

        });
    }

    public void insert(Flashcard fc) {
        executor.execute(() ->
        {
            mFlashcardDao.insert(fc);
            int lsId = fc.getLessonOwnerId();
            String firebaseLessonId = mLessonDao.getLessonFirebaseId(lsId);

            uploadFlashcard(fc, firebaseLessonId);
        });

    }
    public void update(Flashcard fc) { executor.execute(() -> mFlashcardDao.update(fc)); }
    public void delete(Flashcard fc) { executor.execute(() -> mFlashcardDao.delete(fc)); }

    public void uploadFlashcard(Flashcard fc, String lessonId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> cardMap = new HashMap<>();
        cardMap.put("front", fc.getFrontText());
        cardMap.put("back", fc.getBackText());
        cardMap.put("lessonId", lessonId);
        db.collection("flashcards").add(cardMap);
        Log.d("Skibidi", "Upload success");
    }

}
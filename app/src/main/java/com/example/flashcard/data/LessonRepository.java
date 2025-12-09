package com.example.flashcard.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LessonRepository {
    private static final String TAG = "LessonRepository"; // Đổi TAG cho gọn

    private LessonDao mLessonDao;
    private SharedPreferences sharedPreferences;
    private final LiveData<List<Lesson>> mAllLessons;
    private final FirebaseFirestore firestore;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LessonRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mLessonDao = db.lessonDao();
        mAllLessons = mLessonDao.getAllLessons();
        this.firestore = FirebaseFirestore.getInstance();

        sharedPreferences = application.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

        // Kiểm tra dữ liệu khi mở app
        checkDataOnInit();
    }

    public LiveData<List<Lesson>> getAllLessons() {
        return mAllLessons;
    }

    // --- CÁC HÀM THAO TÁC CƠ BẢN ---
    public void deleteAllLessons() {
        AppDatabase.databaseWriteExecutor.execute(() -> mLessonDao.deleteAllLessons());
    }

    // Hàm insert 1: Dùng khi sync từ Firebase (đã có object Lesson đầy đủ)
    public void insert(Lesson lesson) {
        AppDatabase.databaseWriteExecutor.execute(() -> mLessonDao.insert(lesson));
    }

    // Hàm insert 2: Dùng khi User thêm tay từ giao diện
    public void insert(String name) {
        String myId = getCurrentUserId();
        if (myId == null) return;

        AppDatabase.databaseWriteExecutor.execute(() -> {
            int currentMax = mLessonDao.getMaxDisplayOrder();
            int newOrder = currentMax + 1;
            // Tạo bài học mới gán với ID của người dùng hiện tại
            Lesson newLesson = new Lesson(name, newOrder, myId);
            mLessonDao.insert(newLesson);

            // TODO: Ở đây nên có thêm hàm pushLessonToFirebase(newLesson)
        });
    }

    public void delete(Lesson lesson) {
        AppDatabase.databaseWriteExecutor.execute(() -> mLessonDao.delete(lesson));
    }

    // --- LOGIC ĐỒNG BỘ DỮ LIỆU (QUAN TRỌNG NHẤT) ---

    // Hàm kiểm tra ban đầu
    private void checkDataOnInit() {
        executor.execute(() -> {
            int count = mLessonDao.noLesson(); // Hãy đảm bảo DAO có hàm đếm này, hoặc dùng noLesson() như bạn
            Log.d(TAG, "Local DB count: " + count);

            // Nếu DB rỗng -> Tải mới
            // Hoặc bạn có thể bỏ điều kiện if (count == 0) để luôn luôn update mới nhất
            if (count == 0) {
                syncLessonsFromFirebase();
            }
        });
    }

    // Hàm điều phối chính
    public void syncLessonsFromFirebase() {
        Log.d(TAG, ">>> STARTING SYNC PROCESS...");
        String myId = getCurrentUserId();

        executor.execute(() -> {
            // BƯỚC 1: Xóa sạch dữ liệu cũ để tránh trùng lặp
            Log.d(TAG, "   + [Step 1] Clearing local database...");
            mLessonDao.deleteAllLessons();

            // BƯỚC 2: Bắt đầu tải dữ liệu (System trước)
            fetchSystemLessons(myId);
        });
    }

    // Tải dữ liệu mẫu (SYSTEM)
    private void fetchSystemLessons(String myId) {
        firestore.collection("lessons")
                .whereEqualTo("userId", "SYSTEM")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        executor.execute(() -> {
                            int count = 0;
                            for (DocumentSnapshot doc : task.getResult()) {
                                saveLessonFromSnapshot(doc, "SYSTEM");
                                count++;
                            }
                            Log.d(TAG, "   + [Step 2] Saved " + count + " SYSTEM lessons.");

                            // BƯỚC 3: Sau khi tải xong System, tải tiếp User (nếu có)
                            if (myId != null && !myId.equals("GUEST_MODE")) {
                                fetchUserLessons(myId);
                            } else {
                                Log.d(TAG, ">>> SYNC COMPLETED (Guest/No User).");
                            }
                        });
                    } else {
                        Log.e(TAG, "!!! Failed to fetch SYSTEM lessons", task.getException());
                    }
                });
    }

    // Tải dữ liệu riêng của User
    private void fetchUserLessons(String userId) {
        firestore.collection("lessons")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        executor.execute(() -> {
                            int count = 0;
                            for (DocumentSnapshot doc : task.getResult()) {
                                saveLessonFromSnapshot(doc, userId);
                                count++;
                            }
                            Log.d(TAG, "   + [Step 3] Saved " + count + " USER lessons.");
                            Log.d(TAG, ">>> SYNC COMPLETED SUCCESSFULLY.");
                        });
                    } else {
                        Log.e(TAG, "!!! Failed to fetch USER lessons", task.getException());
                    }
                });
    }

    // Hàm phụ trợ: Lưu document vào Room (Tránh viết lặp code)
    private void saveLessonFromSnapshot(DocumentSnapshot doc, String ownerId) {
        String name = doc.getString("name");
        Long orderLong = doc.getLong("order");
        int displayOrder = (orderLong != null) ? orderLong.intValue() : 0;

        if (name != null && !name.isEmpty()) {
            // Quan trọng: Truyền ownerId vào để lưu vào Room
            Lesson lesson = new Lesson(name, displayOrder, ownerId);

            // Nếu bạn có trường firebaseId trong Entity thì set luôn
//            lesson.firebaseId = doc.getId();

            mLessonDao.insert(lesson);
        }
    }

    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            boolean isGuest = sharedPreferences.getBoolean("is_guest", false);
            return isGuest ? "GUEST_MODE" : null;
        }
    }
}
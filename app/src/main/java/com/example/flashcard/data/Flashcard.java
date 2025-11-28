package com.example.flashcard.data;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "flashcards",
    foreignKeys = @ForeignKey(
        entity = Lesson.class,
        parentColumns = "lessonId",
        childColumns = "lessonOwnerId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("lessonOwnerId")}
)
public class Flashcard {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String topic;
    private String frontText;
    private String backText;
    private boolean isMemorized;

    private final int lessonOwnerId;

    public Flashcard(String topic, String frontText, String backText, int lessonOwnerId) {
        this.topic = topic;
        this.frontText = frontText;
        this.backText = backText;
        this.isMemorized = false;
        this.lessonOwnerId = lessonOwnerId; 
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getLessonOwnerId() {
        return lessonOwnerId;
    }


    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getFrontText() { return frontText; }
    public void setFrontText(String frontText) { this.frontText = frontText; }

    public String getBackText() { return backText; }
    public void setBackText(String backText) { this.backText = backText; }

    public boolean isMemorized() { return isMemorized; }
    public void setMemorized(boolean memorized) { isMemorized = memorized; }



}

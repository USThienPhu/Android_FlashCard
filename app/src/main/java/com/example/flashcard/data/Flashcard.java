package com.example.flashcard.data;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "flashcards")
public class Flashcard {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String topic;
    private String frontText;
    private String backText;
    private boolean isMemorized;


    public Flashcard(String topic, String frontText, String backText) {
        this.topic = topic;
        this.frontText = frontText;
        this.backText = backText;
        this.isMemorized = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getFrontText() { return frontText; }
    public void setFrontText(String frontText) { this.frontText = frontText; }

    public String getBackText() { return backText; }
    public void setBackText(String backText) { this.backText = backText; }

    public boolean isMemorized() { return isMemorized; }
    public void setMemorized(boolean memorized) { isMemorized = memorized; }



}

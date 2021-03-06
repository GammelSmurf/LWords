package org.example.LWords.Entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "records")
public class Record {
    @Id
    @Column(name = "record_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String phrase;
    @Column(length = 2000)
    private String translations;
    private int progress;
    private LocalDateTime date;
    private int correctAnsCount;
    private int incorrectAnsCount;
    private Boolean isFinished;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Record() {
    }

    public Record(String phrase, String translations, int progress, Boolean isFinished, LocalDateTime date, User user) {
        this.phrase = phrase;
        this.translations = translations;
        this.progress = progress;
        this.date = date;
        this.user = user;
        this.isFinished = isFinished;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public Long getId() {
        return id;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getTranslations() {
        return translations;
    }

    public void setTranslations(String translations) {
        this.translations = translations;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getCorrectAnsCount() {
        return correctAnsCount;
    }

    public void setCorrectAnsCount(int correctAnsCount) {
        this.correctAnsCount = correctAnsCount;
    }

    public int getIncorrectAnsCount() {
        return incorrectAnsCount;
    }

    public void setIncorrectAnsCount(int incorrectAnsCount) {
        this.incorrectAnsCount = incorrectAnsCount;
    }
}

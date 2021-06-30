package org.example.LWords.Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "records")
public class Record {
    public static int ProgressLength;
    @Id
    @Column(name = "record_id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String phrase;
    private String translations;
    private int progress;
    private Date date;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Record() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

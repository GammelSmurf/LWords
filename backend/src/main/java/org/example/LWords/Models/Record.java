package org.example.LWords.Models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "records")
public class Record {
    public static int ProgressLength;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String phrase;
    private String translations;
    private int progress;
    private Date date;

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

package org.example.LWords.Entities;

import javax.persistence.*;
import java.time.DayOfWeek;

@Entity
@Table(name = "statistics")
public class ActivityStatistic {
    @Id
    @Column(name = "statistic_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private DayOfWeek dayOfWeek;
    private int totalCorrectAnsCount;
    private int totalIncorrectAnsCount;
    private int totalLearnedWordsCount;

    public ActivityStatistic() {
    }

    public ActivityStatistic(User user, DayOfWeek dayOfWeek) {
        this.user = user;
        this.dayOfWeek = dayOfWeek;
        totalCorrectAnsCount = 0;
        totalIncorrectAnsCount = 0;
        totalLearnedWordsCount = 0;
    }

    public Long getId() {
        return id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getTotalCorrectAnsCount() {
        return totalCorrectAnsCount;
    }

    public void setTotalCorrectAnsCount(int totalCorrectAnsCount) {
        this.totalCorrectAnsCount = totalCorrectAnsCount;
    }

    public int getTotalIncorrectAnsCount() {
        return totalIncorrectAnsCount;
    }

    public void setTotalIncorrectAnsCount(int totalIncorrectAnsCount) {
        this.totalIncorrectAnsCount = totalIncorrectAnsCount;
    }

    public int getTotalLearnedWordsCount() {
        return totalLearnedWordsCount;
    }

    public void setTotalLearnedWordsCount(int totalLearnedWordsCount) {
        this.totalLearnedWordsCount = totalLearnedWordsCount;
    }
}

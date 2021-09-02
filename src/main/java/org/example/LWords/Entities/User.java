package org.example.LWords.Entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User{
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private boolean enabled;
    private int progressLength;
    private int translationCount;
    private LocalDateTime date;
    private int learnedWordsCount;
    private String hardestWord;
    private int hardestWordIncCount;
    private int statisticNumberOfWeek;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy="user",orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Record> records;

    @OneToMany(mappedBy= "user",orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ActivityStatistic> statistics;

    public User(){ }

    public User(String username, String password, LocalDateTime date, int numberOfWeek){
        this.username = username;
        this.password = password;
        enabled = true;
        progressLength = 10;
        translationCount = 12;
        this.date = date;
        learnedWordsCount = 0;
        hardestWord = "";
        hardestWordIncCount = 0;
        statisticNumberOfWeek = numberOfWeek;
    }
    public int getHardestWordIncCount() {
        return hardestWordIncCount;
    }

    public void setHardestWordIncCount(int hardestWordIncCount) {
        this.hardestWordIncCount = hardestWordIncCount;
    }

    public int getLearnedWordsCount() {
        return learnedWordsCount;
    }

    public void setLearnedWordsCount(int learnedWordsCount) {
        this.learnedWordsCount = learnedWordsCount;
    }

    public String getHardestWord() {
        return hardestWord;
    }

    public void setHardestWord(String hardestWord) {
        this.hardestWord = hardestWord;
    }

    public int getStatisticNumberOfWeek() {
        return statisticNumberOfWeek;
    }

    public void setStatisticNumberOfWeek(int statisticNumberOfWeek) {
        this.statisticNumberOfWeek = statisticNumberOfWeek;
    }

    public void deleteRole(Role role){
        this.roles.remove(role);
    }

    public Set<ActivityStatistic> getStatistics() {
        return statistics;
    }

    public void setStatistics(Set<ActivityStatistic> statistics) {
        this.statistics = statistics;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Set<Record> getRecords() {
        return records;
    }

    public void setRecords(Set<Record> records) {
        this.records = records;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public int getProgressLength() {
        return progressLength;
    }

    public void setProgressLength(int progressLength) {
        this.progressLength = progressLength;
    }

    public int getTranslationCount() {
        return translationCount;
    }

    public void setTranslationCount(int translationCount) {
        this.translationCount = translationCount;
    }
}

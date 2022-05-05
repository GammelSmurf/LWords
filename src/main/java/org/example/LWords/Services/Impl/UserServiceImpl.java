package org.example.LWords.Services.Impl;

import org.example.LWords.Entities.ActivityStatistic;
import org.example.LWords.Entities.Role;
import org.example.LWords.Entities.User;
import org.example.LWords.Entities.Record;
import org.example.LWords.Services.UserService;
import org.example.LWords.repos.ActivityStatisticRepository;
import org.example.LWords.repos.RecordRepository;
import org.example.LWords.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private ActivityStatisticRepository statisticRepository;

    @Override
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.getUserById(id);
        for (Role role : user.getRoles()) {
            user.deleteRole(role);
        }
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(User user) {
        User currentUser = userRepository.getUserById(user.getId());
        ActivityStatistic statistic = statisticRepository.findByDayOfWeekAndUser(DayOfWeek.from(LocalDate.now()), currentUser);
        currentUser.setProgressLength(user.getProgressLength());
        currentUser.setTranslationCount(user.getTranslationCount());

        int progressLength = user.getProgressLength();
        Iterable<Record> records = recordRepository.findByUser(user);
        int learnedWordsCount = 0;
        for (Record record :
                records) {
            if (record.getProgress() >= progressLength && !record.getFinished()) {
                learnedWordsCount++;
                record.setProgress(100);
                record.setFinished(true);
                recordRepository.save(record);
            }
        }
        statistic.setTotalLearnedWordsCount(statistic.getTotalLearnedWordsCount() + learnedWordsCount);
        currentUser.setLearnedWordsCount(currentUser.getLearnedWordsCount() + learnedWordsCount);
        statisticRepository.save(statistic);
        userRepository.save(currentUser);
    }
}

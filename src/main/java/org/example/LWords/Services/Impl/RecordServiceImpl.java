package org.example.LWords.Services.Impl;

import lombok.RequiredArgsConstructor;
import org.example.LWords.Entities.ActivityStatistic;
import org.example.LWords.Entities.Record;
import org.example.LWords.Entities.User;
import org.example.LWords.Services.RecordService;
import org.example.LWords.Services.TranslatorClient;
import org.example.LWords.dto.Responses.StatisticResponse;
import org.example.LWords.repos.ActivityStatisticRepository;
import org.example.LWords.repos.RecordRepository;
import org.example.LWords.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;
    private final ActivityStatisticRepository statisticRepository;
    private final UserRepository userRepository;
    private final TranslatorClient translatorClient;

    public Iterable<Record> getAllRecords(String username) {
        return recordRepository.findByUser(getUser(username));
    }

    public Iterable<Record> getLearningRecords(String username) {
        Iterable<Record> records = recordRepository.findByUser(getUser(username));
        List<Record> listRecords = new ArrayList<>();
        records.forEach(listRecords::add);
        listRecords = listRecords.stream().filter(x -> !x.getFinished()).collect(Collectors.toList());
        Collections.shuffle(listRecords);
        return listRecords;
    }

    public String addRecord(String username, String phrase, int inputProgress) {
        User user = getUser(username);
        if (!recordRepository.existsByPhraseAndUser(phrase, user)) {

            String translations = translatorClient.getTranslations(phrase, user.getTranslationCount());

            int progress;
            boolean isFinished;
            if (inputProgress >= user.getProgressLength()) {
                isFinished = true;
                progress = 100;
            } else if (inputProgress < 0) {
                isFinished = false;
                progress = 0;
            } else {
                isFinished = false;
                progress = inputProgress;
            }

            if (translations != null) {
                Record record = new Record(phrase, translations, progress, isFinished, LocalDateTime.now(), user);
                recordRepository.save(record);
                return "Added successfully";
            } else
                return "Incorrect input";
        } else
            return "This phrase already exists";
    }

    public void removeRecords(List<Long> ids) {
        for (Long id : ids) {
            recordRepository.deleteById(id);
        }
    }

    public void updateRecord(String username, Long id, Record record) {
        User user = getUser(username);
        Record currentRecord = recordRepository.getRecordById(id);
        ActivityStatistic statistic = statisticRepository.findByDayOfWeekAndUser(DayOfWeek.from(LocalDate.now()), user);
        if (currentRecord.getCorrectAnsCount() != record.getCorrectAnsCount() && currentRecord.getIncorrectAnsCount() != record.getIncorrectAnsCount()) {
            statistic.setTotalIncorrectAnsCount(statistic.getTotalIncorrectAnsCount() - 1);
            statistic.setTotalCorrectAnsCount(statistic.getTotalCorrectAnsCount() + 1);
        } else if (currentRecord.getCorrectAnsCount() != record.getCorrectAnsCount()) {
            statistic.setTotalCorrectAnsCount(statistic.getTotalCorrectAnsCount() + 1);
        } else {
            if (record.getIncorrectAnsCount() > user.getHardestWordIncCount()) {
                user.setHardestWordIncCount(record.getIncorrectAnsCount());
                user.setHardestWord(record.getPhrase());
            } else if (record.getPhrase().equals(user.getHardestWord())) {
                user.setHardestWordIncCount(user.getHardestWordIncCount() + 1);
            }
            statistic.setTotalIncorrectAnsCount(statistic.getTotalIncorrectAnsCount() + 1);
        }
        currentRecord.setCorrectAnsCount(record.getCorrectAnsCount());
        currentRecord.setIncorrectAnsCount(record.getIncorrectAnsCount());
        if (record.getFinished()) {
            currentRecord.setFinished(record.getFinished());
            currentRecord.setProgress(100);
            user.setLearnedWordsCount(user.getLearnedWordsCount() + 1);
            statistic.setTotalLearnedWordsCount(statistic.getTotalLearnedWordsCount() + 1);
        } else {
            currentRecord.setProgress(record.getProgress());
        }
        currentRecord.setTranslations(record.getTranslations());
        recordRepository.save(currentRecord);
        statisticRepository.save(statistic);
        userRepository.save(user);
    }

    public int importCSV(String username, MultipartFile file) {
        try {
            String[] records = new String(file.getBytes()).split("\n");
            int successCount = 0;
            for (String record :
                    records) {
                String phrase = record.split(";")[0].toLowerCase().replaceAll("\"", "");
                int progress = Integer.parseInt(record.split(";")[1].replaceAll("[\"\r]", ""));
                if (addRecord(username, phrase, progress).equals("Added successfully"))
                    successCount += 1;
            }
            return successCount;
        } catch (Exception ex) {
            return 0;
        }
    }

    public StatisticResponse getStatistic(String username) {
        User user = getUser(username);
        int completedRecords = 0;
        int newRecords = 0;
        int inProgressRecords = 0;
        Iterable<Record> records = recordRepository.findByUser(user);
        for (Record record :
                records) {
            if (record.getFinished())
                completedRecords += 1;
            else if (record.getCorrectAnsCount() == 0 && record.getIncorrectAnsCount() == 0 && record.getProgress() == 0)
                newRecords += 1;
            else
                inProgressRecords += 1;
        }

        int[] progressStatistic = new int[user.getProgressLength() + 1];
        for (int iterator = 0; iterator < user.getProgressLength(); iterator++) {
            for (Record record :
                    records) {
                if (record.getProgress() == iterator)
                    progressStatistic[iterator] += 1;
            }
        }
        progressStatistic[user.getProgressLength()] = completedRecords;

        Iterable<ActivityStatistic> activityStatistics = statisticRepository.findByUser(user);
        return new StatisticResponse(completedRecords, newRecords, inProgressRecords, activityStatistics, user.getLearnedWordsCount(), user.getHardestWord(), progressStatistic);

    }

    private User getUser(String username) {
        return userRepository.getUserByUsername(username);
    }
}

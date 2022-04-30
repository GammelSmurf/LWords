package org.example.LWords.Services;

import org.example.LWords.Entities.Record;
import org.example.LWords.Entities.User;
import org.example.LWords.dto.Responses.StatisticResponse;
import org.springframework.web.multipart.MultipartFile;

public interface RecordService {
    Iterable<Record> getRecordsByUser(User user);
    Iterable<Record> getLearningRecords(User user);
    String addRecord(String phrase, int progress);
    void removeRecord(Long id);
    void updateRecord(Long id,Record record);
    int importCSV(MultipartFile file);
    StatisticResponse getStatistic();
}

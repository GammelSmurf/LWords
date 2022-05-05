package org.example.LWords.Services;

import org.example.LWords.Entities.Record;
import org.example.LWords.dto.Responses.StatisticResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecordService {
    Iterable<Record> getAllRecords(String username);

    Iterable<Record> getLearningRecords(String username);

    String addRecord(String username, String phrase, int progress);

    void removeRecords(List<Long> ids);

    void updateRecord(String username, Long id, Record record);

    int importCSV(String username, MultipartFile file);

    StatisticResponse getStatistic(String username);
}

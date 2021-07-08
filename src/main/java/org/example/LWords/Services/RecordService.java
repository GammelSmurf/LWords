package org.example.LWords.Services;

import org.example.LWords.Entities.Record;
import org.example.LWords.Entities.User;
import org.example.LWords.dto.MessageResponse;

public interface RecordService {
    Iterable<Record> getRecordsByUser(User user);
    MessageResponse addRecord(String phrase);
    Boolean removeRecord(Long id);
}

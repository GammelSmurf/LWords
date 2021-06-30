package org.example.LWords.Services;

import org.example.LWords.Entities.Record;
import org.example.LWords.Entities.User;

public interface RecordService {
    Iterable<Record> getRecordsByUser(User user);
}

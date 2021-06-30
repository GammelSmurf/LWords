package org.example.LWords.Services;

import org.example.LWords.Models.Record;
import org.example.LWords.Models.User;

public interface RecordService {
    Iterable<Record> getRecordsByUser(User user);
}

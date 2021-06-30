package org.example.LWords.Services.Impl;

import org.example.LWords.Models.Record;
import org.example.LWords.Models.User;
import org.example.LWords.Services.RecordService;
import org.example.LWords.repos.RecordRepository;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;

    public RecordServiceImpl(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public Iterable<Record> getRecordsByUser(User user){
        return recordRepository.findByUser(user);
    }
}

package org.example.LWords.Services.Impl;

import org.example.LWords.Entities.Record;
import org.example.LWords.Entities.User;
import org.example.LWords.Scrapers.ReversoScraper;
import org.example.LWords.Security.MyUserDetails;
import org.example.LWords.Services.RecordService;
import org.example.LWords.dto.MessageResponse;
import org.example.LWords.repos.RecordRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Service
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;
    //private final UserRepository userRepository;


    public RecordServiceImpl(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public Iterable<Record> getRecordsByUser(User user){
        return recordRepository.findByUser(user);
    }

    public MessageResponse addRecord(String phrase) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!recordRepository.existsByPhraseAndUser(phrase, userDetails.getUser())){
            ReversoScraper scraper = new ReversoScraper();
            String translations = scraper.getTranslations(phrase);
            if(translations != null){
                Record record = new Record(phrase, translations, LocalDateTime.now(), userDetails.getUser());
                recordRepository.save(record);
                return new MessageResponse("Added successfully");
            }
            else
                return new MessageResponse("Incorrect input");
        }
        else
            return new MessageResponse("This phrase already exists");
    }

    public Boolean removeRecord(Long id){
        try{
            recordRepository.deleteById(id);
            return true;
        }
        catch (Exception ex){
            return false;
        }
    }
}

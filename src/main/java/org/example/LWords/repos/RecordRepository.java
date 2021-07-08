package org.example.LWords.repos;

import org.example.LWords.Entities.Record;
import org.example.LWords.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecordRepository extends JpaRepository<Record, Long> {
    Iterable<Record> findByUser(User user);
    Boolean existsByPhraseAndUser(String phrase, User user);
}
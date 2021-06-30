package org.example.LWords.repos;

import org.example.LWords.Models.Record;
import org.example.LWords.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecordRepository extends JpaRepository<Record, Long> {
    Iterable<Record> findByUser(User user);
}
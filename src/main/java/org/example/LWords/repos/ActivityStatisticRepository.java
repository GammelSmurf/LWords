package org.example.LWords.repos;

import org.example.LWords.Entities.ActivityStatistic;
import org.example.LWords.Entities.Record;
import org.example.LWords.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;

public interface ActivityStatisticRepository extends JpaRepository<ActivityStatistic, Long> {
    ActivityStatistic findByDayOfWeekAndUser(DayOfWeek dayOfWeek, User user);
    Iterable<ActivityStatistic> findByUser(User user);
}

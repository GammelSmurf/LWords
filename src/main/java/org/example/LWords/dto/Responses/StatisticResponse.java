package org.example.LWords.dto.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.LWords.Entities.ActivityStatistic;

@Getter
@Setter
@AllArgsConstructor
public class StatisticResponse {
    private int completedRecCount;
    private int newRecCount;
    private int inProgressRecCount;
    private Iterable<ActivityStatistic> areaStatistic;
    private int learnedWordsCount;
    private String hardestWord;
    private int[] progressStatistic;
}

package org.example.LWords.dto.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveRecordsRequest {
    private List<Long> recordIds;
}
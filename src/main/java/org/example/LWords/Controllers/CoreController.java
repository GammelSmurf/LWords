package org.example.LWords.Controllers;

import lombok.RequiredArgsConstructor;
import org.example.LWords.dto.Requests.NewRecordRequest;
import org.example.LWords.Entities.Record;
import org.example.LWords.dto.Requests.RemoveRecordsRequest;
import org.example.LWords.Entities.User;
import org.example.LWords.Services.RecordService;
import org.example.LWords.Services.UserService;
import org.example.LWords.dto.Responses.ImportResponse;
import org.example.LWords.dto.Responses.StatisticResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class CoreController {
    private final RecordService recordService;
    private final UserService userService;

    @GetMapping
    public Iterable<Record> home(@RequestHeader("username") String username) {
        return recordService.getAllRecords(username);
    }

    @GetMapping("/learning")
    public Iterable<Record> getLearningRecords(@RequestHeader("username") String username) {
        return recordService.getLearningRecords(username);
    }

    @PostMapping
    public ResponseEntity<String> addRecord(@RequestHeader("username") String username, @RequestBody NewRecordRequest newRecordRequest) {
        try {
            String message = recordService.addRecord(username, newRecordRequest.getNewPhrase(), 0);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<HttpStatus> removeRecords(@RequestBody RemoveRecordsRequest removeRecordsRequest) {
        try {
            recordService.removeRecords(removeRecordsRequest.getRecordIds());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload")
    public ImportResponse importCSV(@RequestHeader("username") String username, @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                return new ImportResponse(recordService.importCSV(username, file));
            } catch (Exception ex) {
                return new ImportResponse(0);
            }
        } else {
            return new ImportResponse(0);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody User user) {
        try {
            userService.updateUser(user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/profile")
    public StatisticResponse getStatistic(@RequestHeader("username") String username) {
        return recordService.getStatistic(username);
    }

    @PutMapping("/learning/{id}")
    public ResponseEntity<HttpStatus> updateRecord(@RequestHeader("username") String username, @PathVariable("id") long id, @RequestBody Record record) {
        try {
            recordService.updateRecord(username, id, record);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return userService.getUsers();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> removeUser(@PathVariable("id") long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package org.example.LWords.Controllers;

import lombok.RequiredArgsConstructor;
import org.example.LWords.dto.Requests.NewRecordRequest;
import org.example.LWords.Entities.Record;
import org.example.LWords.dto.Requests.RemoveRecordsRequest;
import org.example.LWords.Entities.User;
import org.example.LWords.Security.MyUserDetails;
import org.example.LWords.Services.RecordService;
import org.example.LWords.Services.UserService;
import org.example.LWords.dto.Responses.ImportResponse;
import org.example.LWords.dto.Responses.StatisticResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final RecordService recordService;
    private final UserService userService;

    @GetMapping
    public Iterable<Record> home(@AuthenticationPrincipal MyUserDetails myUserDetails){
        return recordService.getRecordsByUser(myUserDetails.getUser());
    }

    @GetMapping("/learning")
    public Iterable<Record> getLearningRecords(@AuthenticationPrincipal MyUserDetails myUserDetails){
        return recordService.getLearningRecords(myUserDetails.getUser());
    }

    @PostMapping
    public ResponseEntity<String> addRecord(@RequestBody NewRecordRequest newRecordRequest){
        try{
            String message = recordService.addRecord(newRecordRequest.getNewPhrase(), 0);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<HttpStatus> removeRecord(@RequestBody RemoveRecordsRequest removeRecordsRequest){
        try {
            for (Long id:
                 removeRecordsRequest.getRecordIds()) {
                recordService.removeRecord(id);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload")
    public ImportResponse importCSV(@RequestParam("file") MultipartFile file){
        if(!file.isEmpty()){
            try{
                return new ImportResponse(recordService.importCSV(file));
            }
            catch(Exception ex){
                return new ImportResponse(0);
            }
        }
        else{
            return new ImportResponse(0);
        }
    }


    @PutMapping("/profile")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody User user){
        try{
            userService.updateUser(user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/profile")
    public StatisticResponse getStatistic(){
        return recordService.getStatistic();
    }

    @PutMapping("/learning/{id}")
    public ResponseEntity<HttpStatus> updateRecord(@PathVariable("id") long id, @RequestBody Record record){
        try{
            recordService.updateRecord(id, record);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

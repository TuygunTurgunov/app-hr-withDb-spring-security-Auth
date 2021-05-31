package uz.pdp.online.m6l5apphr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.m6l5apphr.entity.TurnicetHistory;
import uz.pdp.online.m6l5apphr.payload.ApiResponse;
import uz.pdp.online.m6l5apphr.payload.TurnicetHistoryDto;
import uz.pdp.online.m6l5apphr.payload.TurnicetHistoryQueryDto;
import uz.pdp.online.m6l5apphr.service.TurnicetHistoryService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/turnicet/turnicetHistory")
public class TurnicetHistoryController {
    @Autowired
    TurnicetHistoryService turnicetHistoryService;


    @PostMapping("/in")
    public HttpEntity<?>addIn(@Valid @RequestBody TurnicetHistoryDto turnicetHistoryDto ){

    ApiResponse apiResponse=turnicetHistoryService.addIn(turnicetHistoryDto);
    return ResponseEntity.status(apiResponse.getIsSuccess()?201:409).body(apiResponse);
    }

    @PostMapping("/out")
    public HttpEntity<?>addOut(@Valid @RequestBody TurnicetHistoryDto turnicetHistoryDto){
     ApiResponse apiResponse=turnicetHistoryService.addOut(turnicetHistoryDto);
     return ResponseEntity.status(apiResponse.getIsSuccess()?201:409).body(apiResponse);
    }


    @PatchMapping("/{id}")
    public HttpEntity<?>editHistory(@PathVariable UUID id,@Valid@RequestBody TurnicetHistoryDto turnicetHistoryDto){
        ApiResponse apiResponse=turnicetHistoryService.editHistory(id,turnicetHistoryDto);
        return ResponseEntity.status(apiResponse.getIsSuccess()?200:409).body(apiResponse);

    }


    @GetMapping
    public HttpEntity<?>getAll(){
        List<TurnicetHistory>turnicetHistoryList= turnicetHistoryService.getAll();
        return ResponseEntity.status(turnicetHistoryList!=null?200:409).body(turnicetHistoryList);
    }

    @GetMapping("/{id}")
    public HttpEntity<?>getOneHistory(@PathVariable UUID id){
        TurnicetHistory turnicetHistory=turnicetHistoryService.getOneHistory(id);
        return ResponseEntity.status(turnicetHistory!=null?200:409).body(turnicetHistory);
    }

    @GetMapping("/query")
    public HttpEntity<?>getByQuery(@RequestBody TurnicetHistoryQueryDto turnicetHistoryQueryDto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<TurnicetHistory >turnicetHistoryList=turnicetHistoryService.getByQuery(turnicetHistoryQueryDto,authentication);
        return ResponseEntity.status(turnicetHistoryList!=null?200:409).body(turnicetHistoryList);

    }
}

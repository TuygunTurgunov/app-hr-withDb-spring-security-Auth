package uz.pdp.online.m6l5apphr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.m6l5apphr.entity.Turnicet;
import uz.pdp.online.m6l5apphr.payload.ApiResponse;
import uz.pdp.online.m6l5apphr.payload.TurnicetDto;
import uz.pdp.online.m6l5apphr.service.TurnicetService;

import java.util.List;

@RestController
@RequestMapping("/api/turnicet")
public class TurnicetController {
    @Autowired
    TurnicetService turnicetService;
    @PostMapping
    public HttpEntity<?>addTurnicet(@RequestBody TurnicetDto turnicetDto){
        ApiResponse apiResponse=turnicetService.addTurnicet(turnicetDto);
        return ResponseEntity.status(apiResponse.getIsSuccess()?201:409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?>edit(@PathVariable Integer id,@RequestBody TurnicetDto turnicetDto){
        ApiResponse apiResponse=turnicetService.edit(id,turnicetDto);
        return ResponseEntity.status(apiResponse.getIsSuccess()?200:409).body(apiResponse);
    }




    @GetMapping
    public HttpEntity<?>getAllTurnicet(){

        List<Turnicet> turnicetList=turnicetService.getAllTurnicet();
        return ResponseEntity.status(turnicetList!=null?200:409).body(turnicetList);

    }
    @GetMapping("/{id}")
    public HttpEntity<?>getOneTurnicet(@PathVariable Integer id){
        Turnicet turnicet=turnicetService.getOneTurnicet(id);
        return ResponseEntity.status(turnicet!=null?200:409).body(turnicet);

    }
    @DeleteMapping("/{id}")
    public HttpEntity<?>delete(@PathVariable Integer id){
        ApiResponse apiResponse=turnicetService.delete(id);
        return ResponseEntity.status(apiResponse.getIsSuccess()?200:409).body(apiResponse);
    }
}

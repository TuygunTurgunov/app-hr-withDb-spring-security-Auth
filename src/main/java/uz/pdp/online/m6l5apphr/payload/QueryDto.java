package uz.pdp.online.m6l5apphr.payload;

import lombok.Data;

@Data
public class QueryDto {
    private String email;
    private  Boolean completed;
    private  Boolean completedOnTime;
}

package uz.pdp.online.m6l5apphr.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class TurnicetHistoryQueryDto {

    private Integer turnicetId;
    private Long first;
    private Long second;



}

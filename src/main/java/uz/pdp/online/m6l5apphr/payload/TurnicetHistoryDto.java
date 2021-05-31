package uz.pdp.online.m6l5apphr.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TurnicetHistoryDto {
    @NotNull
    private Integer turnicetId;

    private Boolean in;

}

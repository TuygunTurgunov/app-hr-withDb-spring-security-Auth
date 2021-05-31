package uz.pdp.online.m6l5apphr.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class TurnicetDto {

    @NotNull
    private UUID userId;
}

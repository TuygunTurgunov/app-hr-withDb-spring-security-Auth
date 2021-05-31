package uz.pdp.online.m6l5apphr.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class SalaryDto {

    @NotNull
    private UUID userId;

    @NotNull
    private Integer monthId;

    @NotNull
    private Double salaryAmount;

    @NotNull
    private Integer year;


}

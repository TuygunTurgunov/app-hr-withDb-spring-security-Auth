package uz.pdp.online.m6l5apphr.payload;

import lombok.Data;
import uz.pdp.online.m6l5apphr.entity.Status;

import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Set;

@Data
public class TaskDto {


    @Size(min = 3,max = 100)
    private String name;


    @Size(min = 3)
    private String description;

    @NotNull(message = "Status id not be empty")
    private Integer statusId;

    @NotNull
    @Email
    private String workerEmail;


    private Long deadlineDate;





}

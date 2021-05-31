package uz.pdp.online.m6l5apphr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.m6l5apphr.entity.Task;

import javax.validation.constraints.Email;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task,UUID> {
    Optional<Task>findByWorkerEmailAndTaskCode(@Email String workerEmail, String taskCode);

    Set<Task>findAllByWorkerEmailAndCompletedAndCompletedOnTime(@Email String workerEmail, Boolean completed, Boolean completedOnTime);







}

package uz.pdp.online.m6l5apphr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.m6l5apphr.entity.Monthly;
import uz.pdp.online.m6l5apphr.entity.Salary;
import uz.pdp.online.m6l5apphr.entity.User;

import java.util.List;
import java.util.UUID;

public interface SalaryRepository extends JpaRepository<Salary, UUID> {

    boolean existsByUserAndMonthlyAndYear(User user, Monthly monthly, Integer year);

    boolean existsByUserAndMonthlyAndYearAndIdNot(User user, Monthly monthly, Integer year, UUID id);

    List<Salary>findAllByUser(User user);
    List<Salary>findAllByMonthly(Monthly monthly);






}

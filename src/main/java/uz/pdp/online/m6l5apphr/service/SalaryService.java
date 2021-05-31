package uz.pdp.online.m6l5apphr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.pdp.online.m6l5apphr.entity.Monthly;
import uz.pdp.online.m6l5apphr.entity.Salary;
import uz.pdp.online.m6l5apphr.entity.User;
import uz.pdp.online.m6l5apphr.entity.enums.RoleName;
import uz.pdp.online.m6l5apphr.payload.ApiResponse;
import uz.pdp.online.m6l5apphr.payload.SalaryDto;
import uz.pdp.online.m6l5apphr.repository.MonthlyRepository;
import uz.pdp.online.m6l5apphr.repository.SalaryRepository;
import uz.pdp.online.m6l5apphr.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class SalaryService {
    @Autowired
    SalaryRepository salaryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MonthlyRepository monthlyRepository;


    public ApiResponse add(SalaryDto salaryDto) {

        Optional<User> optionalUser = userRepository.findById(salaryDto.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("worker not found by id",false);

        Optional<Monthly> optionalMonthly = monthlyRepository.findById(salaryDto.getMonthId());
        if (!optionalMonthly.isPresent())
            return new ApiResponse("Month not found by id",false);

        boolean exists = salaryRepository.existsByUserAndMonthlyAndYear(optionalUser.get(), optionalMonthly.get(), salaryDto.getYear());
        if (exists)
            return new ApiResponse("This worker already get salary ",false);


        Salary salary =new Salary();
        salary.setUser(optionalUser.get());
        salary.setMonthly(optionalMonthly.get());
        salary.setYear(salaryDto.getYear());
        salary.setSalaryAmount(salaryDto.getSalaryAmount());
        salaryRepository.save(salary);
        return new ApiResponse("Process saved",true);
    }


    public ApiResponse edit(UUID id, SalaryDto salaryDto) {

        Optional<Salary> optionalSalary = salaryRepository.findById(id);
        if (!optionalSalary.isPresent())
            return new ApiResponse("salary not found by id",false);

        Optional<User> optionalUser = userRepository.findById(salaryDto.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("user not found by id",false);

        Optional<Monthly> optionalMonthly = monthlyRepository.findById(salaryDto.getMonthId());
        if (!optionalMonthly.isPresent())
            return new ApiResponse("month not found by id",false);


        boolean exists = salaryRepository.existsByUserAndMonthlyAndYearAndIdNot(optionalUser.get(), optionalMonthly.get(), salaryDto.getYear(), id);
        if (exists)
            return new ApiResponse("in this date salary already exists",false);


        Salary salary = optionalSalary.get();
        salary.setYear(salaryDto.getYear());
        salary.setMonthly(optionalMonthly.get());
        salary.setSalaryAmount(salaryDto.getSalaryAmount());
        salary.setUser(optionalUser.get());
        salaryRepository.save(salary);
        return new ApiResponse("salary edited",true);

    }


    public List<Salary> getAll() {

        return salaryRepository.findAll();

    }


    public Salary getOne(UUID id) {

        Optional<Salary> optionalSalary = salaryRepository.findById(id);
        return optionalSalary.orElse(null);

    }

    public List<Salary> getInfoByUserId(UUID id, Authentication authentication) {

        User authenticationUser =(User) authentication.getPrincipal();
        for (GrantedAuthority authority : authenticationUser.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_WORKER.name())){
                return null;
            }
        }


        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent())
            return null;

        User user = optionalUser.get();

        return salaryRepository.findAllByUser(user);


    }

    public List<Salary> getInfoByMonthId(Integer id,Authentication authentication) {

        User authenticationUser =(User) authentication.getPrincipal();
        for (GrantedAuthority authority : authenticationUser.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_WORKER.name())){
                return null;
            }
        }

        Optional<Monthly> optionalMonthly = monthlyRepository.findById(id);
        if (!optionalMonthly.isPresent())
            return null;

        List<Salary> allByMonthly = salaryRepository.findAllByMonthly(optionalMonthly.get());
        return allByMonthly;

    }


    public ApiResponse delete(UUID id) {

        Optional<Salary> optionalSalary = salaryRepository.findById(id);
        if (!optionalSalary.isPresent())
            return new ApiResponse("salary not found by id",false);
        try {

        salaryRepository.deleteById(id);
        return new ApiResponse("salary deleted",true);
        }catch (Exception e){
            return new ApiResponse("no deleted error",false);
        }
    }
}

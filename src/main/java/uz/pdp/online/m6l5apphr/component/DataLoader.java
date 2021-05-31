package uz.pdp.online.m6l5apphr.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.online.m6l5apphr.entity.Monthly;
import uz.pdp.online.m6l5apphr.entity.Role;
import uz.pdp.online.m6l5apphr.entity.Status;
import uz.pdp.online.m6l5apphr.entity.User;
import uz.pdp.online.m6l5apphr.entity.enums.MonthName;
import uz.pdp.online.m6l5apphr.entity.enums.RoleName;
import uz.pdp.online.m6l5apphr.entity.enums.StatusName;
import uz.pdp.online.m6l5apphr.repository.MonthlyRepository;
import uz.pdp.online.m6l5apphr.repository.RoleRepository;
import uz.pdp.online.m6l5apphr.repository.StatusRepository;
import uz.pdp.online.m6l5apphr.repository.UserRepository;
import uz.pdp.online.m6l5apphr.service.AuthService;

import java.util.Collections;
import java.util.UUID;

/**
 * Project run bo'lishi bilan ishga tushadigan class FAQAT application.properties dagi
 *          ba'zi  buyruqlarga bog'lab qoyishimiz kerak , bu class ni faqat bir marta o'qishligi uchun
 */




@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    MonthlyRepository monthlyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthService authService;

    /**
     * spring.sql.init.enabled ==>shuni  application propertiesdagi qiymatini opkeladi
     */
    @Value(value = "${spring.sql.init.enabled}")
    boolean runFirstWithDataSql;


    /**
     *spring.jpa.hibernate.ddl-auto ==>shuni  application propertiesdagi qiymatini opkeladi
     */
    @Value(value = "${spring.jpa.hibernate.ddl-auto}")
    String runDDL;



    // runDDL=update and   runFirstWithDataSql=true
            //bo'lganida shu  CommandLineRunner ni override bo'lgan methodi
                    // ishga tushadi va emailga habar ketadi.
    @Override
    public void run(String... args) throws Exception {
        if(runDDL.equals("create")){

            Role roleDirector=new Role();
            roleDirector.setRoleName(RoleName.ROLE_DIRECTOR);
            roleRepository.save(roleDirector);
            Role roleManager=new Role();
            roleManager.setRoleName(RoleName.ROLE_HR_MANAGER);
            roleRepository.save(roleManager);
            Role roleWorker=new Role();
            roleWorker.setRoleName(RoleName.ROLE_WORKER);
            roleRepository.save(roleWorker);

            Status statusNew=new Status();
            statusNew.setStatusName(StatusName.STATUS_NEW);
            statusRepository.save(statusNew);
            Status statusProcess=new Status();
            statusProcess.setStatusName(StatusName.STATUS_PROCESS);
            statusRepository.save(statusProcess);
            Status statusCompleted=new Status();
            statusCompleted.setStatusName(StatusName.STATUS_COMPLETED);
            statusRepository.save(statusCompleted);

            Monthly monthly1=new Monthly();
            monthly1.setMonthName(MonthName.JANUARY);
            monthlyRepository.save(monthly1);

            Monthly monthly2=new Monthly();
            monthly2.setMonthName(MonthName.FEBRUARY);
            monthlyRepository.save(monthly2);

            Monthly monthly3=new Monthly();
            monthly3.setMonthName(MonthName.MARCH);
            monthlyRepository.save(monthly3);

            Monthly monthly4=new Monthly();
            monthly4.setMonthName(MonthName.APRIL);
            monthlyRepository.save(monthly4);

            Monthly monthly5=new Monthly();
            monthly5.setMonthName(MonthName.MAY);
            monthlyRepository.save(monthly5);

            Monthly monthly6=new Monthly();
            monthly6.setMonthName(MonthName.JUNE);
            monthlyRepository.save(monthly6);

            Monthly monthly7=new Monthly();
            monthly7.setMonthName(MonthName.JULY);
            monthlyRepository.save(monthly7);

            Monthly monthly8=new Monthly();
            monthly8.setMonthName(MonthName.AUGUST);
            monthlyRepository.save(monthly8);

            Monthly monthly9=new Monthly();
            monthly9.setMonthName(MonthName.SEPTEMBER);
            monthlyRepository.save(monthly9);

            Monthly monthly10=new Monthly();
            monthly10.setMonthName(MonthName.OCTOBER);
            monthlyRepository.save(monthly10);

            Monthly monthly11=new Monthly();
            monthly11.setMonthName(MonthName.NOVEMBER);
            monthlyRepository.save(monthly11);

            Monthly monthly12=new Monthly();
            monthly12.setMonthName(MonthName.DECEMBER);
            monthlyRepository.save(monthly12);

        }




        //true  and update
        if (runDDL.equals("create")){
            User director=new User();
            director.setFirstName("murod");
            director.setLastName("rasulov");
            director.setEmail("murodrasulov1467@gmail.com");
            director.setPassword(passwordEncoder.encode("1467"));
            director.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)));
            director.setEmailCode(UUID.randomUUID().toString());
            userRepository.save(director);//yoki Arrays.asList() qilib agar ko'p user bosa
            authService.sendEmail(director.getEmail(), director.getEmailCode());
        }
    }
}

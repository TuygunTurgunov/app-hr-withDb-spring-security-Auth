package uz.pdp.online.m6l5apphr.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.online.m6l5apphr.entity.User;
import uz.pdp.online.m6l5apphr.entity.enums.RoleName;
import uz.pdp.online.m6l5apphr.repository.RoleRepository;
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



        if (runFirstWithDataSql &&runDDL.equals("update")){
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

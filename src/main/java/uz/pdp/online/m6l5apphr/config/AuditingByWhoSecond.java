package uz.pdp.online.m6l5apphr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import uz.pdp.online.m6l5apphr.config.AuditingByWhoIdFirst;

import java.util.UUID;

@Configuration
@EnableJpaAuditing
public class AuditingByWhoSecond {

    @Bean
    AuditorAware<UUID>auditorAware(){
        return new AuditingByWhoIdFirst();
    }
}

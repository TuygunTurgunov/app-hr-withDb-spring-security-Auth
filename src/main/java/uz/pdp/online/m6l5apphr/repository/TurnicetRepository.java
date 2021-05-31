package uz.pdp.online.m6l5apphr.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.m6l5apphr.entity.Turnicet;
import uz.pdp.online.m6l5apphr.entity.User;

public interface TurnicetRepository extends JpaRepository<Turnicet,Integer> {
   boolean existsByUser(User user) ;
   boolean existsByUserAndIdNot(User user, Integer id);


}

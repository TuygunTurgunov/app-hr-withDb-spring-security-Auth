package uz.pdp.online.m6l5apphr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.m6l5apphr.entity.Status;
import uz.pdp.online.m6l5apphr.entity.enums.StatusName;

public interface StatusRepository extends JpaRepository<Status,Integer> {
    Status findByStatusName(StatusName statusName);

}

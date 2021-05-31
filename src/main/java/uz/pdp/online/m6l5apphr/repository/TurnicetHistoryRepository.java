package uz.pdp.online.m6l5apphr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.online.m6l5apphr.entity.Turnicet;
import uz.pdp.online.m6l5apphr.entity.TurnicetHistory;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface TurnicetHistoryRepository extends JpaRepository<TurnicetHistory, UUID> {
    /**
     * Berilgan oraliqda ishchilarni kirib ciqishini tekshirishga
     * @param turnicet employee turnicet
     * @param createdAt berilgan oraliq boshi
     * @param createdAt2 berilgan oraliq oxiri
     * @return turnicetHistory toifasidagi list
     */

    List<TurnicetHistory>findAllByTurnicetAndCreatedAtBetween(Turnicet turnicet, Timestamp createdAt, Timestamp createdAt2);





}

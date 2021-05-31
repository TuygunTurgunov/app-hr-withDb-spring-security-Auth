package uz.pdp.online.m6l5apphr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.online.m6l5apphr.entity.Turnicet;
import uz.pdp.online.m6l5apphr.entity.TurnicetHistory;
import uz.pdp.online.m6l5apphr.entity.User;
import uz.pdp.online.m6l5apphr.entity.enums.RoleName;
import uz.pdp.online.m6l5apphr.payload.ApiResponse;
import uz.pdp.online.m6l5apphr.payload.TurnicetHistoryDto;
import uz.pdp.online.m6l5apphr.payload.TurnicetHistoryQueryDto;
import uz.pdp.online.m6l5apphr.repository.TurnicetHistoryRepository;
import uz.pdp.online.m6l5apphr.repository.TurnicetRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TurnicetHistoryService {
    @Autowired
    TurnicetHistoryRepository turnicetHistoryRepository;
    @Autowired
    TurnicetRepository turnicetRepository;

    public ApiResponse addIn(TurnicetHistoryDto turnicetHistoryDto) {

        Optional<Turnicet> optionalTurnicet = turnicetRepository.findById(turnicetHistoryDto.getTurnicetId());
        if (!optionalTurnicet.isPresent())
            return new ApiResponse("turnicet not found by id",false);

        TurnicetHistory turnicetHistory=new TurnicetHistory();
        turnicetHistory.setTurnicet(optionalTurnicet.get());
        turnicetHistory.setEntered(true);
        turnicetHistoryRepository.save(turnicetHistory);
        return new ApiResponse("welcome",true);

    }


    public ApiResponse addOut(TurnicetHistoryDto turnicetHistoryDto) {
        Optional<Turnicet> optionalTurnicet = turnicetRepository.findById(turnicetHistoryDto.getTurnicetId());
        if (!optionalTurnicet.isPresent())
            return new ApiResponse("turnicet not found by id",false);

        TurnicetHistory turnicetHistory=new TurnicetHistory();
        turnicetHistory.setTurnicet(optionalTurnicet.get());
        turnicetHistory.setEntered(false);
        turnicetHistoryRepository.save(turnicetHistory);
        return new ApiResponse("goodBy",true);
    }


    public List<TurnicetHistory> getAll() {
        return turnicetHistoryRepository.findAll();
    }

    public TurnicetHistory getOneHistory(UUID id) {

        Optional<TurnicetHistory> optionalTurnicetHistory = turnicetHistoryRepository.findById(id);
        return optionalTurnicetHistory.orElse(null);
    }


    public ApiResponse editHistory(UUID id,TurnicetHistoryDto turnicetHistoryDto) {
        Optional<TurnicetHistory> optionalTurnicetHistory = turnicetHistoryRepository.findById(id);
        if (!optionalTurnicetHistory.isPresent())
            return new ApiResponse("history  not found by id",false);

        Optional<Turnicet> optionalTurnicet = turnicetRepository.findById(turnicetHistoryDto.getTurnicetId());
        if (!optionalTurnicet.isPresent())
            return new ApiResponse("turnicet not found by id",false);

        Turnicet turnicet = optionalTurnicet.get();
        TurnicetHistory turnicetHistory = optionalTurnicetHistory.get();
        turnicetHistory.setTurnicet(turnicet);
        turnicetHistory.setEntered(turnicetHistoryDto.getIn());
        turnicetHistoryRepository.save(turnicetHistory);
        return new ApiResponse("turnicet history edited",true);
    }

    public List<TurnicetHistory> getByQuery(TurnicetHistoryQueryDto turnicetHistoryQueryDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticationUser =(User) authentication.getPrincipal();
        for (GrantedAuthority authority : authenticationUser.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_WORKER.name())){
                return null;
            }
        }



        Optional<Turnicet> optionalTurnicet = turnicetRepository.findById(turnicetHistoryQueryDto.getTurnicetId());
        if (!optionalTurnicet.isPresent())
            return null;

        Turnicet turnicet = optionalTurnicet.get();
        Timestamp timeFirst=new Timestamp(turnicetHistoryQueryDto.getFirst());
        Timestamp timeSecond=new Timestamp(turnicetHistoryQueryDto.getSecond());

        List<TurnicetHistory> allByTurnicetAndCreatedAtBetween = turnicetHistoryRepository.findAllByTurnicetAndCreatedAtBetween(turnicet, timeFirst, timeSecond);
        return allByTurnicetAndCreatedAtBetween;


    }
}

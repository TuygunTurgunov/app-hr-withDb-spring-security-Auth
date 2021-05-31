package uz.pdp.online.m6l5apphr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.online.m6l5apphr.entity.Turnicet;
import uz.pdp.online.m6l5apphr.entity.User;
import uz.pdp.online.m6l5apphr.entity.enums.RoleName;
import uz.pdp.online.m6l5apphr.payload.ApiResponse;
import uz.pdp.online.m6l5apphr.payload.TurnicetDto;
import uz.pdp.online.m6l5apphr.repository.TurnicetRepository;
import uz.pdp.online.m6l5apphr.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TurnicetService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TurnicetRepository turnicetRepository;


    public ApiResponse addTurnicet(TurnicetDto turnicetDto) {

        Optional<User> optionalUser = userRepository.findById(turnicetDto.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("User not dound by id",false);
        User user = optionalUser.get();
        boolean existsByUser = turnicetRepository.existsByUser(user);
        if (existsByUser)
            return new ApiResponse("turnicet already exists for this user",false);


        Turnicet turnicet=new Turnicet();
        turnicet.setUser(user);
        turnicetRepository.save(turnicet);
        return new ApiResponse("turnicet saved",true);
    }


    public List<Turnicet> getAllTurnicet() {
        return turnicetRepository.findAll();
    }


    public Turnicet getOneTurnicet(Integer id) {
        Optional<Turnicet> optionalTurnicet = turnicetRepository.findById(id);
        return optionalTurnicet.orElse(null);
    }


    public ApiResponse edit(Integer id,TurnicetDto turnicetDto) {
        Optional<Turnicet> optionalTurnicet = turnicetRepository.findById(id);
        if(!optionalTurnicet.isPresent())
            return new ApiResponse("turnicet not found by id",false);



        Optional<User> optionalUser = userRepository.findById(turnicetDto.getUserId());
        if(!optionalUser.isPresent())
            return new ApiResponse("user id not found",false);

        User user = optionalUser.get();
        boolean existsByUserAndIdNot = turnicetRepository.existsByUserAndIdNot(user, id);
        if (existsByUserAndIdNot)
            return new ApiResponse("already exists",false);


        Turnicet turnicet = optionalTurnicet.get();
        turnicet.setUser(user);
        turnicetRepository.save(turnicet);
        return new ApiResponse("turnicet edited",true);




    }

    public ApiResponse delete(Integer id) {

        try {
            turnicetRepository.deleteById(id);
            return new ApiResponse("turnicet deleted",true);
        }catch (UsernameNotFoundException notFoundException){
            throw new UsernameNotFoundException("turnicet not found by id");
        }


    }
}

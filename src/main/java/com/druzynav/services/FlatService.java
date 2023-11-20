package com.druzynav.services;

import com.druzynav.auth.JwtService;
import com.druzynav.models.characteristic.dto.AllMetricsDTO;
import com.druzynav.models.flat.Flat;
import com.druzynav.models.flat.Options;
import com.druzynav.models.flat.dto.FlatDTO;
import com.druzynav.models.user.Status;
import com.druzynav.models.user.User;
import com.druzynav.repositories.FlatRepository;
import com.druzynav.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Service
public class FlatService {
    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public ResponseEntity<?> saveFlat(AllMetricsDTO flatDTO_, HttpServletRequest request) {
        if (flatDTO_ == null) {
            return ResponseEntity.badRequest().build();
        } else {

            //TODO: Wyczyścić kontroler z tego kodu
            String authHeader = request.getHeader("Authorization");
            String jwt = authHeader.substring(7);
            String username = jwtService.extractUsername(jwt);
            Flat flat = new Flat();

            if (username == null) {
                return ResponseEntity.badRequest().build();
            }

            User user = userRepository.findByEmail(username).get();


            //--------------------------------------------
            //-------Do testow------------------------
//            User user = userRepository.findById(2).get();
            //----------------------------------------
            //TODO: Po konsultacji bedzie do wyrzucenia
            // 'hasFlat' powinno wyladaowac w koszu - do dogadania

            // Status odpowiada za to, czy uzytkownik jest bedzie brany pod uwage jako ten z wypełniony
            // wypełnionymi danymi dotyczącymi mieszkania
            user.setStatus(Status.ACTIVE);
            userRepository.save(user);

                if (flatRepository.findByUserId(user.getId()) != null) {
                    flat = flatRepository.findByUserId(user.getId());
                }

            flat.setSearch_option(flatDTO_.getSearchOption());
            flat.setUser(user);
            flat.setDescription(flatDTO_.getDescription());
            flat.setLatitude(flatDTO_.getLatitude());
            flat.setLongitude(flatDTO_.getLongitude());
            flat.setRoom_count(flatDTO_.getNumberOfRooms());
            flat.setPeople_count(flatDTO_.getNumberOfPeople());

            flatRepository.save(flat);
        }

        return ResponseEntity.ok("Zapisano dane mieszkalne");
    }

    public FlatDTO searchOne(Integer userId) {
        Flat flat = flatRepository.findByUserId(userId);

        if (flat == null) {
            return null;
        }

        FlatDTO flatDTO = new FlatDTO();
        flatDTO.setSearchOption(flat.getSearch_option());
        flatDTO.setDescription(flat.getDescription());
        flatDTO.setLatitude(flat.getLatitude());
        flatDTO.setLongitude(flat.getLongitude());
        flatDTO.setNumberOfPeople(flat.getPeople_count());
        flatDTO.setNumberOfRooms(flat.getRoom_count());


        return flatDTO;
    }
}

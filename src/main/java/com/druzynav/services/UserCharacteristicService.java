package com.druzynav.services;

import com.druzynav.exceptions.CharacteristicsException.CharacteristicsException;
import com.druzynav.models.characteristic.Characteristic;
import com.druzynav.models.characteristic.dto.AllMetricsDTO;
import com.druzynav.models.characteristic.dto.CharacteristicsDTO;
import com.druzynav.models.flat.Flat;
import com.druzynav.models.user.User;
import com.druzynav.models.userCharacteristic.UserCharacteristic;
import com.druzynav.repositories.CharacteristicsRepository;
import com.druzynav.repositories.FlatRepository;
import com.druzynav.repositories.UserCharacteristicsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class UserCharacteristicService {
    @Autowired
    public UserCharacteristicsRepository userCharacteristicsRepository;

    @Autowired
    public CharacteristicsRepository characteristicsRepository;

    @Autowired
    private FlatRepository flatRepository;

    public ResponseEntity<?> saveUserChar(CharacteristicsDTO characteristicsDTO){

        Field[] fields = CharacteristicsDTO.class.getDeclaredFields();
        UserCharacteristic userCharacteristic = new UserCharacteristic();
        System.out.println(characteristicsDTO);
        try{
            for (Field field : fields) {
                if(field.getName() == "userId") continue;
                Characteristic characteristic = characteristicsRepository.findByCharname(field.getName());
                userCharacteristic.setUserId(characteristicsDTO.getUserId());
//                userCharacteristic.setUserId(2);
                System.out.println(characteristic);
                userCharacteristic.setCharId(characteristic.getId());

                switch(field.getName()){
                    case "sleepTime":
                        userCharacteristic.setVal(characteristicsDTO.getSleepTime().toString());
                        break;
                    case "cooking":
                        userCharacteristic.setVal(characteristicsDTO.getCooking().toString());
                        break;
                    case "invitingFriends":
                        userCharacteristic.setVal(characteristicsDTO.getInvitingFriends().toString());
                        break;
                    case "timeSpentOutsideHome":
                        userCharacteristic.setVal(characteristicsDTO.getTimeSpentOutsideHome().toString());
                        break;
                    case "characterType":
                        userCharacteristic.setVal(characteristicsDTO.getCharacterType().toString());
                        break;
                    case "talkativity":
                        userCharacteristic.setVal(characteristicsDTO.getTalkativity().toString());
                        break;
                    case "conciliatory":
                        userCharacteristic.setVal(characteristicsDTO.getConciliatory().toString());
                        break;
                    case "likesPets":
                        userCharacteristic.setVal(characteristicsDTO.getLikesPets().toString());
                        break;
                    case "hasPets":
                        userCharacteristic.setVal(characteristicsDTO.getHasPets().toString());
                        break;
                    case "smokes":
                        userCharacteristic.setVal(characteristicsDTO.getSmokes().toString());
                        break;
                    case "drinks":
                        userCharacteristic.setVal(characteristicsDTO.getDrinks().toString());
                        break;
                    case "isStudent":
                        userCharacteristic.setVal(characteristicsDTO.getIsStudent().toString());
                        break;
                    case "works":
                        userCharacteristic.setVal(characteristicsDTO.getWorks().toString());
                        break;
                    case "acceptsPets":
                        userCharacteristic.setVal(characteristicsDTO.getAcceptsPets().toString());
                        break;
                    case "acceptsSmoking":
                        userCharacteristic.setVal(characteristicsDTO.getAcceptsSmoking().toString());
                        break;
                    case "preferedGender":
                        userCharacteristic.setVal(characteristicsDTO.getPreferedGender());
                        break;
                    case "livesIn":
                        userCharacteristic.setVal(characteristicsDTO.getLivesIn());
                        break;
                    default:
                        break;
                }

                userCharacteristicsRepository.save(userCharacteristic);

            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Saved", HttpStatus.OK);
    }

    public ResponseEntity<?> userCharByUserIdLong(Integer userId) {
        List<UserCharacteristic> userCharacteristicList = userCharacteristicsRepository.findByUserId(userId);
        System.out.println(userCharacteristicList);
        if(userCharacteristicList.isEmpty()){
            throw new CharacteristicsException();
        }
        AllMetricsDTO resp = new AllMetricsDTO();
        resp.setUserId(userId);
        for (UserCharacteristic userCharacteristic: userCharacteristicList) {
            switch(userCharacteristic.getCharId()){
                case 1:
                    resp.setSleepTime(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 2:
                    resp.setCooking(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 3:
                    resp.setInvitingFriends(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 4:
                    resp.setTimeSpentOutsideHome(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 5:
                    resp.setCharacterType(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 6:
                    resp.setTalkativity(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 7:
                    resp.setConciliatory(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 8:
                    resp.setLikesPets(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 9:
                    resp.setHasPets(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 10:
                    resp.setSmokes(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 11:
                    resp.setDrinks(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 12:
                    resp.setIsStudent(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 13:
                    resp.setWorks(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 14:
                    resp.setAcceptsPets(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 15:
                    resp.setAcceptsSmoking(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 16:
                    resp.setPreferedGender(userCharacteristic.getVal());
                    break;
                case 17:
                    resp.setLivesIn(userCharacteristic.getVal());
                    break;
                default:
                    break;
            }

        }

        // Napisac sprawdzenie czy flat istnieje

        Flat flat = flatRepository.findByUserId(userId);
        //Sytuacje gdy uzytkownika nie ma mieszkania nie powinna nigdy nastapic
        // ale w przypadku gdyby tak nie bylo zworicmy caiło bez cześći
        // związanej z mieszkaniem

        if(flat == null){
            System.out.println("Flat is null");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }

        if (flat.getSearch_option() == 2 || flat.getSearch_option() == 3){
            resp.setHasFlat(false);
        } else {
            resp.setHasFlat(true);
        }

        resp.setDescription(flat.getDescription());
        resp.setLatitude(flat.getLatitude());
        resp.setLongitude(flat.getLongitude());
        resp.setNumberOfPeople(flat.getPeople_count());
        resp.setNumberOfRooms(flat.getRoom_count());
        resp.setSearchOption(flat.getSearch_option());

        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    public ResponseEntity<?> userCharByUserIdShort(Integer userId) {
        List<UserCharacteristic> userCharacteristicList = userCharacteristicsRepository.findByUserId(userId);
        System.out.println(userCharacteristicList);
        if(userCharacteristicList.isEmpty()){
            throw new CharacteristicsException();
        }
        CharacteristicsDTO resp = new CharacteristicsDTO();
        resp.setUserId(userId);
        for (UserCharacteristic userCharacteristic: userCharacteristicList) {
            switch(userCharacteristic.getCharId()){
                case 1:
                    resp.setSleepTime(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 2:
                    resp.setCooking(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 3:
                    resp.setInvitingFriends(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 4:
                    resp.setTimeSpentOutsideHome(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 5:
                    resp.setCharacterType(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 6:
                    resp.setTalkativity(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 7:
                    resp.setConciliatory(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 8:
                    resp.setLikesPets(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 9:
                    resp.setHasPets(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 10:
                    resp.setSmokes(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 11:
                    resp.setDrinks(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 12:
                    resp.setIsStudent(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 13:
                    resp.setWorks(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 14:
                    resp.setAcceptsPets(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 15:
                    resp.setAcceptsSmoking(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 16:
                    resp.setPreferedGender(userCharacteristic.getVal());
                    break;
                case 17:
                    resp.setLivesIn(userCharacteristic.getVal());
                    break;
                default:
                    break;
            }

        }

        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    //TODO: Ten serwis bedzie do przerobienia

    public CharacteristicsDTO userCharacteristic(Integer userId){
        List<UserCharacteristic> userCharacteristicList = userCharacteristicsRepository.findByUserId(userId);
        System.out.println(userCharacteristicList);
        if(userCharacteristicList.isEmpty()){
            throw new CharacteristicsException();
        }
        CharacteristicsDTO resp = new CharacteristicsDTO();
        resp.setUserId(userId);
        for (UserCharacteristic userCharacteristic: userCharacteristicList) {
            switch(userCharacteristic.getCharId()){
                case 1:
                    resp.setSleepTime(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 2:
                    resp.setCooking(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 3:
                    resp.setInvitingFriends(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 4:
                    resp.setTimeSpentOutsideHome(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 5:
                    resp.setCharacterType(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 6:
                    resp.setTalkativity(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 7:
                    resp.setConciliatory(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 8:
                    resp.setLikesPets(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 9:
                    resp.setHasPets(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 10:
                    resp.setSmokes(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 11:
                    resp.setDrinks(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 12:
                    resp.setIsStudent(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 13:
                    resp.setWorks(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 14:
                    resp.setAcceptsPets(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 15:
                    resp.setAcceptsSmoking(Integer.parseInt(userCharacteristic.getVal()));
                    break;
                case 16:
                    resp.setPreferedGender(userCharacteristic.getVal());
                    break;
                case 17:
                    resp.setLivesIn(userCharacteristic.getVal());
                    break;
                default:
                    break;
            }
        }

        return resp;
    }

}

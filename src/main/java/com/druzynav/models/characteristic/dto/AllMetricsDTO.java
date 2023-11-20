package com.druzynav.models.characteristic.dto;

import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AllMetricsDTO extends CharacteristicsDTO{

    //-----FlatDTO-----
    //Czy uzytkownik zaznaczyl tak lub nie
    Boolean hasFlat;
    // Cyfry od 0 do 3 okreslajace jakim typem 'poszukiwacza' jest uzytkownik
    Integer searchOption;
    // Liczba pokoi znajdujacych sie w mieszkaniu
    Integer numberOfRooms;
    // Liczba osob mieszkajacych w mieszkaniu
    Integer numberOfPeople;
    // Opis mieszkania
    String description;
    // Miejsce na zdjecie
    //--------------------

    // Wartosci zczytane z mapy
    Double latitude;
    Double longitude;

    //-----FlatPhotoDTO-----
}

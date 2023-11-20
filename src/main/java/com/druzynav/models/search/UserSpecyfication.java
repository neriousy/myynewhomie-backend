package com.druzynav.models.search;

import com.druzynav.models.flat.Flat;
import com.druzynav.models.flat.Options;
import com.druzynav.models.user.User;
import com.druzynav.models.user.dto.SearchCriteriaExtendedDTO;
import com.druzynav.models.userCharacteristic.UserCharacteristic;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecyfication implements Specification<User>{

    private final List<Specification<User>> specifications = new ArrayList<>();

    public void addAgeGreaterSpecification(int minAge) {
        specifications.add((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("age"), minAge));
    }

    public void addAgeLessSpecification(int maxAge) {
        specifications.add((root, query, builder) -> builder.lessThanOrEqualTo(root.get("age"), maxAge));
    }

    public void addAgeBetweenSpecification(int minAge, int maxAge) {
        specifications.add((root, query, builder) -> builder.between(root.get("age"), minAge, maxAge));
    }

    public void addGenderSpecification(String gender) {
        specifications.add((root, query, builder) -> builder.equal(root.get("gender"), gender));
    }

    public void addStillLookingSpecification() {
            specifications.add((root, query, builder) -> builder.equal(root.get("still_looking"), true));
    }


    public void addSmokingStatusSpecification() {
        specifications.add((root, query, builder) -> {
            Join<User, UserCharacteristic> userCharacteristicJoin = root.join("characteristics");
            return builder.and(
                    builder.equal(userCharacteristicJoin.get("charId"), "10"),
                    builder.equal(userCharacteristicJoin.get("val"), "1")
            );
        });
    }

    public void addDrinkingAlcStatusSpecification() {
        specifications.add((root, query, builder) -> {
            Join<User, UserCharacteristic> userCharacteristicJoin = root.join("characteristics");
            return builder.and(
                    builder.equal(userCharacteristicJoin.get("charId"), "11"),
                    builder.equal(userCharacteristicJoin.get("val"), "1")
            );
        });
    }

    public void addStudentStatusSpecification() {
        specifications.add((root, query, builder) -> {
            Join<User, UserCharacteristic> userCharacteristicJoin = root.join("characteristics");
                return builder.and(
                        builder.equal(userCharacteristicJoin.get("charId"), "12"),
                        builder.equal(userCharacteristicJoin.get("val"), "1")
                );
        });
    }

    public void addWorkingStatusSpecification() {
        specifications.add((root, query, builder) -> {
            Join<User, UserCharacteristic> userCharacteristicJoin = root.join("characteristics");
            return builder.and(
                    builder.equal(userCharacteristicJoin.get("charId"), "13"),
                    builder.equal(userCharacteristicJoin.get("val"), "1")
            );
        });
    }

    public void addCitySpecification(String city) {
        specifications.add((root, query, builder) -> {
            Join<User, UserCharacteristic> userCharacteristicJoin = root.join("characteristics");
            return builder.and(
                    builder.equal(userCharacteristicJoin.get("charId"), "17"),
                    builder.equal(userCharacteristicJoin.get("val"), city)
            );
        });
    }

    public void addAnimalToleranceSpecification(List<UserCharacteristic> userCharacteristics) {
        if (((userCharacteristics.get(13).getCharId()) == 14) && (userCharacteristics.get(13).getVal()).equals("1")) {
            if (((userCharacteristics.get(8).getCharId()) == 9) && (userCharacteristics.get(8).getVal()).equals("1")) {
                specifications.add((root, query, builder) -> {
                    Join<User, UserCharacteristic> userCharacteristicJoin = root.join("characteristics");
                    return builder.and(
                            builder.equal(userCharacteristicJoin.get("charId"), "14"),
                            builder.equal(userCharacteristicJoin.get("val"), "1")
                    );
                });
            } else if (((userCharacteristics.get(8).getCharId()) == 9) && (userCharacteristics.get(8).getVal()).equals("0")) {
                specifications.add((root, query, builder) -> {
                    Join<User, UserCharacteristic> userCharacteristicJoin = root.join("characteristics");
                    return builder.or(
                            builder.and(
                                    //Akcpetuje zwierzeta i dodaje kogogos kto je ma
                                    builder.equal(userCharacteristicJoin.get("charId"), "9"),
                                    builder.equal(userCharacteristicJoin.get("val"), "1")
                            ),
                            builder.and(
                                    builder.equal(userCharacteristicJoin.get("charId"), "9"),
                                    builder.equal(userCharacteristicJoin.get("val"), "0")
                            )

                    );
                });
            }
        } else if ((((userCharacteristics.get(13).getCharId()) == 14) && (userCharacteristics.get(13).getVal()).equals("0"))
                && (((userCharacteristics.get(8).getCharId()) == 9) && (userCharacteristics.get(8).getVal()).equals("0"))) {
            specifications.add((root, query, builder) -> {
                Join<User, UserCharacteristic> userCharacteristicJoin = root.join("characteristics");
                return builder.and(
                        builder.equal(userCharacteristicJoin.get("charId"), "9"),
                        builder.equal(userCharacteristicJoin.get("val"), "0")
                );
            });
        }
    }

    public void addSmokingToleranceSpecification(List<UserCharacteristic> userCharacteristics) {
        if (((userCharacteristics.get(14).getCharId()) == 15) && (userCharacteristics.get(14).getVal()).equals("1")) {
            if (((userCharacteristics.get(9).getCharId()) == 10) && (userCharacteristics.get(9).getVal()).equals("1")) {
                specifications.add((root, query, builder) -> {
                    Join<User, UserCharacteristic> userCharacteristicJoin = root.join("characteristics");
                    return builder.and(
                            builder.equal(userCharacteristicJoin.get("charId"), "15"),
                            builder.equal(userCharacteristicJoin.get("val"), "1")
                    );
                });
            } else if (((userCharacteristics.get(9).getCharId()) == 10) && (userCharacteristics.get(9).getVal()).equals("0")) {
                specifications.add((root, query, builder) -> {
                    Join<User, UserCharacteristic> userCharacteristicJoin = root.join("characteristics");
                    return builder.or(
                            builder.and(
                                    builder.equal(userCharacteristicJoin.get("charId"), "10"),
                                    builder.equal(userCharacteristicJoin.get("val"), "1")
                            ),
                            builder.and(
                                    builder.equal(userCharacteristicJoin.get("charId"), "10"),
                                    builder.equal(userCharacteristicJoin.get("val"), "0")
                            )

                    );
                });
            }
        } else if ((((userCharacteristics.get(14).getCharId()) == 15) && (userCharacteristics.get(14).getVal()).equals("0"))
                && (((userCharacteristics.get(9).getCharId()) == 10) && (userCharacteristics.get(9).getVal()).equals("0"))) {
            specifications.add((root, query, builder) -> {
                Join<User, UserCharacteristic> userCharacteristicJoin = root.join("characteristics");
                return builder.and(
                        builder.equal(userCharacteristicJoin.get("charId"), "10"),
                        builder.equal(userCharacteristicJoin.get("val"), "0")
                );
            });
        }
    }

    public void addPreferedGenderSpecification(List<UserCharacteristic> userCharacteristics) {
        if (((userCharacteristics.get(16).getCharId()) == 17) && (userCharacteristics.get(16).getVal()).equals("M")) {
                specifications.add((root, query, builder) -> {
                    Join<User, UserCharacteristic> userCharacteristicJoin = root.join("characteristics");
                    return builder.and(
                            builder.equal(userCharacteristicJoin.get("charId"), "17"),
                            builder.equal(userCharacteristicJoin.get("val"), "M")
                    );
                });
            } else if (((userCharacteristics.get(16).getCharId()) == 17) && (userCharacteristics.get(16).getVal()).equals("K")) {
            specifications.add((root, query, builder) -> {
                Join<User, UserCharacteristic> userCharacteristicJoin = root.join("characteristics");
                return builder.and(
                        builder.equal(userCharacteristicJoin.get("charId"), "17"),
                        builder.equal(userCharacteristicJoin.get("val"), "K")
                );
            });
            } else if (((userCharacteristics.get(16).getCharId()) == 17) && (userCharacteristics.get(16).getVal()).equals("O")) {
        specifications.add((root, query, builder) -> {
            Join<User, UserCharacteristic> userCharacteristicJoin = root.join("characteristics");
            return builder.or(
                        builder.and(
                            builder.equal(userCharacteristicJoin.get("charId"), "17"),
                            builder.equal(userCharacteristicJoin.get("val"), "M")
                        ),
                        builder.and(
                            builder.equal(userCharacteristicJoin.get("charId"), "17"),
                            builder.equal(userCharacteristicJoin.get("val"), "K"))
            );
        });
        }
    }

    public void checkForCurrentUser(List<UserCharacteristic> userCharacteristic){
        addStillLookingSpecification();
        addAnimalToleranceSpecification(userCharacteristic);
        addSmokingToleranceSpecification(userCharacteristic);
        addPreferedGenderSpecification(userCharacteristic);
        System.out.println(userCharacteristic.get(15));
        System.out.println(userCharacteristic.get(16));
        addCitySpecification(userCharacteristic.get(16).getVal());
    }


    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.and(specifications.stream()
                .map(specification -> specification.toPredicate(root, query, builder))
                .toArray(Predicate[]::new));
    }

    // Scenariusze:
    // 1. Szukam osoby, ktora ma mieszkanie i szuka ma enum =
    public void addFlatSpecification(SearchCriteriaExtendedDTO searchCriteriaDTO) {
        System.out.println("Search option: " + searchCriteriaDTO.getSearch_option());
        if (searchCriteriaDTO.getSearch_option() == 0) {
            specifications.add((root, query, builder) -> {
                Join<User, Flat> userFlatJoin = root.join("flat");
                return builder.equal(userFlatJoin.get("search_option"), 2);
            });

        } else if (searchCriteriaDTO.getSearch_option() == 1) {
            specifications.add((root, query, builder) -> {
                Join<User, Flat> userFlatJoin = root.join("flat");
                return builder.equal(userFlatJoin.get("search_option"), 3);
            });

        } else if (searchCriteriaDTO.getSearch_option() == 2) {
            specifications.add((root, query, builder) -> {
                Join<User, Flat> userFlatJoin = root.join("flat");
                return builder.or(
                        builder.equal(userFlatJoin.get("search_option"), 0),
                        builder.equal(userFlatJoin.get("search_option"), 2)
                );
            });

        } else if (searchCriteriaDTO.getSearch_option() == 3) {
            System.out.println("tu wchodze");
            specifications.add((root, query, builder) -> {
                Join<User, Flat> userFlatJoin = root.join("flat");
                return builder.or(
                        builder.equal(userFlatJoin.get("search_option"), 1),
                        builder.equal(userFlatJoin.get("search_option"), 3)
                );
            });

        }
    }

}

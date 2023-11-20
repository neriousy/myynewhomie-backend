package com.druzynav;

import com.druzynav.models.user.Role;
import com.druzynav.models.user.User;
import com.druzynav.repositories.UserRepository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.github.javafaker.Faker;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTests {
    @Autowired
    private UserRepository repo;

    @Test
    @Order(1)
    @Rollback(false)
    public void createUserTest() {
        User user = User.builder()
                .firstname("John")
                .lastname("Doe")
                .email("johndoe@gmail.com")
                .build();

        repo.save(user);

        assert user.getId() > 0;
    }

    @Test
    @Order(2)
    @Rollback(false)
    public void getUserTest() {
        User user = repo.findById(1).get();
        assert user.getId() == 1;
    }

   @Test
   @Order(3)
   @Rollback(false)
    public void getListOfUsersTest() {
        List<User> users = repo.findAll();

        assert !users.isEmpty();
    }

    @Test
    @Order(4)
    @Rollback(false)
    public void updateUserTest() {
        User user = repo.findById(1).get();
        user.setFirstname("Kazik");
        repo.save(user);

        assert user.getFirstname().equals("Kazik");
    }

    @Test
    @Order(5)
    @Rollback
    public void populateWithMockUsers() {
        Faker faker = new Faker();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwd = encoder.encode("qwerty");

        for (int i = 0; i < 100; i++) {
            User user = User.builder()
                    .firstname(faker.name().firstName())
                    .lastname(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .phonenumber(faker.phoneNumber().cellPhone())
                    .password(passwd)
                    .gender("female")
                    .role(Role.USER)
                    .age(faker.number().numberBetween(18, 100))
                    .build();
            repo.save(user);
        }
    }

}

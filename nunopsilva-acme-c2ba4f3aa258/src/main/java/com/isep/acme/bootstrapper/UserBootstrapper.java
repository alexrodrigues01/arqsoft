package com.isep.acme.bootstrapper;

import com.isep.acme.model.*;
import com.isep.acme.repositories.mongoDB.UserRepositoryMongo;
import com.isep.acme.repositories.redis.UserRepositoryRedis;
import com.isep.acme.repositories.neo4J.UserRepositoryNeo4J;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.isep.acme.repositories.h2.UserRepository;

import java.util.Arrays;

@Component
//@Profile("bootstrap")
public class UserBootstrapper implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserRepositoryMongo userRepositoryMongo;

    @Autowired
    private UserRepositoryRedis userRepositoryRedis;

    @Autowired
    private UserRepositoryNeo4J userRepositoryNeo4J;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Environment environment;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {

        //admin
        if(userRepo.findByUsername("admin1@mail.com").isEmpty()) {
            User admin1 = new User("admin1@mail.com", encoder.encode("AdminPW1"),
                    "Jose Antonio", "355489123", "Rua Um");
            UserRedis admin2 = new UserRedis("admin1@mail.com", encoder.encode("AdminPW1"),
                    "Jose Antonio", "355489123", "Rua Um");
            UserMongo admin3 = new UserMongo("admin1@mail.com", encoder.encode("AdminPW1"),
                    "Jose Antonio", "355489123", "Rua Um");
            admin1.addAuthority(new Role(Role.Admin));
            admin2.addAuthority(new Role(Role.Admin));
            admin3.addAuthority(new Role(Role.Admin));

            if(Arrays.asList(environment.getActiveProfiles()).contains("neo4J")){
                userRepositoryNeo4J.save(userMapper.toUserNeo4J(admin1));
            }
            userRepo.save(admin1);
            if(Arrays.asList(environment.getActiveProfiles()).contains("mongoDB")){
                userRepositoryMongo.save(admin3);
            }
            if(Arrays.asList(environment.getActiveProfiles()).contains("reddisDB")){
                userRepositoryRedis.save(admin2);
            }




        }

        if(userRepo.findByUsername("admin2@mail.com").isEmpty()) {
            User mod1 = new User("admin2@mail.com", encoder.encode("AdminPW2"),
                    "Antonio Jose", "321984553", "Rua dois");
            UserRedis mod2 = new UserRedis("admin2@mail.com", encoder.encode("AdminPW2"),
                    "Antonio Jose", "321984553", "Rua dois");
            UserMongo mod3 = new UserMongo("admin2@mail.com", encoder.encode("AdminPW2"),
                    "Antonio Jose", "321984553", "Rua dois");
            mod1.addAuthority(new Role(Role.Mod));
            mod2.addAuthority(new Role(Role.Mod));
            mod3.addAuthority(new Role(Role.Mod));

            if(Arrays.asList(environment.getActiveProfiles()).contains("neo4J")){
                userRepositoryNeo4J.save(userMapper.toUserNeo4J(mod1));
            }
            userRepo.save(mod1);
            if(Arrays.asList(environment.getActiveProfiles()).contains("mongoDB")){
                userRepositoryMongo.save(mod3);
            }
            if(Arrays.asList(environment.getActiveProfiles()).contains("reddisDB")){
                userRepositoryRedis.save(mod2);
            }



        }

        if(userRepo.findByUsername("user1@mail.com").isEmpty()) {
            User user1 = new User("user1@mail.com", encoder.encode("userPW1"),
                    "Nuno Miguel", "253647883", "Rua tres");
            UserRedis user2 = new UserRedis("user1@mail.com", encoder.encode("userPW1"),
                    "Nuno Miguel", "253647883", "Rua tres");
            UserMongo user3 = new UserMongo("user1@mail.com", encoder.encode("userPW1"),
                    "Nuno Miguel", "253647883", "Rua tres");
            user1.addAuthority(new Role(Role.RegisteredUser));
            user2.addAuthority(new Role(Role.RegisteredUser));
            user3.addAuthority(new Role(Role.RegisteredUser));
            if(Arrays.asList(environment.getActiveProfiles()).contains("neo4J")){
                userRepositoryNeo4J.save(userMapper.toUserNeo4J(user1));
            }
            userRepo.save(user1);
            if(Arrays.asList(environment.getActiveProfiles()).contains("mongoDB")){
                userRepositoryMongo.save(user3);
            }
            if(Arrays.asList(environment.getActiveProfiles()).contains("reddisDB")){
                userRepositoryRedis.save(user2);
            }

        }
        Thread.sleep(1000);
        if(userRepo.findByUsername("user2@mail.com").isEmpty()) {
            User user2 = new User("user2@mail.com", encoder.encode("userPW2"),
                    "Miguel Nuno", "253698854", "Rua quatro");
            UserRedis user3 = new UserRedis("user2@mail.com", encoder.encode("userPW2"),
                    "Miguel Nuno", "253698854", "Rua quatro");
            UserMongo user4 = new UserMongo("user2@mail.com", encoder.encode("userPW2"),
                    "Miguel Nuno", "253698854", "Rua quatro");
            user2.addAuthority(new Role(Role.RegisteredUser));
            user3.addAuthority(new Role(Role.RegisteredUser));
            user4.addAuthority(new Role(Role.RegisteredUser));

            if(Arrays.asList(environment.getActiveProfiles()).contains("neo4J")){
                userRepositoryNeo4J.save(userMapper.toUserNeo4J(user2));
            }
            userRepo.save(user2);
            if(Arrays.asList(environment.getActiveProfiles()).contains("mongoDB")){
                userRepositoryMongo.save(user4);
            }

            if(Arrays.asList(environment.getActiveProfiles()).contains("reddisDB")){
                userRepositoryRedis.save(user3);
            }

        }

        if(userRepo.findByUsername("user3@mail.com").isEmpty()) {
            User user3 = new User("user3@mail.com", encoder.encode("userPW3"),
                    "Antonio Pedro", "254148863", "Rua vinte");
            UserRedis user4 = new UserRedis("user3@mail.com", encoder.encode("userPW3"),
                    "Antonio Pedro", "254148863", "Rua vinte");
            UserMongo user5 = new UserMongo("user3@mail.com", encoder.encode("userPW3"),
                    "Antonio Pedro", "254148863", "Rua vinte");
            user3.addAuthority(new Role(Role.RegisteredUser));
            user4.addAuthority(new Role(Role.RegisteredUser));
            user5.addAuthority(new Role(Role.RegisteredUser));

            if(Arrays.asList(environment.getActiveProfiles()).contains("neo4J")){
                userRepositoryNeo4J.save(userMapper.toUserNeo4J(user3));
            }
            userRepo.save(user3);
            if(Arrays.asList(environment.getActiveProfiles()).contains("mongoDB")){
                userRepositoryMongo.save(user5);
            }

            if(Arrays.asList(environment.getActiveProfiles()).contains("reddisDB")){
                userRepositoryRedis.save(user4);
            }

        }

        if(userRepo.findByUsername("user4@mail.com").isEmpty()) {
            User user4 = new User("user4@mail.com", encoder.encode("userPW4"),
                    "Pedro Antonio", "452369871", "Rua cinco");
            UserRedis user5 = new UserRedis("user4@mail.com", encoder.encode("userPW4"),
                    "Pedro Antonio", "452369871", "Rua cinco");
            UserMongo user6 = new UserMongo("user4@mail.com", encoder.encode("userPW4"),
                    "Pedro Antonio", "452369871", "Rua cinco");
            user4.addAuthority(new Role(Role.RegisteredUser));
            user5.addAuthority(new Role(Role.RegisteredUser));
            user6.addAuthority(new Role(Role.RegisteredUser));
            if(Arrays.asList(environment.getActiveProfiles()).contains("neo4J")){
                userRepositoryNeo4J.save(userMapper.toUserNeo4J(user4));
            }
            userRepo.save(user4);
            if(Arrays.asList(environment.getActiveProfiles()).contains("mongoDB")){
                userRepositoryMongo.save(user6);
            }
            if(Arrays.asList(environment.getActiveProfiles()).contains("reddisDB")){
                userRepositoryRedis.save(user5);
            }

        }

        if(userRepo.findByUsername("user5@mail.com").isEmpty()) {
            User user5 = new User("user5@mail.com", encoder.encode("userPW5"),
                    "Ricardo Joao", "452858596", "Rua seis");
            UserRedis user6 = new UserRedis("user5@mail.com", encoder.encode("userPW5"),
                    "Ricardo Joao", "452858596", "Rua seis");
            UserMongo user7 = new UserMongo("user5@mail.com", encoder.encode("userPW5"),
                    "Ricardo Joao", "452858596", "Rua seis");
            user5.addAuthority(new Role(Role.RegisteredUser));
            user6.addAuthority(new Role(Role.RegisteredUser));
            user7.addAuthority(new Role(Role.RegisteredUser));
            if(Arrays.asList(environment.getActiveProfiles()).contains("neo4J")){
                userRepositoryNeo4J.save(userMapper.toUserNeo4J(user5));
            }
            userRepo.save(user5);
            if(Arrays.asList(environment.getActiveProfiles()).contains("mongoDB")){
                userRepositoryMongo.save(user7);
            }

            if(Arrays.asList(environment.getActiveProfiles()).contains("reddisDB")){
                userRepositoryRedis.save(user6);
            }

        }

        if(userRepo.findByUsername("user6@mail.com").isEmpty()) {
            User user6 = new User("user6@mail.com", encoder.encode("userPW6"),
                    "Joao Ricardo", "425364781", "Rua sete");
            UserRedis user7 = new UserRedis("user6@mail.com", encoder.encode("userPW6"),
                    "Joao Ricardo", "425364781", "Rua sete");
            UserMongo user8 = new UserMongo("user6@mail.com", encoder.encode("userPW6"),
                    "Joao Ricardo", "425364781", "Rua sete");
            user6.addAuthority(new Role(Role.RegisteredUser));
            user7.addAuthority(new Role(Role.RegisteredUser));
            user8.addAuthority(new Role(Role.RegisteredUser));
            if(Arrays.asList(environment.getActiveProfiles()).contains("neo4J")){
                userRepositoryNeo4J.save(userMapper.toUserNeo4J(user6));
            }
            userRepo.save(user6);
            if(Arrays.asList(environment.getActiveProfiles()).contains("mongoDB")){
                userRepositoryMongo.save(user8);
            }

            if(Arrays.asList(environment.getActiveProfiles()).contains("reddisDB")){
                userRepositoryRedis.save(user7);
            }

        }
        Thread.sleep(1000);
        if(userRepo.findByUsername("user7@mail.com").isEmpty()) {
            User user7 = new User("user7@mail.com", encoder.encode("userPW7"),
                    "Luis Pedro", "526397747", "Rua oito");
            UserRedis user8 = new UserRedis("user7@mail.com", encoder.encode("userPW7"),
                    "Luis Pedro", "526397747", "Rua oito");
            UserMongo user9 = new UserMongo("user7@mail.com", encoder.encode("userPW7"),
                    "Luis Pedro", "526397747", "Rua oito");
            user7.addAuthority(new Role(Role.RegisteredUser));
            user8.addAuthority(new Role(Role.RegisteredUser));
            user9.addAuthority(new Role(Role.RegisteredUser));
            if(Arrays.asList(environment.getActiveProfiles()).contains("neo4J")){
                userRepositoryNeo4J.save(userMapper.toUserNeo4J(user7));
            }
            userRepo.save(user7);
            if(Arrays.asList(environment.getActiveProfiles()).contains("mongoDB")){
                userRepositoryMongo.save(user9);
            }

            if(Arrays.asList(environment.getActiveProfiles()).contains("reddisDB")){
                userRepositoryRedis.save(user8);
            }

        }
        if(userRepo.findByUsername("user8@mail.com").isEmpty()) {
            User user8 = new User("user8@mail.com", encoder.encode("userPW8"),
                    "Pedro Luis", "523689471", "Rua nove ");
            UserRedis user9 = new UserRedis("user8@mail.com", encoder.encode("userPW8"),
                    "Pedro Luis", "523689471", "Rua nove ");
            UserMongo user10 = new UserMongo("user8@mail.com", encoder.encode("userPW8"),
                    "Pedro Luis", "523689471", "Rua nove ");
            user8.addAuthority(new Role(Role.RegisteredUser));
            user9.addAuthority(new Role(Role.RegisteredUser));
            user10.addAuthority(new Role(Role.RegisteredUser));
            if(Arrays.asList(environment.getActiveProfiles()).contains("neo4J")){
                userRepositoryNeo4J.save(userMapper.toUserNeo4J(user8));
            }
            userRepo.save(user8);
            if(Arrays.asList(environment.getActiveProfiles()).contains("mongoDB")){
                userRepositoryMongo.save(user10);
            }
            if(Arrays.asList(environment.getActiveProfiles()).contains("reddisDB")){
                userRepositoryRedis.save(user9);
            }

        }
        Thread.sleep(1000);
        if(userRepo.findByUsername("user9@mail.com").isEmpty()) {
            User user9 = new User("user9@mail.com", encoder.encode("userPW9"),
                    "Marco Antonio", "253148965", "Rua dez");
            UserRedis user10 = new UserRedis("user9@mail.com", encoder.encode("userPW9"),
                    "Marco Antonio", "253148965", "Rua dez");
            UserMongo user11 = new UserMongo("user9@mail.com", encoder.encode("userPW9"),
                    "Marco Antonio", "253148965", "Rua dez");
            user9.addAuthority(new Role(Role.RegisteredUser));
            user10.addAuthority(new Role(Role.RegisteredUser));
            user11.addAuthority(new Role(Role.RegisteredUser));
            if(Arrays.asList(environment.getActiveProfiles()).contains("neo4J")){
                userRepositoryNeo4J.save(userMapper.toUserNeo4J(user9));
            }

            userRepo.save(user9);
            if(Arrays.asList(environment.getActiveProfiles()).contains("mongoDB")){
                userRepositoryMongo.save(user11);
            }
            if(Arrays.asList(environment.getActiveProfiles()).contains("reddisDB")){
                userRepositoryRedis.save(user10);
            }


        }
        Thread.sleep(1000);
        if(userRepo.findByUsername("user10@mail.com").isEmpty()) {
            User user10 = new User("user10@mail.com", encoder.encode("userPW10"),
                    "Antonio Marco", "201023056", "Rua onze");
            UserRedis user11 = new UserRedis("user10@mail.com", encoder.encode("userPW10"),
                    "Antonio Marco", "201023056", "Rua onze");
            UserMongo user12 = new UserMongo("user10@mail.com", encoder.encode("userPW10"),
                    "Antonio Marco", "201023056", "Rua onze");
            user10.addAuthority(new Role(Role.RegisteredUser));
            user11.addAuthority(new Role(Role.RegisteredUser));
            user12.addAuthority(new Role(Role.RegisteredUser));
            if(Arrays.asList(environment.getActiveProfiles()).contains("neo4J")){
                userRepositoryNeo4J.save(userMapper.toUserNeo4J(user10));
            }

            userRepo.save(user10);
            if(Arrays.asList(environment.getActiveProfiles()).contains("mongoDB")){
                userRepositoryMongo.save(user12);
            }
            if(Arrays.asList(environment.getActiveProfiles()).contains("reddisDB")){
                userRepositoryRedis.save(user11);
            }

        }
        if(userRepo.findByUsername("user11@mail.com").isEmpty()) {
            User user11 = new User("user11@mail.com", encoder.encode("userPW11"),
                    "Rui Ricardo", "748526326", "Rua doze");
            UserRedis user12 = new UserRedis("user11@mail.com", encoder.encode("userPW11"),
                    "Rui Ricardo", "748526326", "Rua doze");
            UserMongo user13 = new UserMongo("user11@mail.com", encoder.encode("userPW11"),
                    "Rui Ricardo", "748526326", "Rua doze");
            user11.addAuthority(new Role(Role.RegisteredUser));
            user12.addAuthority(new Role(Role.RegisteredUser));
            user13.addAuthority(new Role(Role.RegisteredUser));
            if(Arrays.asList(environment.getActiveProfiles()).contains("neo4J")){
                userRepositoryNeo4J.save(userMapper.toUserNeo4J(user11));
            }

            userRepo.save(user11);
            if(Arrays.asList(environment.getActiveProfiles()).contains("mongoDB")){
                userRepositoryMongo.save(user13);
            }
            if(Arrays.asList(environment.getActiveProfiles()).contains("reddisDB")){
                userRepositoryRedis.save(user12);
            }

        }

    }

}

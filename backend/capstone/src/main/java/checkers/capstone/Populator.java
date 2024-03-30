package checkers.capstone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import checkers.capstone.models.UserModel;
import checkers.capstone.repos.UserRepo;

@Component
public class Populator implements CommandLineRunner {

    private final UserRepo userRepo;

    public Populator(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        UserModel user1 = new UserModel("Jack", "j");
        UserModel user2 = new UserModel("Sarah", "s");
        UserModel user3 = new UserModel("Bob", "b");
        UserModel user4 = new UserModel("Billy", "b");
        UserModel user5 = new UserModel("Mandy", "m");
        UserModel user6 = new UserModel("Ruby", "d");
        userRepo.save(user1);
        userRepo.save(user2);
        userRepo.save(user3);
        userRepo.save(user4);
        userRepo.save(user5);
        userRepo.save(user6);
    }
    
}

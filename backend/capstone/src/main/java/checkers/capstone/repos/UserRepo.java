package checkers.capstone.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import checkers.capstone.models.UserModel;


@Repository
public interface UserRepo extends BaseRepo<UserModel> {
    
    boolean existsByUsername(String username);
    Optional<UserModel> findByUsername(String username);
    String findUsernameById(long id);//This doesn't work.
    List<UserModel> findAll();
}

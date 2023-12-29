package checkers.capstone.repos;

import org.springframework.stereotype.Repository;

import checkers.capstone.models.UserModel;

@Repository
public interface UserRepo extends BaseRepo<UserModel> {
    
}

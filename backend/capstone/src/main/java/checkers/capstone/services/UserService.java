package checkers.capstone.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import checkers.capstone.dtos.UserDTO;
import checkers.capstone.models.UserModel;
import checkers.capstone.repos.UserRepo;

@Service
public class UserService {

    private final UserRepo repo;

    public UserService(UserRepo repo) {
        this.repo = repo;
    }

    public boolean createUser(UserDTO dto){
        if (repo.existsByUsername(dto.getUsername())){
            return false;
        }
        repo.save(new UserModel(dto.getUsername(), dto.getPassword()));
        return true;
    }

    public long login(UserDTO dto){
        Optional<UserModel> oModel = repo.findByUsername(dto.getUsername());
        if (!oModel.isPresent())
            return 0;

        UserModel model = oModel.get();
        if (dto.getPassword().equals(model.getPassword()))
            return model.getId();
        else
            return 0;
    }

    public String[] getAllUsernames(){
        List<UserModel> models = repo.findAll();
        String[] results = new String[models.size()];
        for(int i=0; i<models.size(); i++){
            results[i] = models.get(i).getUsername();
        }
        return results;
    }
}

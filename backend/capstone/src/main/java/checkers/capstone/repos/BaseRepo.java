package checkers.capstone.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import checkers.capstone.models.BaseModel;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@NoRepositoryBean
public interface BaseRepo<Model extends BaseModel> extends CrudRepository<Model, Long> {
    
}

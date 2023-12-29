package checkers.capstone.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import checkers.capstone.game.rules.BaseRules;
import checkers.capstone.models.BoardModel;
import checkers.capstone.repos.BoardRepo;
import checkers.capstone.repos.custom.UpdateBoard;

@Service
public class BoardService {
    
    private final BoardRepo repo;
    private final UpdateBoard updateRepo;
    private final Map<Long, BaseRules> ruleMap = new HashMap<>();

    public BoardService(BoardRepo boardRepo, UpdateBoard updateRepo) {
        this.repo = boardRepo;
        this.updateRepo = updateRepo;
    }

    public BoardModel getById(long id){
        return repo.findById(id).get();
    }

    public BaseRules getRulesById(long id){
        if (ruleMap.containsKey(id))
            return ruleMap.get(id);

        BoardModel model = repo.findById(id).get();
        BaseRules rules = new BaseRules(model, updateRepo);
        ruleMap.put(id, rules);
        return rules;
    }
}

package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/commits")
public class CommitController {
    @Autowired
    private final CommitRepository commitRepository ;
    public CommitController(CommitRepository repository) {
        this.commitRepository = repository;
    }

    @GetMapping
    public List<Commit> findAll() {
        return commitRepository.findAll();
    }

    @GetMapping("/{id}")
    public Commit findOne(@PathVariable String id) throws CommitNotFoundException {
        Optional<Commit> commit = commitRepository.findById(id);
        if (!commit.isPresent()) {
            throw new CommitNotFoundException();
        }
        return commit.get();
    }

}

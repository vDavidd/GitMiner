package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.IssuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/issues")
public class IssuesController {

    @Autowired
    IssuesRepository repository;

    public IssuesController(IssuesRepository repository) { this.repository=repository; }

    @GetMapping
    public List<Issue> findAll(@RequestParam(required = false, name = "authorId") String authorId,
                               @RequestParam(required = false, name = "state") String state){
        List<Issue> issues = repository.findAll();
        if(authorId!=null)
            issues.removeIf(issue -> !issue.getAuthor().getId().equals(authorId));
        if(state!=null)
            issues.removeIf(issue -> !issue.getState().equals(state));
        return issues;
    }

    @GetMapping("/{id}")
    public Issue findOne(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        return issue.get();
    }

    @GetMapping("/{id}/comments")
    public List<Comment> findCommentsOfIssue(@PathVariable String id) throws IssueNotFoundException{
        Issue issue = findOne(id);
        return issue.getComments();
    }
}

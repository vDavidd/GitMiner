package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.IssuesRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Tag(name = "Issue", description = "Issue management API")
@RestController
@RequestMapping("/gitminer/issues")
public class IssuesController {

    @Autowired
    IssuesRepository repository;

    public IssuesController(IssuesRepository repository) { this.repository=repository; }

    @Operation(
            summary = "Retrieve a list of Issues",
            description = "Get a list of Issues",
            tags = { "issues", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List all issues",
                    content = { @Content(schema = @Schema(implementation =
                            Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Issues not found",
                    content = { @Content(schema = @Schema()) }) })
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


    @Operation(
            summary = "Retrieve a Issue by id",
            description = "Get a Issue by id",
            tags = { "issue", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Issue by id",
                    content = { @Content(schema = @Schema(implementation =
                            Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Issue not found",
                    content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}")
    public Issue findOne(@Parameter(description = "id of the issue to be searched") @PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        return issue.get();
    }
    @Operation(
            summary = "Retrieve a comment of a Issue by Issue id",
            description = "Get a comment of a Issue by Issue id",
            tags = { "issue","comment", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment of a Issue by id",
                    content = { @Content(schema = @Schema(implementation =
                            Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Comment not found",
                    content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}/comments")
    public List<Comment> findCommentsOfIssue(@PathVariable String id) throws IssueNotFoundException{
        Issue issue = findOne(id);
        return issue.getComments();
    }
}

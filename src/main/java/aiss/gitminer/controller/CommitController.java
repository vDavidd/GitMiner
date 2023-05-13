package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.CommitRepository;
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

@Tag(name = "Commit", description = "Commit management API")
@RestController
@RequestMapping("/gitminer/commits")
public class CommitController {
    @Autowired
    private final CommitRepository commitRepository ;
    public CommitController(CommitRepository repository) {
        this.commitRepository = repository;
    }

    @Operation(
            summary = "Retrieve a list of Commits",
            description = "Get a list of Commits",
            tags = { "commit", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List all commits",
                    content = { @Content(schema = @Schema(implementation =
                            Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Commits not found",
                    content = { @Content(schema = @Schema()) }) })
    @GetMapping
    public List<Commit> findAll() {
        return commitRepository.findAll();
    }


    @Operation(
            summary = "Retrieve a Commit by id",
            description = "Get a Commit by id",
            tags = { "commit", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commit by id",
                    content = { @Content(schema = @Schema(implementation =
                            Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Commit not found",
                    content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}")
    public Commit findOne(@Parameter(description = "id of the commit to be searched")@PathVariable String id) throws CommitNotFoundException {
        Optional<Commit> commit = commitRepository.findById(id);
        if (!commit.isPresent()) {
            throw new CommitNotFoundException();
        }
        return commit.get();
    }

}

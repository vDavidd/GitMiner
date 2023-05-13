package aiss.gitminer.controller;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.CommentRepository;
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


@Tag(name = "Comment", description = "Comment management API")
@RestController
@RequestMapping("/gitminer/comments")
public class CommentController {
    @Autowired
    private final CommentRepository commentRepository;

    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Operation(
            summary = "Retrieve a list of Comments",
            description = "Get a list of Comments",
            tags = { "comments", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List all comments",
                    content = { @Content(schema = @Schema(implementation =
                            Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Comments not found",
                    content = { @Content(schema = @Schema()) }) })
    @GetMapping
    public List<Comment> findAll(){
        return commentRepository.findAll();
    }


    @Operation(
            summary = "Retrieve a Comment by id",
            description = "Get a Comment by id",
            tags = { "comment", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment by id",
                    content = { @Content(schema = @Schema(implementation =
                            Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Comment not found",
                    content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}")
    public Comment findOne(@Parameter(description = "id of the comment to be searched") @PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);

        if(!comment.isPresent())
            throw new CommentNotFoundException();

        return comment.get();
    }


}
package aiss.gitminer.controller;


import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

// API DOCUMENTATION: http://localhost:8080/swagger-ui/index.html
@Tag(name = "Project", description = "Project management API")
@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository repository) {
        this.projectRepository = repository;
    }

    @Operation(
            summary = "Retrieve a list of Projects",
            description = "Get a list of Projects",
            tags = { "projects", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List all projects",
                    content = { @Content(schema = @Schema(implementation =
                            Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Projects not found",
                    content = { @Content(schema = @Schema()) }) })
    @GetMapping
    public List<Project> findAll() {
        return projectRepository.findAll();
    }


    @Operation(
            summary = "Retrieve a Project by id",
            description = "Get a Project by id",
            tags = { "project", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project by id",
                    content = { @Content(schema = @Schema(implementation =
                            Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Project not found",
                    content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}")
    public Project findOne(@Parameter(description = "id of the project to be searched") @PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            throw new ProjectNotFoundException();
        }
        return project.get();
    }

    @Operation(
            summary = "Insert a Project",
            description = "Add a new project whose data is passed in the body",
            tags = { "projects", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = { @Content(schema = @Schema(implementation =
                            Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400",
                    content = { @Content(schema = @Schema()) }) })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@RequestBody @Valid Project project) {
        return projectRepository.save(new Project(project.getId(), project.getName(), project.getWebUrl(),
                project.getCommits(),project.getIssues()));
    }

    @Operation(
            summary = "Update a a Projects",
            description = "Update a Project",
            tags = { "project", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Update a project",
                    content = { @Content(schema = @Schema(implementation =
                            Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description="Bad request",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description="Project not found",
                    content = { @Content(schema = @Schema()) }) })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Project putProject(@PathVariable String id, @RequestBody @Valid Project infoToChange) throws ProjectNotFoundException{
        Optional<Project> project = projectRepository.findById(id);

        Project updateProject = project.get();

        updateProject.setName(infoToChange.getName());
        updateProject.setWebUrl(infoToChange.getWebUrl());
        updateProject.setCommits(infoToChange.getCommits());
        updateProject.setIssues(infoToChange.getIssues());

        projectRepository.save(updateProject);
        return updateProject;
    }

    @Operation(
            summary = "Delete a a Projects",
            description = "Delete a Project",
            tags = { "projectd", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Delete a project",
                    content = { @Content(schema = @Schema(implementation =
                            Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description="Bad request",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404",description="Project not found",
                    content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable String id){
        if(projectRepository.existsById(id))
            projectRepository.deleteById(id);
    }
}


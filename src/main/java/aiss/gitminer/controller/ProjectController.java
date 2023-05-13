package aiss.gitminer.controller;


import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository repository) {
        this.projectRepository = repository;
    }

    @GetMapping
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @GetMapping("/{id}")
    public Project findOne(@PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            throw new ProjectNotFoundException();
        }
        return project.get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@RequestBody @Valid Project project) {
        return projectRepository.save(new Project(project.getId(), project.getName(), project.getWebUrl(),
                project.getCommits(),project.getIssues()));
    }

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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable String id){
        if(projectRepository.existsById(id))
            projectRepository.deleteById(id);
    }
}


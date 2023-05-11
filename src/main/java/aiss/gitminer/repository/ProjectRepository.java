package aiss.gitminer.repository;


import aiss.gitminer.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project,String> { //String porque el id de Project es de ese tipo.
}


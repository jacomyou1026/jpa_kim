package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;

public interface TeamRepositroy extends JpaRepository<Team,Long> {
}

package hackathon.code.repository;

import hackathon.code.model.Leader;
import hackathon.code.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaderRepository extends JpaRepository<Leader, Long> {
    Optional<Leader> findByUser(User user);
}

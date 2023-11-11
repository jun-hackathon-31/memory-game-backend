package hackathon.code.repository;

import hackathon.code.model.Leader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderRepository extends JpaRepository<Leader, Long> {
}

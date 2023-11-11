package hackathon.code.repository;

import hackathon.code.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {
//    Optional<Round> findByUserName(String userName);
}

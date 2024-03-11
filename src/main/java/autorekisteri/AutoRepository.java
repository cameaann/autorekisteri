package autorekisteri;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutoRepository extends JpaRepository<Auto, Long> {
    @EntityGraph(value = "Auto.omistajat")
    List<Auto> findByIdNotNull();
}

package autorekisteri;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OmistajaRepository extends JpaRepository<Omistaja, Long> {

}

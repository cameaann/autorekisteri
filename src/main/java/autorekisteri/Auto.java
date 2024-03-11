package autorekisteri;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(name="Auto.omistajat",
        attributeNodes = {@NamedAttributeNode("omistajat")})
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Auto extends AbstractPersistable<Long> {
    private String valmistenumero;
    private String rekisterinumero;
    private String merkki;
    private String malli;
    private Integer valmistusvuosi;

    @ManyToMany(mappedBy = "autot")
    private List<Omistaja> omistajat = new ArrayList<>();

}

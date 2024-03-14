package autorekisteri;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;


@Controller
public class AutorekisteriController {

    private final AutoRepository autoRepository;
    private final OmistajaRepository omistajaRepository;

    public AutorekisteriController(AutoRepository autoRepos, OmistajaRepository omistajaRepos) {
        this.autoRepository = autoRepos;
        this.omistajaRepository = omistajaRepos;
    }

    @GetMapping("/")
    public String showMain() {
        return "index";
    }

    @GetMapping("/car-add")
    public String showAddAutoForm(Model model) {
        model.addAttribute("omistajat", this.omistajaRepository.findAll());
        model.addAttribute("autot", this.autoRepository.findAll());
        return "car-add";
    }

    @PostMapping("/car-add")
    public String AddAuto(@RequestParam String valmistenumero,
                          @RequestParam String rekisterinumero,
                          @RequestParam String merkki,
                          @RequestParam String malli,
                          @RequestParam String valmistusvuosi){

        if(!valmistenumero.isEmpty() && !rekisterinumero.isEmpty() && !merkki.isEmpty() && !malli.isEmpty()){
            int valmVuosi = Integer.parseInt(valmistusvuosi);
            Auto auto = new Auto(valmistenumero, rekisterinumero,
                    merkki, malli, valmVuosi, new ArrayList<>());
            this.autoRepository.save(auto);

            return "redirect:/car-edit/"+ auto.getId();
        }

        return "/car-add";
    }

    @GetMapping("/car-edit/{autoId}")
    public String getCar(Model model, @PathVariable("autoId") Long autoId) {
        model.addAttribute("omistajat", this.omistajaRepository.findAll());
        Auto auto = this.autoRepository.getReferenceById(autoId);
        model.addAttribute("auto", auto);
        return "car-edit";
    }

    @Transactional
    @PostMapping("/car-edit/{autoId}/owners")
    public String addOwner(
            Model model,
            @PathVariable Long autoId,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastname
            ) {
        if (ownerId != null) {
            Omistaja omistaja = this.omistajaRepository.getReferenceById(ownerId);
            Auto auto = this.autoRepository.getReferenceById(autoId);
            if(!omistaja.getAutot().contains(auto)){
                omistaja.getAutot().add(auto);
            }else{
                model.addAttribute("error", "Tämä omistaja on jo listassa");
                return "redirect:/car-edit/" + autoId;
            }


        } else if (name != null && lastname != null) {
            Omistaja omistaja = new Omistaja(name, lastname, new ArrayList<>());
            this.omistajaRepository.save(omistaja);
            Auto auto = this.autoRepository.getReferenceById(autoId);
            omistaja.getAutot().add(auto);
            this.omistajaRepository.save(omistaja);

        } else {
            throw new IllegalArgumentException("Existing or new owner expected");
        }
        return "redirect:/car-edit/" + autoId;
    }


    @GetMapping("/car-list")
    public String showOmistajatAutot(Model model) {
        model.addAttribute("autot", this.autoRepository.findByIdNotNull());
        return "car-list";
    }
}

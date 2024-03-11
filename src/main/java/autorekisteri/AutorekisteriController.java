package autorekisteri;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

            int valmVuosi = Integer.parseInt(valmistusvuosi);
            Auto auto = new Auto(valmistenumero, rekisterinumero,
                    merkki, malli, valmVuosi, new ArrayList<>());
            this.autoRepository.save(auto);

        return "redirect:/car-edit/"+ auto.getId();
    }

    @GetMapping("/car-edit/{autoId}")
    public String getCar(Model model, @PathVariable("autoId") Long autoId) {
        model.addAttribute("omistajat", this.omistajaRepository.findAll());
        Auto auto = this.autoRepository.getReferenceById(autoId);
        model.addAttribute("auto", auto);
        return "car-edit";
    }

    @PostMapping("/car-edit/{autoId}/owners")
    public String addOwner(
            @PathVariable Long autoId,
            @RequestParam Long ownerId) {
        Omistaja omistaja = this.omistajaRepository.getReferenceById(ownerId);
        Auto auto = this.autoRepository.getReferenceById(autoId);
        omistaja.getAutot().add(auto);
        this.omistajaRepository.save(omistaja);

        return "redirect:/car-edit/" + autoId;
    }

//    @PostMapping("/car-edit/")
//    public String addNewOwner(@RequestParam String name,
//                           @RequestParam String lastname) {
//        if (!name.isEmpty() && !lastname.isEmpty()) {
//            Omistaja omistaja = new Omistaja(name, lastname, new ArrayList<>());
//            this.omistajaRepository.save(omistaja);
//        }
//
//        return "redirect:/addOwner";
//    }

    @GetMapping("/car-list")
    public String showOmistajatAutot(Model model) {
        model.addAttribute("autot", this.autoRepository.findByIdNotNull());
        return "car-list";
    }
}

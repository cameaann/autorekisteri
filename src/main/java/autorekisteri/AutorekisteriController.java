package autorekisteri;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AutorekisteriController {

    @GetMapping("/")
    public String showMain(){
        return "index";
    }
}

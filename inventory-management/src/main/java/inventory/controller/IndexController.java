package inventory.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping("/index")
	public String index(HttpSession session) {
//		Integer number = (Integer) session.getAttribute("randomNumber");
//		number = number == 1 ? 0 : 1;
//		session.setAttribute("randomNumber", number);
		return "index";
	}
}

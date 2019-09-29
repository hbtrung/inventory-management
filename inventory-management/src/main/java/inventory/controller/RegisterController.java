package inventory.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import inventory.model.RegisterForm;
import inventory.model.Users;
import inventory.service.UserService;
import inventory.util.Constant;
import inventory.validate.RegisterValidator;

@Controller
public class RegisterController {

	private static Logger logger = Logger.getLogger(RegisterController.class);
	@Autowired
	private UserService userService;
	
	@Autowired
	private RegisterValidator registerValidator;
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		if (binder.getTarget() == null) return;
		if (binder.getTarget().getClass() == RegisterForm.class) {
			binder.setValidator(registerValidator);
		}
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("registerForm", new RegisterForm());
		return "login/register";
	}
	
	@PostMapping("/processRegister")
	public String processRegister(Model model, @ModelAttribute("registerForm") @Validated RegisterForm userForm, 
			BindingResult bindingResult, HttpSession session) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("registerForm", userForm);
			return "login/register";
		}
		try {
			userService.save(userForm);
			session.setAttribute(Constant.MSG_SUCCESS, "Register successful!");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			session.setAttribute(Constant.MSG_ERROR, "Register failed!");
		}
		return "redirect:/login";
	}
}

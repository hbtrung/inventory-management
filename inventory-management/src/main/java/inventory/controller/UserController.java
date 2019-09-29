package inventory.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import inventory.model.Paging;
import inventory.model.Role;
import inventory.model.UserRole;
import inventory.model.Users;
import inventory.service.RoleService;
import inventory.service.UserService;
import inventory.util.Constant;
import inventory.validate.UserValidator;

@Controller
public class UserController {

	private static Logger logger = Logger.getLogger(UserController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserValidator userValidator;
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		if (binder.getTarget() == null) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
		if (binder.getTarget().getClass() == Users.class) {
			binder.setValidator(userValidator);
		}
	}
	
	@RequestMapping("/user/list")
	public String showAllUser() {
		return "redirect:/user/list/1";
	}
	
	@RequestMapping("/user/list/{pageIndex}")
	public String showUserList(Model model, HttpSession session, @ModelAttribute("searchForm") Users user, 
			@PathVariable("pageIndex") int pageIndex) {
		Paging paging = new Paging(10);
		paging.setPageIndex(pageIndex);
		List<Users> users = userService.findAll(user, paging);
		if (session.getAttribute(Constant.MSG_SUCCESS) != null) {
			model.addAttribute(Constant.MSG_SUCCESS, session.getAttribute(Constant.MSG_SUCCESS));
			session.removeAttribute(Constant.MSG_SUCCESS);
		}
		if (session.getAttribute(Constant.MSG_ERROR) != null) {
			model.addAttribute(Constant.MSG_ERROR, session.getAttribute(Constant.MSG_ERROR));
			session.removeAttribute(Constant.MSG_ERROR);
		}
		model.addAttribute("pageInfo", paging);
		model.addAttribute("users", users);
		return "user-list";
	}
	
	@GetMapping("/user/add")
	public String add(Model model) {
		model.addAttribute("roleMap", initRoleMap());
		model.addAttribute("pageTitle", "Add User");
		model.addAttribute("modelForm", new Users());
		model.addAttribute("viewOnly", false);
		return "user-action";
	}
	
	@GetMapping("/user/edit/{id}")
	public String edit(Model model, @PathVariable("id") int id) {
		logger.info("Edit user with id = " + id);
		Users user = userService.find(id);
		if (user != null) {
			UserRole userRole = (UserRole) user.getUserRoles().iterator().next();
			user.setRoleId(userRole.getRole().getId());
			model.addAttribute("roleMap", initRoleMap());
			model.addAttribute("pageTitle", "Edit User");
			model.addAttribute("modelForm", user);
			model.addAttribute("viewOnly", false);
			model.addAttribute("editMode", true);
			return "user-action";
		}
		return "redirect:/user/list";
	}
	
	@GetMapping("/user/view/{id}")
	public String view(Model model, @PathVariable("id") int id) {
		logger.info("View user with id = " + id);
		Users user = userService.find(id);
		if (user != null) {
			UserRole userRole = (UserRole) user.getUserRoles().iterator().next();
			model.addAttribute("userRoleName", userRole.getRole().getRoleName());
			model.addAttribute("pageTitle", "View User");
			model.addAttribute("modelForm", user);
			model.addAttribute("viewOnly", true);
			model.addAttribute("editMode", true);
			return "user-action";
		}
		return "redirect:/user/list";
	}
	
	@PostMapping("/user/save")
	public String save(Model model, @ModelAttribute("modelForm") @Validated Users user, 
			BindingResult bindingResult, HttpSession session) {
		if (bindingResult.hasErrors()) {
			
			for (Object err : bindingResult.getAllErrors()) {
				System.out.println(err);
			}
			if (user.getId() != null) {
				model.addAttribute("pageTitle", "Edit User");
				model.addAttribute("editMode", true);
			} else {
				model.addAttribute("pageTitle", "Add User");
			}
			model.addAttribute("roleMap", initRoleMap());
			model.addAttribute("modelForm", user);
			model.addAttribute("viewOnly", false);
			return "user-action";
		}
		
		if (user.getId() != null && user.getId() != 0) {
			try {
				userService.update(user);
				session.setAttribute(Constant.MSG_SUCCESS, "Update successful!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				session.setAttribute(Constant.MSG_ERROR, "Update failed!");
			}
		} else {
			try {
				userService.save(user);
				session.setAttribute(Constant.MSG_SUCCESS, "Insert successful!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				session.setAttribute(Constant.MSG_ERROR, "Insert failed!");
			}
		}
		return "redirect:/user/list";
	}
	
	@GetMapping("/user/delete/{id}")
	public String delete(Model model, @PathVariable("id") int id, HttpSession session) {
		logger.info("Delete user with id = " + id);
		Users user = userService.find(id);
		if (user != null) {
			try {
				userService.delete(user);
				session.setAttribute(Constant.MSG_SUCCESS, "Delete successful!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(e.getMessage());
				session.setAttribute(Constant.MSG_ERROR, "Delete failed!");
			}
		}
		return "redirect:/user/list";
	}
	
	private Map<String, String> initRoleMap() {
		Map<String, String> roleMap = new HashMap<>();
		List<Role> roles = roleService.findAll(null, null);
		for (Role role : roles) {
			roleMap.put(role.getId().toString(), role.getRoleName());
		}
		return roleMap;
	}
}

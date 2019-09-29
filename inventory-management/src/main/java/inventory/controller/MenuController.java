package inventory.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import inventory.model.Auth;
import inventory.model.AuthForm;
import inventory.model.Menu;
import inventory.model.Paging;
import inventory.model.Role;
import inventory.model.UserRole;
import inventory.model.Users;
import inventory.service.MenuService;
import inventory.service.RoleService;
import inventory.service.UserService;
import inventory.util.Constant;

@Controller
public class MenuController {

	private static Logger logger = Logger.getLogger(MenuController.class);
	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	
	@RequestMapping("/menu/list")
	public String redirect() {
		return "redirect:/menu/list/1";
	}
	
	@RequestMapping("/menu/list/{pageIndex}")
	public String menuList(Model model, HttpSession session, @ModelAttribute("searchForm") Menu menu, 
			@PathVariable("pageIndex") int pageIndex) {
		Paging paging = new Paging(15);
		paging.setPageIndex(pageIndex);
		List<Menu> menus = menuService.findAll(null, paging);
		List<Role> roles = roleService.findAll(null, null);
		Collections.sort(roles, (o1, o2) -> o1.getId() - o2.getId());
		for (Menu m: menus) {
			Map<Integer, Integer> authMap = new TreeMap<>();
			for (Role role: roles) {
				authMap.put(role.getId(), 0);
			}
			for (Object obj: m.getAuths()) {
				Auth auth = (Auth) obj;
				authMap.put(auth.getRole().getId(), auth.getPermission());
			}
			m.setAuthMap(authMap);
		}
		if (session.getAttribute(Constant.MSG_SUCCESS) != null) {
			model.addAttribute(Constant.MSG_SUCCESS, session.getAttribute(Constant.MSG_SUCCESS));
			session.removeAttribute(Constant.MSG_SUCCESS);
		}
		if (session.getAttribute(Constant.MSG_ERROR) != null) {
			model.addAttribute(Constant.MSG_ERROR, session.getAttribute(Constant.MSG_ERROR));
			session.removeAttribute(Constant.MSG_ERROR);
		}
		model.addAttribute("pageInfo", paging);
		model.addAttribute("menus", menus);
		model.addAttribute("roles", roles);
		return "menu-list";
	}
	
	@GetMapping("/menu/change-status/{id}")
	public String change(Model model, @PathVariable("id") int id, HttpSession session) {
		try {
			menuService.changeStatus(id);
			
			// reset menu list and user info
			Users tmpUser = (Users) session.getAttribute(Constant.USER_INFO);
			Users user = userService.find("userName", tmpUser.getUserName()).get(0);
			UserRole userRole = (UserRole) user.getUserRoles().iterator().next();
			Role role = userRole.getRole();
			List<Menu> menuList = new ArrayList<>();
			List<Menu> childMenuList = new ArrayList<>();
			
			for(Object obj : role.getAuths()) {
				Auth auth = (Auth) obj;
				Menu menu = auth.getMenu();
				if (menu.getOrderIndex() != -1 && menu.getActiveFlag() == 1 && auth.getPermission() == 1 && auth.getActiveFlag() == 1) {
					menu.setMenuId(menu.getUrl().replace("/", "") + "Id");
					if(menu.getParentId() == 0) {
						menuList.add(menu);
					} else {
						childMenuList.add(menu);
					}
				}
			}
			
			for(Menu menu : menuList) {
				List<Menu> childList = new ArrayList<>();
				for (Menu childMenu : childMenuList) {
					if (childMenu.getParentId() == menu.getId()) {
						childList.add(childMenu);
					}
				}
				menu.setChild(childList);
			}
			
			sortMenu(menuList);
			for(Menu menu : menuList) {
				sortMenu(menu.getChild());
			}
			
			session.setAttribute(Constant.MENU_SESSION, menuList);
			session.setAttribute(Constant.USER_INFO, user);
			
			session.setAttribute(Constant.MSG_SUCCESS, "Status changed successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute(Constant.MSG_ERROR, "Status change failed!");
		}
		return "redirect:/menu/list";
	}
	
	@GetMapping("/menu/permission")
	public String permission(Model model) {
		model.addAttribute("modelForm", new AuthForm());
		initSelectbox(model);
		return "menu-permission";
	}
//	
	@PostMapping("/menu/update-permission")
	public String updatePermission(Model model, HttpSession session, @ModelAttribute("modelForm") AuthForm authForm ) {
		try {
			menuService.updatePermission(authForm.getRoleId(), authForm.getMenuId(), authForm.getPermission());
			session.setAttribute(Constant.MSG_SUCCESS, "Update successful!");
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute(Constant.MSG_ERROR, "Update failed!");
		}
		return "redirect:/menu/list";
	}
	
	private void initSelectbox(Model model) {
		List<Role> roles = roleService.findAll(null, null);
		List<Menu> menus = menuService.findAll(null, null);
		Map<Integer, String> roleMap = new HashMap<>();
		Map<Integer, String> menuMap = new HashMap<>();
		for(Role role :roles) {
			roleMap.put(role.getId(), role.getRoleName());
		}
		for(Menu menu:menus) {
			menuMap.put(menu.getId(), menu.getUrl());
		}
		model.addAttribute("roleMap", roleMap);
		model.addAttribute("menuMap", menuMap);
	}
	
	private void sortMenu(List<Menu> menus) {
		Collections.sort(menus, new Comparator<Menu>() {
			@Override
			public int compare(Menu m1, Menu m2) {
				return m1.getOrderIndex() - m2.getOrderIndex();
			}
		});
	}
}

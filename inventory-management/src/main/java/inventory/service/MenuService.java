package inventory.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import inventory.dao.AuthDAO;
import inventory.dao.MenuDAO;
import inventory.model.Auth;
import inventory.model.Menu;
import inventory.model.Paging;
import inventory.model.Role;

@Service
public class MenuService {

	private static Logger logger = Logger.getLogger(MenuService.class);
	@Autowired
	private MenuDAO<Menu> menuDAO;
	@Autowired
	private AuthDAO<Auth> authDAO;
	
	public List<Menu> findAll(Menu menu, Paging paging) {
		logger.info("show all menu");
		StringBuilder queryStr = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		queryStr.append(" or model.activeFlag = 0");
		return menuDAO.findAll(queryStr.toString(), mapParams, paging);
	}
	
	public void changeStatus(int id) throws Exception {
		logger.info("change menu status of id=" + id);
		Menu menu = menuDAO.findById(Menu.class, id);
		if(menu!=null) {
			menu.setActiveFlag(menu.getActiveFlag() == 1 ? 0 : 1);
			menuDAO.update(menu);
		}
	}
	
	public void updatePermission(int roleId, int menuId, int permission) throws Exception {
		logger.info("update permission: roleId=" + roleId + ", menuId=" + menuId + ", p=" + permission);
		Auth auth = authDAO.find(roleId, menuId);
		if (auth != null) {
			auth.setPermission(permission);
			authDAO.update(auth);
		} else if (permission == 1){
			auth = new Auth();
			Menu menu = new Menu();
			menu.setId(menuId);
			Role role = new Role();
			role.setId(roleId);
			auth.setMenu(menu);
			auth.setRole(role);
			auth.setPermission(permission);
			auth.setActiveFlag(1);
			auth.setCreateDate(new Date());
			auth.setUpdateDate(new Date());
			authDAO.save(auth);
		}
	}
}

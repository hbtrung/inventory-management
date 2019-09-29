package inventory.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import inventory.dao.RoleDAO;
import inventory.model.Paging;
import inventory.model.Role;

@Service
public class RoleService {

	private static Logger logger = Logger.getLogger(RoleService.class);
	@Autowired
	private RoleDAO<Role> roleDAO;
	
	public List<Role> findAll(Role role, Paging paging) {
		logger.info("Find all roles");
		StringBuilder queryStr = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<String, Object>();
		if (role != null) {
			if (!StringUtils.isEmpty(role.getRoleName())) {
				queryStr.append(" and model.roleName like :roleName");
				mapParams.put("roleName", "%" + role.getRoleName() + "%");
			}
		}
		return roleDAO.findAll(queryStr.toString(), mapParams, paging);
	}
	
	public void save(Role role) throws Exception {
		logger.info("Insert role: " + role);
		role.setActiveFlag(1);
		role.setCreateDate(new Date());
		role.setUpdateDate(new Date());
		roleDAO.save(role);
	}
	
	public void update(Role role) throws Exception {
		logger.info("Update role: " + role);
		role.setUpdateDate(new Date());
		roleDAO.update(role);
	}
	
	public void delete(Role role) throws Exception {
		logger.info("Delete role: " + role);
		role.setActiveFlag(0);
		role.setUpdateDate(new Date());
		roleDAO.update(role);
	}
	
	public List<Role> find(String property, Object value) {
		logger.info("Find role by property: Property = " + property + ", value = " + value);
		return roleDAO.findByProperty(property, value);
	}
	
	public Role find(int id) {
		logger.info("Find role by id: " + id);
		return roleDAO.findById(Role.class, id);
	}
}

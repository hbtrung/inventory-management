package inventory.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import inventory.dao.UserDAO;
import inventory.dao.UserRoleDAO;
import inventory.model.Paging;
import inventory.model.RegisterForm;
import inventory.model.Role;
import inventory.model.UserRole;
import inventory.model.Users;
import inventory.util.PasswordHashing;

@Service
public class UserService {

	@Autowired
	private UserDAO<Users> userDAO;
	@Autowired
	private UserRoleDAO<UserRole> userRoleDAO;
	final static Logger logger = Logger.getLogger(UserService.class);
	
	public List<Users> findAll(Users user, Paging paging) {
		logger.info("Find all users");
		StringBuilder queryStr = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<String, Object>();
		if (user != null) {
			if (!StringUtils.isEmpty(user.getName())) {
				queryStr.append(" and model.name like :name");
				mapParams.put("name", "%" + user.getName() + "%");
			}
			if (!StringUtils.isEmpty(user.getUserName())) {
				queryStr.append(" and model.userName like :userName");
				mapParams.put("userName", "%" + user.getUserName() + "%");
			}
			if (!StringUtils.isEmpty(user.getEmail())) {
				queryStr.append(" and model.email like :email");
				mapParams.put("email", "%" + user.getEmail() + "%");
			}
		}
		return userDAO.findAll(queryStr.toString(), mapParams, paging);
	}
	
	public void save(Users user) throws Exception {
		logger.info("Insert user: " + user);
		user.setPassword(PasswordHashing.encrypt(user.getPassword()));
		user.setActiveFlag(1);
		user.setCreateDate(new Date());
		user.setUpdateDate(new Date());
		UserRole userRole = new UserRole();
		userRole.setUsers(user);
		Role role = new Role();
		role.setId(user.getRoleId());
		userRole.setRole(role);
		userRole.setActiveFlag(1);
		userRole.setCreateDate(new Date());
		userRole.setUpdateDate(new Date());
		userDAO.save(user);
		userRoleDAO.save(userRole);
	}
	
	public void save(RegisterForm userForm) throws Exception {
		logger.info("Insert user: " + userForm);
		Users user = new Users();
		user.setUserName(userForm.getUserName());
		user.setPassword(PasswordHashing.encrypt(userForm.getPassword()));
		user.setEmail(userForm.getEmail());
		user.setName(userForm.getName());
		UserRole userRole = new UserRole();
		userRole.setUsers(user);
		Role role = new Role();
		role.setId(4);
		userRole.setRole(role);
		user.setActiveFlag(1);
		user.setCreateDate(new Date());
		user.setUpdateDate(new Date());
		userRole.setActiveFlag(1);
		userRole.setCreateDate(new Date());
		userRole.setUpdateDate(new Date());
		userDAO.save(user);
		userRoleDAO.save(userRole);
	}
	
	public void update(Users tmpUser) throws Exception {
		logger.info("Update user: " + tmpUser);
		Users user = find(tmpUser.getId());
		if (user != null) {
			UserRole userRole = (UserRole) user.getUserRoles().iterator().next();
			Role role = new Role();
			role.setId(tmpUser.getRoleId());
			userRole.setRole(role);
			userRole.setUpdateDate(new Date());
			user.setUserName(tmpUser.getUserName());
			user.setName(tmpUser.getName());
			user.setEmail(tmpUser.getEmail());
			user.setUpdateDate(new Date());
			userRoleDAO.update(userRole);
		}
		userDAO.update(user);
	}
	
	public void delete(Users user) throws Exception {
		logger.info("Delete user: " + user);
		user.setActiveFlag(0);
		user.setUpdateDate(new Date());
		userDAO.update(user);
	}
	
	public List<Users> find(String property, Object value) {
		logger.info("Find user by property: Property = " + property + ", value = " + value);
		return userDAO.findByProperty(property, value);
	}
	
	public Users find(int id) {
		logger.info("Find user by id: " + id);
		return userDAO.findById(Users.class, id);
	}
}

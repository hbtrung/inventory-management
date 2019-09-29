package inventory.validate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import inventory.model.Role;
import inventory.service.RoleService;

@Component
public class RoleValidator implements Validator {

	@Autowired
	private RoleService roleService;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == Role.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		Role role = (Role) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roleName", "msg.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "msg.required");
		if (!StringUtils.isEmpty(role.getRoleName())) {
			List<Role> roles = roleService.find("roleName", role.getRoleName());
			if (roles != null && !roles.isEmpty()) {
				if (role.getId() != null && role.getId() != 0) {
					if (roles.get(0).getId() != role.getId()) {
						errors.rejectValue("roleName", "msg.rolename.exist");
					}
				} else {
					errors.rejectValue("roleName", "msg.rolename.exist");
				}	
			}
		}
	}
	
}

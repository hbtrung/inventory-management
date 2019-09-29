package inventory.validate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import inventory.model.Users;
import inventory.service.UserService;
import inventory.util.PasswordHashing;

@Component
public class LoginValidator implements Validator {
	
	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == Users.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		Users user = (Users) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "msg.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "msg.required");
		if (!StringUtils.isEmpty(user.getUserName()) && !StringUtils.isEmpty(user.getPassword())) {
			List<Users> users = userService.find("userName", user.getUserName());
			if (users != null && !users.isEmpty()) {
				if (!users.get(0).getPassword().equals(PasswordHashing.encrypt(user.getPassword()))) {
					errors.rejectValue("password", "msg.wrong.password");
				}
			} else {
				errors.rejectValue("userName", "msg.wrong.username");
			}
		}
	}
	
}

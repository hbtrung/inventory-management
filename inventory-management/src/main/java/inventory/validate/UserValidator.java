package inventory.validate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import inventory.model.Users;
import inventory.service.UserService;
import inventory.util.Constant;

@Component
public class UserValidator implements Validator {

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
		if (user.getId() == null) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "msg.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "msg.required");
		}
		if (!StringUtils.isEmpty(user.getUserName())) {
			List<Users> users = userService.find("userName", user.getUserName());
			if (users != null && !users.isEmpty()) {
				if (user.getId() != null && user.getId() != 0) {
					if (user.getId() != users.get(0).getId()) {
						errors.rejectValue("userName", "msg.username.exist");
					}
				} else {
					errors.rejectValue("userName", "msg.username.exist");
				}
			}
		}
		
		if (!StringUtils.isEmpty(user.getEmail())) {
			Pattern pattern = Pattern.compile(Constant.EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(user.getEmail());
			if(!matcher.matches()) {
				errors.rejectValue("email", "msg.wrong.email");
			}
		}
	}
}

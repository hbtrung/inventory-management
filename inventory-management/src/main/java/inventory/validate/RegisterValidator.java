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

import inventory.model.RegisterForm;
import inventory.model.Users;
import inventory.service.UserService;
import inventory.util.Constant;

@Component
public class RegisterValidator implements Validator {
	
	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == RegisterForm.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		RegisterForm user = (RegisterForm) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "msg.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "msg.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rePassword", "msg.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "msg.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "msg.required");
		
		if (!StringUtils.isEmpty(user.getUserName())) {
			List<Users> users = userService.find("userName", user.getUserName());
			if (users != null && !users.isEmpty()) {
				errors.rejectValue("userName", "msg.username.exist");
			}
		}
		if (!StringUtils.isEmpty(user.getRePassword())) {
			if (!user.getPassword().equals(user.getRePassword())) {
				errors.rejectValue("rePassword", "msg.password.notmatch");
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

package inventory.validate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import inventory.model.Category;
import inventory.service.ProductService;

@Component
public class CategoryValidator implements Validator {

	@Autowired
	private ProductService productService;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == Category.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		Category category = (Category) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "msg.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "msg.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "msg.required");
		if (!StringUtils.isEmpty(category.getCode())) {
			List<Category> categories = productService.findCategory("code", category.getCode());
			if (categories != null && !categories.isEmpty()) {
				if (category.getId() != null && category.getId() != 0) {
					if (categories.get(0).getId() != category.getId()) {
						errors.rejectValue("code", "msg.code.exist");
					}
				} else {
					errors.rejectValue("code", "msg.code.exist");
				}	
			}
		}
	}
	
}

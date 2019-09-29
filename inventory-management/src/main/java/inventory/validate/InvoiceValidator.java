package inventory.validate;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import inventory.model.Invoice;
import inventory.service.InvoiceService;

@Component
public class InvoiceValidator implements Validator {

	@Autowired
	private InvoiceService invoiceService;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == Invoice.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		Invoice invoice = (Invoice) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "msg.required");
		ValidationUtils.rejectIfEmpty(errors, "qty", "msg.required");
		ValidationUtils.rejectIfEmpty(errors, "price", "msg.required");
		if (invoice.getCode() != null) {
			List<Invoice> invoices = invoiceService.find("code", invoice.getCode());
			if (invoices != null && !invoices.isEmpty()) {
				if (invoice.getId() != null && invoice.getId() != 0) {
					if (invoices.get(0).getId() != invoice.getId()) {
						errors.rejectValue("code", "msg.code.exist");
					}
				} else {		
					errors.rejectValue("code", "msg.code.exist");
				}	
			}
		}
		try {
			if (invoice.getQty() <= 0) {
				errors.rejectValue("qty", "msg.wrong.value");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (invoice.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
				errors.rejectValue("price", "msg.wrong.value");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (invoice.getFromDate() != null && invoice.getToDate() != null) {
			if (invoice.getFromDate().after(invoice.getToDate())) {
				errors.rejectValue("fromDate", "msg.wrong.date");
			}
		}
	}
	
}

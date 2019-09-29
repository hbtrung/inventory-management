package inventory.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import inventory.model.Invoice;
import inventory.model.Paging;
import inventory.model.ProductInfo;
import inventory.service.InvoiceReport;
import inventory.service.InvoiceService;
import inventory.service.ProductService;
import inventory.util.Constant;
import inventory.validate.InvoiceValidator;

@Controller
public class GoodsIssueController {

	@Autowired
	private InvoiceService invoiceService;
	@Autowired
	private ProductService productService;
	@Autowired
	private InvoiceValidator invoiceValidator;
	
	private static Logger logger = Logger.getLogger(GoodsIssueController.class);
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		if (binder.getTarget() == null) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
		if (binder.getTarget().getClass() == Invoice.class) {
			binder.setValidator(invoiceValidator);
		}
	}
	
	@RequestMapping("/goods-issue/list")
	public String showAllInvoice() {
		return "redirect:/goods-issue/list/1";
	}
	
	@RequestMapping("/goods-issue/list/{pageIndex}")
	public String showInvoiceList(Model model, HttpSession session, @ModelAttribute("searchForm") Invoice invoice, 
			@PathVariable("pageIndex") int pageIndex) {
		Paging paging = new Paging(10);
		paging.setPageIndex(pageIndex);
		if (invoice == null) {
			invoice = new Invoice();
		}
		invoice.setType(Constant.TYPE_GOODS_ISSUE);
		List<Invoice> invoices = invoiceService.findAll(invoice, paging);
		if (session.getAttribute(Constant.MSG_SUCCESS) != null) {
			model.addAttribute(Constant.MSG_SUCCESS, session.getAttribute(Constant.MSG_SUCCESS));
			session.removeAttribute(Constant.MSG_SUCCESS);
		}
		if (session.getAttribute(Constant.MSG_ERROR) != null) {
			model.addAttribute(Constant.MSG_ERROR, session.getAttribute(Constant.MSG_ERROR));
			session.removeAttribute(Constant.MSG_ERROR);
		}
		model.addAttribute("pageInfo", paging);
		model.addAttribute("invoices", invoices);
		return "goods-issue-list";
	}
	
	@GetMapping("/goods-issue/add")
	public String add(Model model) {
		model.addAttribute("productMap", initProductMap());
		model.addAttribute("pageTitle", "Add Goods Issue");
		model.addAttribute("modelForm", new Invoice());
		model.addAttribute("viewOnly", false);
		return "goods-issue-action";
	}
	
	@GetMapping("/goods-issue/edit/{id}")
	public String edit(Model model, @PathVariable("id") int id) {
		logger.info("Edit invoice with id = " + id);
		Invoice invoice = invoiceService.find(id);
		if (invoice != null) {
			invoice.setProductId(invoice.getProductInfo().getId());
			model.addAttribute("productMap", initProductMap());
			model.addAttribute("pageTitle", "Edit Goods Issue");
			model.addAttribute("modelForm", invoice);
			model.addAttribute("viewOnly", false);
			return "goods-issue-action";
		}
		return "redirect:/goods-issue/list";
	}
	
	@GetMapping("/goods-issue/view/{id}")
	public String view(Model model, @PathVariable("id") int id) {
		logger.info("View invoice with id = " + id);
		Invoice invoice = invoiceService.find(id);
		if (invoice != null) {
			model.addAttribute("pageTitle", "View Goods Issue");
			model.addAttribute("modelForm", invoice);
			model.addAttribute("viewOnly", true);
			return "goods-issue-action";
		}
		return "redirect:/goods-issue/list";
	}
	
	@PostMapping("/goods-issue/save")
	public String save(Model model, @ModelAttribute("modelForm") @Validated Invoice invoice, 
			BindingResult bindingResult, HttpSession session) {
		if (bindingResult.hasErrors()) {
			if (invoice.getId() != null) {
				model.addAttribute("pageTitle", "Edit Goods Issue");
			} else {
				model.addAttribute("pageTitle", "Add Goods Issue");
			}
			model.addAttribute("productMap", initProductMap());
			model.addAttribute("modelForm", invoice);
			model.addAttribute("viewOnly", false);
			return "goods-issue-action";
		}
		
		// set productInfo
		ProductInfo product = new ProductInfo();
		product.setId(invoice.getProductId());
		invoice.setProductInfo(product);
		
		// set invoice type then save or update
		invoice.setType(Constant.TYPE_GOODS_ISSUE);
		if (invoice.getId() != null && invoice.getId() != 0) {
			try {
				invoiceService.update(invoice);
				session.setAttribute(Constant.MSG_SUCCESS, "Update successful!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				session.setAttribute(Constant.MSG_ERROR, "Update failed!");
			}
		} else {
			try {
				invoiceService.save(invoice);
				session.setAttribute(Constant.MSG_SUCCESS, "Insert successful!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				session.setAttribute(Constant.MSG_ERROR, "Insert failed!");
			}
		}
		return "redirect:/goods-issue/list";
	}
	
	@GetMapping("/goods-issue/export")
	public ModelAndView exportReport() {
		ModelAndView modelAndView = new ModelAndView();
		Invoice invoice = new Invoice();
		invoice.setType(Constant.TYPE_GOODS_ISSUE);
		List<Invoice> invoices = invoiceService.findAll(invoice, null);
		modelAndView.addObject(Constant.KEY_INVOICE_REPORT, invoices);
		modelAndView.setView(new InvoiceReport());
		return modelAndView;
	}
	
	private Map<String, String> initProductMap() {
		Map<String, String> productMap = new HashMap<>();
		List<ProductInfo> products = productService.findAllProductInfo(null, null);
		for (ProductInfo product : products) {
			productMap.put(product.getId().toString(), product.getName());
		}
		return productMap;
	}
}

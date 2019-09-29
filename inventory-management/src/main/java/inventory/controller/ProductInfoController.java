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

import inventory.model.Category;
import inventory.model.Paging;
import inventory.model.ProductInfo;
import inventory.service.ProductService;
import inventory.util.Constant;
import inventory.validate.ProductInfoValidator;

@Controller
public class ProductInfoController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductInfoValidator productInfoValidator;
	
	private static Logger logger = Logger.getLogger(ProductInfoController.class);
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		if (binder.getTarget() == null) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
		if (binder.getTarget().getClass() == ProductInfo.class) {
			binder.setValidator(productInfoValidator);
		}
	}
	
	@RequestMapping("/product-info/list")
	public String showAllProductInfo() {
		return "redirect:/product-info/list/1";
	}
	
	@RequestMapping("/product-info/list/{pageIndex}")
	public String showProductInfoList(Model model, HttpSession session, @ModelAttribute("searchForm") ProductInfo productInfo, 
			@PathVariable("pageIndex") int pageIndex) {
		Paging paging = new Paging(10);
		paging.setPageIndex(pageIndex);
		List<ProductInfo> products = productService.findAllProductInfo(productInfo, paging);
		if (session.getAttribute(Constant.MSG_SUCCESS) != null) {
			model.addAttribute(Constant.MSG_SUCCESS, session.getAttribute(Constant.MSG_SUCCESS));
			session.removeAttribute(Constant.MSG_SUCCESS);
		}
		if (session.getAttribute(Constant.MSG_ERROR) != null) {
			model.addAttribute(Constant.MSG_ERROR, session.getAttribute(Constant.MSG_ERROR));
			session.removeAttribute(Constant.MSG_ERROR);
		}
		model.addAttribute("pageInfo", paging);
		model.addAttribute("products", products);
		return "productInfo-list";
	}
	
	@GetMapping("/product-info/add")
	public String add(Model model) {
		List<Category> categories = productService.findAllCategory(null, null);
		Map<String, String> categoryMap = new HashMap<>();
		for (Category category : categories) {
			categoryMap.put(String.valueOf(category.getId()), category.getName());
		}
		model.addAttribute("categoryMap", categoryMap);
		model.addAttribute("pageTitle", "Add ProductInfo");
		model.addAttribute("modelForm", new ProductInfo());
		model.addAttribute("viewOnly", false);
		return "productInfo-action";
	}
	
	@GetMapping("/product-info/edit/{id}")
	public String edit(Model model, @PathVariable("id") int id) {
		logger.info("Edit productInfo with id = " + id);
		ProductInfo productInfo = productService.findProductInfo(id);
		if (productInfo != null) {
			List<Category> categories = productService.findAllCategory(null, null);
			Map<String, String> categoryMap = new HashMap<>();
			for (Category category : categories) {
				categoryMap.put(String.valueOf(category.getId()), category.getName());
			}
			productInfo.setCateId(productInfo.getCategory().getId());
			model.addAttribute("categoryMap", categoryMap);
			model.addAttribute("pageTitle", "Edit ProductInfo");
			model.addAttribute("modelForm", productInfo);
			model.addAttribute("viewOnly", false);
			return "productInfo-action";
		}
		return "redirect:/product-info/list";
	}
	
	@GetMapping("/product-info/view/{id}")
	public String view(Model model, @PathVariable("id") int id) {
		logger.info("View productInfo with id = " + id);
		ProductInfo productInfo = productService.findProductInfo(id);
		if (productInfo != null) {
			productInfo.setCateId(productInfo.getCategory().getId());
			model.addAttribute("pageTitle", "View ProductInfo");
			model.addAttribute("modelForm", productInfo);
			model.addAttribute("viewOnly", true);
			return "productInfo-action";
		}
		return "redirect:/product-info/list";
	}
	
	@PostMapping("/product-info/save")
	public String save(Model model, @ModelAttribute("modelForm") @Validated ProductInfo productInfo, 
			BindingResult bindingResult, HttpSession session) {
		if (bindingResult.hasErrors()) {
			if (productInfo.getId() != null) {
				model.addAttribute("pageTitle", "Edit ProductInfo");
			} else {
				model.addAttribute("pageTitle", "Add ProductInfo");
			}
			List<Category> categories = productService.findAllCategory(null, null);
			Map<String, String> categoryMap = new HashMap<>();
			for (Category category : categories) {
				categoryMap.put(String.valueOf(category.getId()), category.getName());
			}
			// no need to set cateId cuz already set
			model.addAttribute("categoryMap", categoryMap);
			model.addAttribute("modelForm", productInfo);
			model.addAttribute("viewOnly", false);
			return "productInfo-action";
		}
		
		// set category for productInfo
		Category category = new Category();
		category.setId(productInfo.getCateId());
		productInfo.setCategory(category);
		
		// save or update
		if (productInfo.getId() != null && productInfo.getId() != 0) {
			try {
				productService.updateProductInfo(productInfo);
				session.setAttribute(Constant.MSG_SUCCESS, "Update successful!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				session.setAttribute(Constant.MSG_ERROR, "Update failed!");
			}
		} else {
			try {
				productService.saveProductInfo(productInfo);
				session.setAttribute(Constant.MSG_SUCCESS, "Insert successful!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				session.setAttribute(Constant.MSG_ERROR, "Insert failed!");
			}
		}
		return "redirect:/product-info/list";
	}
	
	@GetMapping("/product-info/delete/{id}")
	public String delete(Model model, @PathVariable("id") int id, HttpSession session) {
		logger.info("Delete productInfo with id = " + id);
		ProductInfo productInfo = productService.findProductInfo(id);
		if (productInfo != null) {
			try {
				productService.deleteProductInfo(productInfo);
				session.setAttribute(Constant.MSG_SUCCESS, "Delete successful!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(e.getMessage());
				session.setAttribute(Constant.MSG_ERROR, "Delete failed!");
			}
		}
		return "redirect:/product-info/list";
	}
}

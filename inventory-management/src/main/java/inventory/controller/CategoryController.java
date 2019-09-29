package inventory.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import inventory.service.ProductService;
import inventory.util.Constant;
import inventory.validate.CategoryValidator;

@Controller
public class CategoryController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryValidator categoryValidator;
	
	private static Logger logger = Logger.getLogger(CategoryController.class);
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		if (binder.getTarget() == null) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
		if (binder.getTarget().getClass() == Category.class) {
			binder.setValidator(categoryValidator);
		}
	}
	
	@RequestMapping("/category/list")
	public String showAllCategory() {
		return "redirect:/category/list/1";
	}
	
	@RequestMapping("/category/list/{pageIndex}")
	public String showCategoryList(Model model, HttpSession session, @ModelAttribute("searchForm") Category category, 
			@PathVariable("pageIndex") int pageIndex) {
		Paging paging = new Paging(10);
		paging.setPageIndex(pageIndex);
		List<Category> categories = productService.findAllCategory(category, paging);
		if (session.getAttribute(Constant.MSG_SUCCESS) != null) {
			model.addAttribute(Constant.MSG_SUCCESS, session.getAttribute(Constant.MSG_SUCCESS));
			session.removeAttribute(Constant.MSG_SUCCESS);
		}
		if (session.getAttribute(Constant.MSG_ERROR) != null) {
			model.addAttribute(Constant.MSG_ERROR, session.getAttribute(Constant.MSG_ERROR));
			session.removeAttribute(Constant.MSG_ERROR);
		}
		model.addAttribute("pageInfo", paging);
		model.addAttribute("categories", categories);
		return "category-list";
	}
	
	@GetMapping("/category/add")
	public String add(Model model) {
		model.addAttribute("pageTitle", "Add Category");
		model.addAttribute("modelForm", new Category());
		model.addAttribute("viewOnly", false);
		return "category-action";
	}
	
	@GetMapping("/category/edit/{id}")
	public String edit(Model model, @PathVariable("id") int id) {
		logger.info("Edit category with id = " + id);
		Category category = productService.findCategory(id);
		if (category != null) {
			model.addAttribute("pageTitle", "Edit Category");
			model.addAttribute("modelForm", category);
			model.addAttribute("viewOnly", false);
			return "category-action";
		}
		return "redirect:/category/list";
	}
	
	@GetMapping("/category/view/{id}")
	public String view(Model model, @PathVariable("id") int id) {
		logger.info("View category with id = " + id);
		Category category = productService.findCategory(id);
		if (category != null) {
			model.addAttribute("pageTitle", "View Category");
			model.addAttribute("modelForm", category);
			model.addAttribute("viewOnly", true);
			return "category-action";
		}
		return "redirect:/category/list";
	}
	
	@PostMapping("/category/save")
	public String save(Model model, @ModelAttribute("modelForm") @Validated Category category, 
			BindingResult bindingResult, HttpSession session) {
		if (bindingResult.hasErrors()) {
			if (category.getId() != null) {
				model.addAttribute("pageTitle", "Edit Category");
			} else {
				model.addAttribute("pageTitle", "Add Category");
			}
			model.addAttribute("modelForm", category);
			model.addAttribute("viewOnly", false);
			return "category-action";
		}
		if (category.getId() != null && category.getId() != 0) {
			try {
				productService.updateCategory(category);
				session.setAttribute(Constant.MSG_SUCCESS, "Update successful!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				session.setAttribute(Constant.MSG_ERROR, "Update failed!");
			}
		} else {
			try {
				productService.saveCategory(category);
				session.setAttribute(Constant.MSG_SUCCESS, "Insert successful!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				session.setAttribute(Constant.MSG_ERROR, "Insert failed!");
			}
		}
		return "redirect:/category/list";
	}
	
	@GetMapping("/category/delete/{id}")
	public String delete(Model model, @PathVariable("id") int id, HttpSession session) {
		logger.info("Delete category with id = " + id);
		Category category = productService.findCategory(id);
		if (category != null) {
			try {
				productService.deleteCategory(category);
				session.setAttribute(Constant.MSG_SUCCESS, "Delete successful!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(e.getMessage());
				session.setAttribute(Constant.MSG_ERROR, "Delete failed!");
			}
		}
		return "redirect:/category/list";
	}
}

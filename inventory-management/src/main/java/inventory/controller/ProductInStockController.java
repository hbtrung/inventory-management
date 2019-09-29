package inventory.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import inventory.model.Paging;
import inventory.model.ProductInStock;
import inventory.service.ProductInStockService;

@Controller
public class ProductInStockController {

	@Autowired
	private ProductInStockService productInStockService;
	
	private static Logger logger = Logger.getLogger(ProductInStockController.class);
	
	@RequestMapping("/product-in-stock/list")
	public String redirect() {
		return "redirect:/product-in-stock/list/1";
	}
	
	@RequestMapping("/product-in-stock/list/{pageIndex}")
	public String list(Model model, HttpSession session, @ModelAttribute("searchForm") ProductInStock productInStock, 
			@PathVariable("pageIndex") int pageIndex) {
		Paging paging = new Paging(10);
		paging.setPageIndex(pageIndex);
		List<ProductInStock> products = productInStockService.getAll(productInStock, paging);
		model.addAttribute("pageInfo", paging);
		model.addAttribute("products", products);
		return "product-in-stock";
	}
}

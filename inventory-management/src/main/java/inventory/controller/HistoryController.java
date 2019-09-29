package inventory.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import inventory.model.History;
import inventory.model.Paging;
import inventory.service.HistoryService;
import inventory.util.Constant;

@Controller
public class HistoryController {

	@Autowired
	private HistoryService historyService;
	
	private static Logger logger = Logger.getLogger(HistoryController.class);
	
	@RequestMapping("/history/list")
	public String redirect() {
		return "redirect:/history/list/1";
	}
	
	@RequestMapping("/history/list/{pageIndex}")
	public String list(Model model, HttpSession session, @ModelAttribute("searchForm") History history, 
			@PathVariable("pageIndex") int pageIndex) {
		Paging paging = new Paging(10);
		paging.setPageIndex(pageIndex);
		List<History> histories = historyService.getAll(history, paging);
		Map<String, String> typeMap = new HashMap<>();
		typeMap.put(String.valueOf(Constant.TYPE_ALL), "All");
		typeMap.put(String.valueOf(Constant.TYPE_GOODS_RECEIPT), "Goods Receipt");
		typeMap.put(String.valueOf(Constant.TYPE_GOODS_ISSUE), "Goods Issue");
		model.addAttribute("typeMap", typeMap);
		model.addAttribute("pageInfo", paging);
		model.addAttribute("histories", histories);
		return "history";
	}
}

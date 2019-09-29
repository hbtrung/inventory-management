package inventory.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import inventory.dao.HistoryDAO;
import inventory.model.Category;
import inventory.model.History;
import inventory.model.Invoice;
import inventory.model.Paging;
import inventory.model.ProductInfo;

@Service
public class HistoryService {

	@Autowired
	private HistoryDAO<History> historyDAO;
	
	final static Logger logger = Logger.getLogger(HistoryService.class);
	
	public List<History> getAll(History history, Paging paging) {
		logger.info("Find all histories");
		StringBuilder queryStr = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<String, Object>();
		if (history != null) {
			if (history.getProductInfo() != null) {
				ProductInfo tmpProductInfo = history.getProductInfo();
				Category tmpCategory = tmpProductInfo.getCategory();
				if (tmpCategory != null && !StringUtils.isEmpty(tmpCategory.getName())) {
					queryStr.append(" and model.productInfo.category.name like :cateName");
					mapParams.put("cateName", "%" + tmpCategory.getName() + "%");
				}
				if (!StringUtils.isEmpty(tmpProductInfo.getCode())) {
					queryStr.append(" and model.productInfo.code like :code");
					mapParams.put("code", "%" + tmpProductInfo.getCode() + "%");
				}
				if (!StringUtils.isEmpty(tmpProductInfo.getName())) {
					queryStr.append(" and model.productInfo.name like :name");
					mapParams.put("name", "%" + tmpProductInfo.getName() + "%");
				}
			}
			if (!StringUtils.isEmpty(history.getActionName())) {
				queryStr.append(" and model.action_name like :actionName");
				mapParams.put("actionName", "%" + history.getActionName() + "%");
			}
			if (history.getType() != 0) {
				queryStr.append(" and model.type = :type");
				mapParams.put("type", history.getType());
			}
		}
		return historyDAO.findAll(queryStr.toString(), mapParams, paging);
	}
	
	public void save(Invoice invoice, String action) throws Exception {
		logger.info("save history ");
		if (invoice.getProductInfo() != null) {
			logger.info("save history: actionName=" + action + ", type=" + invoice.getType() + ", qty=" 
					+ invoice.getQty() + ", price=" + invoice.getPrice());
			History history = new History();
			history.setProductInfo(invoice.getProductInfo());
			history.setActionName(action);
			history.setType(invoice.getType());
			history.setQty(invoice.getQty());
			history.setPrice(invoice.getPrice());
			history.setActiveFlag(1);
			history.setCreateDate(new Date());
			history.setUpdateDate(new Date());
			historyDAO.save(history);
		}
	}
}

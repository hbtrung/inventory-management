package inventory.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import inventory.dao.InvoiceDAO;
import inventory.model.Invoice;
import inventory.model.Paging;
import inventory.util.Constant;

@Service
public class InvoiceService {

	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProductInStockService productInStockService;
	@Autowired
	private InvoiceDAO<Invoice> invoiceDAO;
	
	private static Logger logger = Logger.getLogger(InvoiceService.class);
	
	public List<Invoice> findAll(Invoice invoice, Paging paging) {
		logger.info("find all invoices");
		StringBuilder queryStr = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		if (invoice != null) {
			if (invoice.getType() != 0) {
				queryStr.append(" and model.type = :type");
				mapParams.put("type", invoice.getType());
			}
			if (!StringUtils.isEmpty(invoice.getCode())) {
				queryStr.append(" and model.code = :code ");
				mapParams.put("code", invoice.getCode());
			}
			if(invoice.getFromDate() != null) {
				queryStr.append(" and model.updateDate >= :fromDate");
				mapParams.put("fromDate", invoice.getFromDate());
			}
			if(invoice.getToDate() != null) {
				queryStr.append(" and model.updateDate <= :toDate");
				mapParams.put("toDate", invoice.getToDate());
			}
		}
		return invoiceDAO.findAll(queryStr.toString(), mapParams, paging);
	}
	
	public List<Invoice> find(String property, Object value) {
		logger.info("find invoice by property: property=" + property + ", value=" + value);
		return invoiceDAO.findByProperty(property, value);
	}
	
	public Invoice find(int id) {
		logger.info("find invoice by id:" + id);
		return invoiceDAO.findById(Invoice.class, id);
	}
	
	public void save(Invoice invoice) throws Exception {
		logger.info("save invoice:" + invoice);
		invoice.setActiveFlag(1);
		invoice.setCreateDate(new Date());
		invoice.setUpdateDate(new Date());
		productInStockService.saveOrUpdate(invoice);
		historyService.save(invoice, Constant.ACTION_ADD);
		invoiceDAO.save(invoice);
	}
	
	public void update(Invoice invoice) throws Exception {
		logger.info("update invoice:" + invoice);
		int originalQty = invoiceDAO.findById(Invoice.class, invoice.getId()).getQty();
		Invoice invoice2 = new Invoice();
		invoice2.setProductInfo(invoice.getProductInfo());
		invoice2.setType(invoice.getType());
		invoice2.setQty(invoice.getQty() - originalQty);
		invoice2.setPrice(invoice.getPrice());
		invoice.setUpdateDate(new Date());
		productInStockService.saveOrUpdate(invoice2); // must go first
		historyService.save(invoice, Constant.ACTION_EDIT);
		invoiceDAO.update(invoice);
	}
}

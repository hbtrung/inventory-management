package inventory.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import inventory.dao.ProductInStockDAO;
import inventory.model.Category;
import inventory.model.Invoice;
import inventory.model.Paging;
import inventory.model.ProductInStock;
import inventory.model.ProductInfo;

@Service
public class ProductInStockService {

	@Autowired
	private ProductInStockDAO<ProductInStock> productInStockDAO;
	
	final static Logger logger = Logger.getLogger(ProductInStockService.class);
	
	public List<ProductInStock> getAll(ProductInStock productInStock, Paging paging) {
		logger.info("Find all productInStock");
		StringBuilder queryStr = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<String, Object>();
		if (productInStock != null && productInStock.getProductInfo() != null) {
			ProductInfo tmpProductInfo = productInStock.getProductInfo();
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
		return productInStockDAO.findAll(queryStr.toString(), mapParams, paging);
	}
	
	public void saveOrUpdate(Invoice invoice) throws Exception {
		logger.info("product in stock ");
		if (invoice.getProductInfo() != null) {
			// find by ProductInfo id not ProductInStock
			List<ProductInStock> products = productInStockDAO.findByProperty("productInfo.id", invoice.getProductInfo().getId());
			if (products != null && !products.isEmpty()) {
				logger.info("update stock qty=" + invoice.getQty() + " and price=" + invoice.getPrice());
				ProductInStock product = products.get(0);
				if (invoice.getType() == 1) {
					product.setQty(product.getQty() + invoice.getQty());
					product.setPrice(invoice.getPrice());
				} else {
					if (product.getQty() - invoice.getQty() >= 0) {
						product.setQty(product.getQty() - invoice.getQty());
					} else {
						throw new Exception("Insufficient supply!");
					}
				}
				product.setUpdateDate(new Date());
				productInStockDAO.update(product);
			} else if (invoice.getType() == 1){
				logger.info("insert to stock qty=" + invoice.getQty() + " and price=" + invoice.getPrice());
				ProductInStock product = new ProductInStock();
				ProductInfo productInfo = new ProductInfo();
				productInfo.setId(invoice.getProductInfo().getId());
				product.setProductInfo(productInfo);
				product.setQty(invoice.getQty());
				product.setPrice(invoice.getPrice());
				product.setActiveFlag(1);
				product.setCreateDate(new Date());
				product.setUpdateDate(new Date());
				productInStockDAO.save(product);
			}
		}
	}
	
//	public void saveProductInStock(ProductInStock productInStock) throws Exception {
//		logger.info("Insert productInStock: " + productInStock);
//		productInStock.setActiveFlag(1);
//		productInStock.setCreateDate(new Date());
//		productInStock.setUpdateDate(new Date());
//		productInStockDAO.save(productInStock);
//	}
//	
//	public void updateProductInStock(ProductInStock productInStock) throws Exception {
//		logger.info("Update productInStock: " + productInStock);
//		productInStock.setUpdateDate(new Date());
//		productInStockDAO.update(productInStock);
//	}
//	
//	public void deleteProductInStock(ProductInStock productInStock) throws Exception {
//		logger.info("Delete productInStock: " + productInStock);
//		productInStock.setActiveFlag(0);
//		productInStock.setUpdateDate(new Date());
//		productInStockDAO.update(productInStock);
//	}
//	
//	public List<ProductInStock> findProductInStock(String property, Object value) {
//		logger.info("Find productInStock by property: Property = " + property + ", value = " + value);
//		return productInStockDAO.findByProperty(property, value);
//	}
//	
//	public ProductInStock findProductInStock(int id) {
//		logger.info("Find productInStock by id: " + id);
//		return productInStockDAO.findById(ProductInStock.class, id);
//	}
}

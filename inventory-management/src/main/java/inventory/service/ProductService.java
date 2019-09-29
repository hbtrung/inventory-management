package inventory.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import inventory.dao.CategoryDAO;
import inventory.dao.ProductInfoDAO;
import inventory.model.Category;
import inventory.model.Paging;
import inventory.model.ProductInfo;
import inventory.util.ConfigLoader;

@Service
public class ProductService {

	@Autowired
	private CategoryDAO<Category> categoryDAO;
	@Autowired
	private ProductInfoDAO<ProductInfo> productInfoDAO;
	
	final static Logger logger = Logger.getLogger(ProductService.class);
	
	public List<Category> findAllCategory(Category category, Paging paging) {
		logger.info("Find all categories");
		StringBuilder queryStr = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<String, Object>();
		if (category != null) {
			if (category.getId() != null && category.getId() != 0) {
				queryStr.append(" and model.id=:id");
				mapParams.put("id", category.getId());
			}
			if (!StringUtils.isEmpty(category.getCode())) {
				queryStr.append(" and model.code like :code");
				mapParams.put("code", "%" + category.getCode() + "%");
			}
			if (!StringUtils.isEmpty(category.getName())) {
				queryStr.append(" and model.name like :name");
				mapParams.put("name", "%" + category.getName() + "%");
			}
		}
		return categoryDAO.findAll(queryStr.toString(), mapParams, paging);
	}
	
	public void saveCategory(Category category) throws Exception {
		logger.info("Insert category: " + category);
		category.setActiveFlag(1);
		category.setCreateDate(new Date());
		category.setUpdateDate(new Date());
		categoryDAO.save(category);
	}
	
	public void updateCategory(Category category) throws Exception {
		logger.info("Update category: " + category);
		category.setUpdateDate(new Date());
		categoryDAO.update(category);
	}
	
	public void deleteCategory(Category category) throws Exception {
		logger.info("Delete category: " + category);
		category.setActiveFlag(0);
		category.setUpdateDate(new Date());
		categoryDAO.update(category);
	}
	
	public List<Category> findCategory(String property, Object value) {
		logger.info("Find category by property: Property = " + property + ", value = " + value);
		return categoryDAO.findByProperty(property, value);
	}
	
	public Category findCategory(int id) {
		logger.info("Find category by id: " + id);
		return categoryDAO.findById(Category.class, id);
	}
	
	// PRODUCT INFO
	
	public List<ProductInfo> findAllProductInfo(ProductInfo productInfo, Paging paging) {
		logger.info("Find all productInfo");
		StringBuilder queryStr = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<String, Object>();
		if (productInfo != null) {
			if (productInfo.getId() != null && productInfo.getId() != 0) {
				queryStr.append(" and model.id=:id");
				mapParams.put("id", productInfo.getId());
			}
			if (!StringUtils.isEmpty(productInfo.getCode())) {
				queryStr.append(" and model.code like :code");
				mapParams.put("code", "%" + productInfo.getCode() + "%");
			}
			if (!StringUtils.isEmpty(productInfo.getName())) {
				queryStr.append(" and model.name like :name");
				mapParams.put("name", "%" + productInfo.getName() + "%");
			}
		}
		return productInfoDAO.findAll(queryStr.toString(), mapParams, paging);
	}
	
	public void saveProductInfo(ProductInfo productInfo) throws Exception {
		logger.info("Insert productInfo: " + productInfo);
		String fileName = System.currentTimeMillis() + "_" + productInfo.getMultipartFile().getOriginalFilename();
		processFileUpload(productInfo.getMultipartFile(), fileName);
		productInfo.setImgUrl("/upload/" + fileName);
		productInfo.setActiveFlag(1);
		productInfo.setCreateDate(new Date());
		productInfo.setUpdateDate(new Date());
		productInfoDAO.save(productInfo);
	}
	
	public void updateProductInfo(ProductInfo productInfo) throws Exception {
		logger.info("Update productInfo: " + productInfo);
		if (!productInfo.getMultipartFile().getOriginalFilename().isEmpty()) {
			String fileName = System.currentTimeMillis() + "_" + productInfo.getMultipartFile().getOriginalFilename();
			processFileUpload(productInfo.getMultipartFile(), fileName);
			productInfo.setImgUrl("/upload/" + fileName);
		}
		productInfo.setUpdateDate(new Date());
		productInfoDAO.update(productInfo);
	}
	
	public void deleteProductInfo(ProductInfo productInfo) throws Exception {
		logger.info("Delete productInfo: " + productInfo);
		productInfo.setActiveFlag(0);
		productInfo.setUpdateDate(new Date());
		productInfoDAO.update(productInfo);
	}
	
	public List<ProductInfo> findProductInfo(String property, Object value) {
		logger.info("Find productInfo by property: Property = " + property + ", value = " + value);
		return productInfoDAO.findByProperty(property, value);
	}
	
	public ProductInfo findProductInfo(int id) {
		logger.info("Find productInfo by id: " + id);
		return productInfoDAO.findById(ProductInfo.class, id);
	}
	
	private void processFileUpload(MultipartFile multipartFile, String fileName) throws IllegalStateException, IOException {
		if (!multipartFile.getOriginalFilename().isEmpty()) {
			File dir = new File(ConfigLoader.getInstance().getValue("upload.location"));
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(ConfigLoader.getInstance().getValue("upload.location"), fileName);
			multipartFile.transferTo(file);
		}
	}
}

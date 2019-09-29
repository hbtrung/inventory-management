package inventory.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import inventory.model.Paging;

@Repository
@Transactional(rollbackFor=Exception.class)
public class BaseDAOImpl<E> implements BaseDAO<E> {

	final static Logger logger = Logger.getLogger(BaseDAOImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public List<E> findAll(String queryStr, Map<String, Object> mapParams, Paging paging) {
		logger.info("find all record from db");
		StringBuilder queryString = new StringBuilder("");
		StringBuilder countQueryString = new StringBuilder();
		queryString.append(" from ").append(getGenericName()).append(" as model where model.activeFlag=1");
		countQueryString.append("select count(*) from ").append(getGenericName()).append(" as model where model.activeFlag=1");
		if (queryStr != null) {
			queryString.append(queryStr);
			countQueryString.append(queryStr);
		}
		Query<E> query = sessionFactory.getCurrentSession().createQuery(queryString.toString());
		Query<E> countQuery = sessionFactory.getCurrentSession().createQuery(countQueryString.toString());
		if (mapParams != null && !mapParams.isEmpty()) {
			for (String key : mapParams.keySet()) {
				query.setParameter(key, mapParams.get(key));
				countQuery.setParameter(key, mapParams.get(key));
			}
		}
		if (paging != null) {
			query.setFirstResult(paging.getOffset());
			query.setMaxResults(paging.getRecordPerPage());
			long totalRecords = (long) countQuery.uniqueResult();
			paging.setTotalRows(totalRecords);
		}
		logger.info("Query find all ====>" + queryString.toString());
		return query.list();
	}

	@Override
	public E findById(Class<E> e, Serializable id) {
		logger.info("Find by ID");
		return sessionFactory.getCurrentSession().get(e, id);
	}

	@Override
	public List<E> findByProperty(String property, Object value) {
		logger.info("Find by property");
		StringBuilder queryString = new StringBuilder("");
		queryString.append(" from ").append(getGenericName()).append(" as model where model.activeFlag=1")
			.append(" and model.").append(property).append("=:valueName");
		logger.info("Query find by property ====>" + queryString.toString());
		Query<E> query = sessionFactory.getCurrentSession().createQuery(queryString.toString());
		query.setParameter("valueName", value);
		return query.getResultList();
	}

	@Override
	public void save(E instance) {
		logger.info("save instance");		
		sessionFactory.getCurrentSession().persist(instance);
	}

	@Override
	public void update(E instance) {
		logger.info("update instance");		
		sessionFactory.getCurrentSession().merge(instance);
	}
	
	public String getGenericName() {
		String s = getClass().getGenericSuperclass().toString();
		Pattern pattern = Pattern.compile("\\<(.*?)\\>");
		Matcher m = pattern.matcher(s);
		String generic = "null";
		if(m.find()) {
			generic = m.group(1);
		}
		return generic;
	}

}

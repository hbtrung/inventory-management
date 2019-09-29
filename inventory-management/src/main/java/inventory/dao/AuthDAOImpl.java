package inventory.dao;

import java.util.List;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import inventory.model.Auth;

@Repository
@Transactional(rollbackFor=Exception.class)
public class AuthDAOImpl extends BaseDAOImpl<Auth> implements AuthDAO<Auth> {

	@Override
	public Auth find(int roleId, int menuId) {
		String queryString = "from Auth as model where model.role.id = :roleId and model.menu.id = :menuId";
		Query<Auth> query = sessionFactory.getCurrentSession().createQuery(queryString, Auth.class);
		query.setParameter("roleId", roleId);
		query.setParameter("menuId", menuId);
		List<Auth> auths = query.getResultList();
		if (!CollectionUtils.isEmpty(auths)) {
			return auths.get(0);
		}
		return null;
	}

}

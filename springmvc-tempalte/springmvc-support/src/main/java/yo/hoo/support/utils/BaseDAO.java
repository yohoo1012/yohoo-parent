package yo.hoo.support.utils;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import yo.hoo.support.utils.BaseDAO.DaoException.ExceptionId;

@SuppressWarnings("serial")
class BaseDAO<T> {

	public static class DaoException extends RuntimeException {

		public enum ExceptionId {
			新增时, 更新时, 保存时, 删除时, 查询时;
		}

		public DaoException(ExceptionId exceptionId, Throwable cause) {
			super("数据" + exceptionId + "异常", cause);
		}

	}

	private static final int PAGE_SIZE = 100;

	protected Class<T> entityClass;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	public BaseDAO() {
		entityClass = GenericsUtils.getSuperClassGenericType(getClass());
	}

	public void save(T t) {
		try {
			hibernateTemplate.save(t);
		} catch (Exception e) {
			throw new DaoException(ExceptionId.新增时, e);
		}
	}

	public void update(T t) {
		try {
			hibernateTemplate.update(t);
		} catch (Exception e) {
			throw new DaoException(ExceptionId.更新时, e);
		}
	}

	public void saveOrUpdate(T t) {
		try {
			hibernateTemplate.saveOrUpdate(t);
		} catch (Exception e) {
			throw new DaoException(ExceptionId.保存时, e);
		}
	}

	public void delete(T t) {
		try {
			hibernateTemplate.delete(t);
		} catch (Exception e) {
			throw new DaoException(ExceptionId.删除时, e);
		}
	}

	public void saveOrUpdateAll(Collection<T> entities) {
		try {
			hibernateTemplate.saveOrUpdateAll(entities);
		} catch (Exception e) {
			throw new DaoException(ExceptionId.保存时, e);
		}
	}

	public void deleteAll(Collection<T> entities) {
		try {
			hibernateTemplate.deleteAll(entities);
		} catch (Exception e) {
			throw new DaoException(ExceptionId.删除时, e);
		}
	}

	public T findById(Serializable id) {
		try {
			return hibernateTemplate.get(entityClass, id);
		} catch (Exception e) {
			throw new DaoException(ExceptionId.查询时, e);
		}
	}

	@SuppressWarnings("unchecked")
	protected List<T> find(String hql, Object... values) {
		try {
			return hibernateTemplate.find(hql, values);
		} catch (Exception e) {
			throw new DaoException(ExceptionId.查询时, e);
		}
	}

	protected List<?> findObjects(String hql, Object... values) {
		try {
			return hibernateTemplate.find(hql, values);
		} catch (Exception e) {
			throw new DaoException(ExceptionId.查询时, e);
		}
	}

	protected Page pagedQuery(String hql, int pageNo, Object... values) {
		try {
			String countHql = "select count(" + getQueryObject(hql) + ") ";
			countHql += removeSelect(removeOrders(hql));
			countHql = countHql.replace("fetch", "");
			return pagedQuery(hql, countHql, pageNo, PAGE_SIZE, values);
		} catch (Exception e) {
			throw new DaoException(ExceptionId.查询时, e);
		}
	}

	protected Page pagedQuery(String hql, int pageNo, int pageSize, Object... values) {
		try {
			String countHql = "select count(" + getQueryObject(hql) + ") ";
			countHql += removeSelect(removeOrders(hql));
			countHql = countHql.replace("fetch", "");
			return pagedQuery(hql, countHql, pageNo, pageSize, values);
		} catch (Exception e) {
			throw new DaoException(ExceptionId.查询时, e);
		}
	}

	@SuppressWarnings("rawtypes")
	protected Page pagedQuery(final String resultHql, String countHql, int pageNo, final int pageSize,
			final Object... values) {
		try {
			List listCount = find(countHql, values);
			int totalCount = 0;
			if (!StringUtils.isEmpty(listCount)) {
				totalCount = new Integer(listCount.get(0).toString());
			}
			if (totalCount < 1) {
				return new Page();
			}
			final int start = Page.getStartOfPage(pageNo, pageSize);
			List list = hibernateTemplate.executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createQuery(resultHql);
					if (values != null) {
						for (int i = 0; i < values.length; i++) {
							query.setParameter(i, values[i]);
						}
					}
					List list = query.setFirstResult(start).setMaxResults(pageSize).list();
					return list;
				}
			});
			return new Page(start, totalCount, pageSize, list);
		} catch (Exception e) {
			throw new DaoException(ExceptionId.查询时, e);
		}
	}

	private String getQueryObject(String hql) {
		if (hql.indexOf("distinct") >= 0) {
			int start = hql.indexOf("distinct");
			int end = hql.indexOf("from");
			return hql.substring(start, end);
		} else {
			return "*";
		}
	}

	private String removeOrders(String hql) {
		String regex = "order//s*by[//w|//W|//s|//S]*";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(hql);
		StringBuffer buf = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(buf, "");
		}
		matcher.appendTail(buf);
		return buf.toString();
	}

	private String removeSelect(String hql) {
		int beginIndex = hql.toLowerCase().indexOf("from");
		return hql.substring(beginIndex);
	}

	public static class Page {

		private int start;
		private int totalCount;
		private int pageSize;
		private List<?> list;

		public Page() {
			this.list = Arrays.asList();
		}

		public Page(int start, int totalCount, int pageSize, List<?> list) {
			this.start = start;
			this.totalCount = totalCount;
			this.pageSize = pageSize;
			this.list = list;
		}

		public static int getStartOfPage(int pageNo, int pageSize) {
			return (pageNo - 1) * pageSize;
		}

		public int getStart() {
			return start;
		}

		public void setStart(int start) {
			this.start = start;
		}

		public int getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public List<?> getList() {
			return list;
		}

		public void setList(List<?> list) {
			this.list = list;
		}

	}

}

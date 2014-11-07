package yo.hoo.core.component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import yo.hoo.core.component.BeanTable.BeanTableContainer;
import yo.hoo.support.widget.ThemeHoo;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class HqlTable extends Table {
	
	private HqlContainer container;

	public HqlTable() {
		setSizeFull();
		addStyleName(ThemeHoo.HOO_TABLE);
	}

	/**
	 * @see Table#setContainerDataSource(Container)
	 * @param container
	 */
	public void setHqlContainer(HqlContainer container) {
		this.container = container;
		setContainerDataSource(container);
		setVisibleColumns(container.getVisibleColumns());
	}

	/**
	 * {@link BeanTable#setBeanContainer(BeanTableContainer)}
	 */
	@Override
	@Deprecated
	public void setContainerDataSource(Container newDataSource) {
		super.setContainerDataSource(newDataSource);
	}

	@Override
	public HqlContainer getContainerDataSource() {
		return container;
	}
	
	public static class HqlContainer extends IndexedContainer {

		private static final int PAGE_SIZE = 50;
		private int page;
		private String hql;
		private String[] values;
		private Integer idIndex;
		private Type[] returnTypes;
		private String[] returnAliases;
		private HibernateTemplate template;
		private List<String> visibleColumns;

		public HqlContainer(String hql, String... values) {
			this.hql = hql;
			this.values = values;
			ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
			WebApplicationContext webApplicationContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			template = webApplicationContext.getBean(HibernateTemplate.class);
			buildContainerDataSource();
			askMoreItems();
		}

		public void askMoreItems() {
			template.execute(new HibernateCallback<Object>() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createQuery(hql);
					query.setFirstResult(page * PAGE_SIZE);
					query.setMaxResults(PAGE_SIZE);
					if (values != null) {
						for (int i = 0; i < values.length; i++) {
							query.setParameter(i, values[i]);
						}
					}
					List<?> list = query.list();
					for (Object o : list) {
						Object[] os = (Object[]) o;
						Object itemId;
						if (idIndex == null) {
							itemId = addItem();
						} else {
							itemId = os[idIndex];
							addItem(itemId);
						}
						if (containsId(itemId)) {
							for (int i = 0; i < returnAliases.length; i++) {
								Item item = getItem(itemId);
								@SuppressWarnings("unchecked")
								Property<Object> itemProperty = item.getItemProperty(returnAliases[i]);
								itemProperty.setValue(os[i]);
							}
						}
					}
					page++;
					return null;
				}
			});
		}

		private void buildContainerDataSource() {
			template.execute(new HibernateCallback<Object>() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createQuery(hql);
					returnTypes = query.getReturnTypes();
					returnAliases = query.getReturnAliases();
					return null;
				}
			});
			if (returnAliases != null) {
				visibleColumns = new ArrayList<String>();
				for (int i = 0; i < returnAliases.length; i++) {
					addContainerProperty(returnAliases[i], returnTypes[i].getReturnedClass(), null);
					if (!"ID".equalsIgnoreCase(returnAliases[i])) {
						visibleColumns.add(returnAliases[i]);
					} else {
						idIndex = i;
					}
				}
			}
		}

		public Object[] getVisibleColumns() {
			return Collections.unmodifiableList(visibleColumns).toArray();
		}

	}
}

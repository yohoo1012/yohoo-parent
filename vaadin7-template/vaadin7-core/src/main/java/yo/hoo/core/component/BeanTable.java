package yo.hoo.core.component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

import org.apache.commons.logging.LogFactory;

import yo.hoo.core.config.pojo.TableConfig;
import yo.hoo.core.statics.PropertyId;
import yo.hoo.core.statics.TableName;
import yo.hoo.core.utils.ReflectUtil;
import yo.hoo.support.utils.DateUtil;
import yo.hoo.support.utils.DateUtil.DateField;
import yo.hoo.support.widget.ThemeHoo;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class BeanTable extends Table {

	public interface BindingListener {

		void binding(Item item, Object bean);

	}

	private BeanTableContainer container;

	public BeanTable() {
		setSizeFull();
		addStyleName(ThemeHoo.HOO_TABLE);
	}

	/**
	 * @see Table#setContainerDataSource(Container)
	 * @param container
	 */
	public void setBeanContainer(BeanTableContainer container) {
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
	public BeanTableContainer getContainerDataSource() {
		return container;
	}

	public static class BeanTableContainer extends IndexedContainer implements BeanContainer, Comparator<TableConfig> {

		@Override
		public Item addBean(Object bean) {
			if (bean == null) {
				return null;
			}
			Object itemId = bean;
			if (bean.getClass().isAnnotationPresent(Entity.class)) {
				itemId = ReflectUtil.getId(bean);
			}
			Item item = addItem(itemId);
			bindingItem(item, bean);
			return item;
		}

		@Override
		public Item updateBean(Object bean) {
			if (bean == null) {
				return null;
			}
			Object itemId = bean;
			if (bean.getClass().isAnnotationPresent(Entity.class)) {
				itemId = ReflectUtil.getId(bean);
			}
			Item item = getItem(itemId);
			bindingItem(item, bean);
			return null;

		}

		@Override
		public boolean removeBean(Object bean) {
			if (bean == null) {
				return false;
			}
			Object itemId = bean;
			if (bean.getClass().isAnnotationPresent(Entity.class)) {
				itemId = ReflectUtil.getId(bean);
			}
			if (containsId(itemId)) {
				return removeItem(itemId);
			}
			return false;
		}

		@Override
		public Object getBean(Object itemId) {
			if (containsId(itemId)) {
				Item item = getItem(itemId);
				return item.getItemProperty(PropertyId.BEAN).getValue();
			}
			return null;
		}

		private BindingListener bindingListener;
		private TableName tableName;
		private List<TableConfig> visibleFields;
		private List<Object> visibleColumns = new ArrayList<Object>();

		public BeanTableContainer(TableName tableName) {
			this.tableName = tableName;
			setContainerProperty();
		}

		private void setContainerProperty() {
			try {
				addContainerProperty(PropertyId.BEAN, Object.class, null);
				for (TableConfig config : getVisibleFields()) {
					Class<?> cls = config.getTableName().getTableClass();
					String[] fields = config.getMethodName().split("\\.");
					for (int i = 0; i < fields.length; i++) {
						Method member = cls.getDeclaredMethod(fields[i]);
						Class<?> returnType = member.getReturnType();
						if (returnType.isAnnotationPresent(Entity.class)) {
							cls = returnType;
						} else {
							if (returnType == Date.class) {
								returnType = String.class;
							}
							addContainerProperty(config.getColumnName(), ReflectUtil.getWrapClass(returnType),
									null);
							visibleColumns.add(config.getColumnName());
						}
					}
				}
			} catch (Exception e) {
				LogFactory.getLog(getClass()).error(null, e);
				throw new RuntimeException(e);
			}
		}

		private List<TableConfig> getVisibleFields() {
			if (visibleFields == null) {
				visibleFields = tableName.getConfigs();
				Collections.sort(visibleFields, this);
			}
			return visibleFields;
		}

		@SuppressWarnings("unchecked")
		private void bindingItem(Item item, Object bean) {
			if (item != null) {
				item.getItemProperty(PropertyId.BEAN).setValue(bean);
				for (TableConfig config : getVisibleFields()) {
					Class<?> cls = config.getTableName().getTableClass();
					Object obj = bean;
					String[] fields = config.getMethodName().split("\\.");
					for (int i = 0; i < fields.length; i++) {
						if (obj == null) {
							break;
						}
						try {
							Method member = cls.getDeclaredMethod(fields[i]);
							Class<?> returnType = member.getReturnType();
							if (returnType.isAnnotationPresent(Entity.class)) {
								cls = returnType;
								obj = member.invoke(obj);
							} else {
								Object fieldValue = member.invoke(obj);
								if (returnType == Date.class) {
									fieldValue = DateUtil.format((Date) fieldValue, DateField.DAY_OF_MONTH);
//								} else if (member.isAnnotationPresent(CodeMapper.class)) {
//									fieldValue = member.getAnnotation(CodeMapper.class).value();
								}
								item.getItemProperty(config.getColumnName()).setValue(fieldValue);
							}
						} catch (Exception e) {
							LogFactory.getLog(getClass()).error(null, e);
							throw new RuntimeException(e);
						}
					}
				}
				if (getBindingListener() != null) {
					getBindingListener().binding(item, bean);
				}
			}
		}

		@Override
		public int compare(TableConfig o1, TableConfig o2) {
			return o1.getSortId() - o2.getSortId();
		}

		public BindingListener getBindingListener() {
			return bindingListener;
		}

		public void setBindingListener(BindingListener bindingListener) {
			this.bindingListener = bindingListener;
		}

		@Override
		public boolean setParent(Object itemId, Object newParentId) {
			throw new UnsupportedOperationException();
		}

		public Object[] getVisibleColumns() {
			return Collections.unmodifiableList(visibleColumns).toArray();
		}

	}

}

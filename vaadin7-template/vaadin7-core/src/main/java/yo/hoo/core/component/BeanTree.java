package yo.hoo.core.component;

import javax.persistence.Entity;

import yo.hoo.core.statics.PropertyId;
import yo.hoo.core.utils.ReflectUtil;
import yo.hoo.support.widget.ThemeHoo;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;

@SuppressWarnings("serial")
public class BeanTree extends Tree {

	private BeanTreeContainer container;

	public BeanTree() {
		addStyleName(ThemeHoo.TREE_CONNECTORS);
	}

	public void setBeanContainer(BeanTreeContainer container) {
		this.container = container;
		setContainerDataSource(container);
	}

	@Override
	@Deprecated
	public void setContainerDataSource(Container newDataSource) {
		super.setContainerDataSource(newDataSource);
	}

	@Override
	public BeanTreeContainer getContainerDataSource() {
		return container;
	}

	public static class BeanTreeContainer extends HierarchicalContainer implements BeanContainer {

		public BeanTreeContainer() {
			addContainerProperty(PropertyId.BEAN, Object.class, null);
			addContainerProperty(PropertyId.SORTID, String.class, null);
			addContainerProperty(PropertyId.CAPTION, String.class, null);
		}

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

		@SuppressWarnings("unchecked")
		private void bindingItem(Item item, Object bean) {
			if (item != null) {
				item.getItemProperty(PropertyId.BEAN).setValue(bean);
				item.getItemProperty(PropertyId.SORTID).setValue(ReflectUtil.getSortId(bean));
				item.getItemProperty(PropertyId.CAPTION).setValue(ReflectUtil.getCaption(bean));
			}
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

	}
}

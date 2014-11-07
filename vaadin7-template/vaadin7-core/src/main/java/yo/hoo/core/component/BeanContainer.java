package yo.hoo.core.component;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

public interface BeanContainer extends Container {

	public Object getBean(Object itemId);

	public Item addBean(Object bean);

	public Item updateBean(Object bean);

	public boolean removeBean(Object bean);

	public boolean setParent(Object itemId, Object newParentId);
}

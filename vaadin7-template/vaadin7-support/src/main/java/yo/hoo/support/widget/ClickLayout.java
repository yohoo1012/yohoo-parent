package yo.hoo.support.widget;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ClickLayout extends VerticalLayout {
	
    public ClickLayout() {
        setWidth("100%");
        addStyleName("v-clicklayout");
    }

    public ClickLayout(Component... children) {
        this();
        addComponents(children);
    }

}

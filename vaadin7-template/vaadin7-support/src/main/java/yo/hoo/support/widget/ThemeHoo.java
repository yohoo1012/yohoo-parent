package yo.hoo.support.widget;

import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ChameleonTheme;

public class ThemeHoo extends ChameleonTheme {
	
	public static final String HOO_BIG = "hoo-big";
	public static final String HOO_BIGGER = "hoo-bigger";

	public static final String HSP_NAME = "hsp-name";
	public static final String MENU_HEADER = "header-menubar";
	public static final String MENU_NAVIGATOR = "navigator-menubar";
	public static final String LAYOUT_CAPTION = "caption-layout";
	public static final String LAYOUT_PATIENT_LIST = "patient-list-layout";
	public static final String HOO_TEXTFIELD_BIG = "hoo-big-textfield";
	public static final String HOO_SELECT_BIG = "hoo-big-filterselect";
	public static final String HOO_TABLE_EDIT = "hoo-edit-table";
	public static final String HOO_TABLE = "hoo-table";
	public static final String HOO_LABEL_POSITION = "hoo-position-label";
	public static final String HOO_LABEL_CAPTION = "hoo-caption-label";

	public static void registerTheme(Component component){
		component.addStyleName(component.getClass().getSimpleName());
	}

}

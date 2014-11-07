package yo.hoo.core.component.util;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

public class ButtonFactory {

	public interface IButtonId {
		String getCaption();
	}

	public static Button createButton(IButtonId buttonId) {
		Button button = new Button();
		button.setData(buttonId);
		button.setCaption(buttonId.getCaption());
		return button;
	}

	public static Button createButton(IButtonId buttonId, ClickListener listener) {
		Button button = createButton(buttonId);
		button.addClickListener(listener);
		return button;
	}
}
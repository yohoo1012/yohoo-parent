package yo.hoo.support.widget;

import java.io.Serializable;
import java.lang.reflect.Method;

import yo.hoo.support.widget.client.HoverButtonServerRpc;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;

@SuppressWarnings("serial")
public class HoverButton extends Button {

	public class ButtonOutEvent extends HoverEvent {

		public ButtonOutEvent(HoverButton hoverButton, MouseEventDetails details) {
			super(hoverButton, details);
		}

	}

	public class ButtonOverEvent extends HoverEvent {

		public ButtonOverEvent(HoverButton hoverButton,
				MouseEventDetails details) {
			super(hoverButton, details);
		}

	}

	private HoverButtonServerRpc rpc = new HoverButtonServerRpc() {

		@Override
		public void mouseOver(MouseEventDetails details) {
			fireMouseOver(details);
		}

		@Override
		public void mouseOut(MouseEventDetails details) {
			fireMouseOut(details);
		}

	};

	public HoverButton() {
		super();
		registerRpc(rpc);
	}

	public HoverButton(String caption) {
		super(caption);
		registerRpc(rpc);
	}

	public HoverButton(String caption, ClickListener clickListener) {
		super(caption, clickListener);
		registerRpc(rpc);
	}

	public void fireMouseOver(MouseEventDetails details) {
		fireEvent(new ButtonOverEvent(this, details));
	}

	public void fireMouseOut(MouseEventDetails details) {
		fireEvent(new ButtonOutEvent(this, details));
	}

	public interface ButtonHoverListener extends Serializable {
		public static final Method BUTTON_OVER_METHOD = ReflectTools
				.findMethod(ButtonHoverListener.class, "buttonHover",
						ButtonOverEvent.class);
		public static final Method BUTTON_OUT_METHOD = ReflectTools
				.findMethod(ButtonHoverListener.class, "buttonHover",
						ButtonOutEvent.class);

		public void buttonHover(ButtonOverEvent event);

		public void buttonHover(ButtonOutEvent event);

	}

	public void addHoverListener(ButtonHoverListener listener) {
		addListener(ButtonOverEvent.class, listener,
				ButtonHoverListener.BUTTON_OVER_METHOD);
		addListener(ButtonOutEvent.class, listener,
				ButtonHoverListener.BUTTON_OUT_METHOD);
	}

	public void removeHoverListener(ButtonHoverListener listener) {
		removeListener(ButtonOverEvent.class, listener,
				ButtonHoverListener.BUTTON_OVER_METHOD);
		removeListener(ButtonOutEvent.class, listener,
				ButtonHoverListener.BUTTON_OUT_METHOD);
	}

	public static class HoverEvent extends Component.Event {

		private final MouseEventDetails details;

		public HoverEvent(Component source) {
			super(source);
			details = null;
		}

		public HoverEvent(Component source, MouseEventDetails details) {
			super(source);
			this.details = details;
		}

		public HoverButton getButton() {
			return (HoverButton) getSource();
		}

		public int getClientX() {
			if (null != details) {
				return details.getClientX();
			} else {
				return -1;
			}
		}

		public int getClientY() {
			if (null != details) {
				return details.getClientY();
			} else {
				return -1;
			}
		}

		public int getRelativeX() {
			if (null != details) {
				return details.getRelativeX();
			} else {
				return -1;
			}
		}

		public int getRelativeY() {
			if (null != details) {
				return details.getRelativeY();
			} else {
				return -1;
			}
		}
	}

}

package yo.hoo.support.widget.client;

import yo.hoo.support.widget.HoverButton;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.Connect.LoadStyle;

@SuppressWarnings("serial")
@Connect(value = HoverButton.class, loadStyle = LoadStyle.EAGER)
public class HoverButtonConnector extends ButtonConnector implements
		MouseOverHandler, MouseOutHandler {

	@Override
	public void init() {
		super.init();
		getWidget().addMouseOverHandler(this);
		getWidget().addMouseOutHandler(this);
	}
	
	@Override
	public void onMouseOut(MouseOutEvent event) {
        MouseEventDetails details = MouseEventDetailsBuilder
                .buildMouseEventDetails(event.getNativeEvent(), getWidget()
                        .getElement());
		getRpcProxy(HoverButtonServerRpc.class).mouseOut(details);
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
        MouseEventDetails details = MouseEventDetailsBuilder
                .buildMouseEventDetails(event.getNativeEvent(), getWidget()
                        .getElement());
		getRpcProxy(HoverButtonServerRpc.class).mouseOver(details);
	}
}

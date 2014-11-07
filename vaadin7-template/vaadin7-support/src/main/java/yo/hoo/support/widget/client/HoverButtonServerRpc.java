package yo.hoo.support.widget.client;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;


public interface HoverButtonServerRpc extends ServerRpc {

	void mouseOver(MouseEventDetails details);

	void mouseOut(MouseEventDetails details);

}
package yo.hoo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.jouni.animator.AnimatorProxy;
import org.vaadin.jouni.animator.AnimatorProxy.AnimationEvent;
import org.vaadin.jouni.animator.AnimatorProxy.AnimationListener;
import org.vaadin.jouni.animator.shared.AnimType;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "yo.hoo.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);
        
        Button button = new Button("Click Me1");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                Label label = new Label("Thank you for clicking");
//                Animator.animate(label, new Css().setProperty("display","none")).ease(Ease.OUT).duration(2000);
				layout.addComponent(label);
            }
        });
        layout.addComponent(button);
        
        button = new Button("Click Me2");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                Label label = new Label("Thank you for clicking");
            	layout.addComponent(label);
            	AnimatorProxy proxy = new AnimatorProxy();
            	layout.addComponent(proxy);
            	proxy.animate(label, AnimType.FADE_IN).setDuration(500).setDelay(100);
            	proxy.addListener(new AnimationListener() {
            	  public void onAnimation(AnimationEvent event) {
            	    System.out.println(event.getAnimation());
            	  }
            	});
            }
        });
        layout.addComponent(button);
    }

}

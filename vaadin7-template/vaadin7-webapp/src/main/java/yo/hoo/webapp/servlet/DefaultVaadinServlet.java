package yo.hoo.webapp.servlet;

import java.util.Date;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import yo.hoo.support.utils.DateUtil;
import yo.hoo.support.utils.DateUtil.DateField;
import yo.hoo.webapp.servlet.DefaultVaadinServlet.DefaultUI;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@VaadinServletConfiguration(ui = DefaultUI.class, widgetset = "yo.hoo.webapp.WidgetSet", productionMode = false, resourceCacheTime = 0)
public class DefaultVaadinServlet extends VaadinServlet {

	private static final String WEB_APP_NAME = "菲特区域医疗信息平台";
	private static final String BASE_PACKAGE_NAME = "com.afirstech.rhio";

	public static class DefaultUI extends UI {

		@Override
		protected void init(VaadinRequest request) {
			getPage().setTitle(WEB_APP_NAME);
		}
	}

	private static final Log log = LogFactory.getLog(DefaultVaadinServlet.class);

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		getService().addSessionInitListener(new SessionInitListener() {
			@Override
			public void sessionInit(SessionInitEvent event) throws ServiceException {
				event.getSession().addUIProvider(new UIProvider() {
					@Override
					public UI createInstance(UICreateEvent event) {
						Class<? extends UI> uiClass = event.getUIClass();
						try {
							return uiClass.newInstance();
						} catch (Exception e) {
							log.error(e, e);
							throw new RuntimeException("初始化" + uiClass.getSimpleName() + "失败");
						}
					}

					@Override
					@SuppressWarnings("unchecked")
					public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
						try {
							String path = event.getRequest().getPathInfo();
							if (path.length() > 1) {
								if (path.endsWith("/")) {
									path = path.substring(1, path.length() - 1);
								} else {
									path = path.substring(1);
								}
								path = path.replaceAll("/", ".");
								if (path.indexOf(BASE_PACKAGE_NAME) == -1) {
									path = BASE_PACKAGE_NAME + "." + path;
								}
							}
							return (Class<? extends UI>) Class.forName(path);
						} catch (Exception e) {
						}
						return null;
					}
				});
			}
		});
		getService().addSessionDestroyListener(new SessionDestroyListener() {
			@Override
			public void sessionDestroy(SessionDestroyEvent event) {
				log.info(DateUtil.format(new Date(), DateField.SECOND) + " session destroy!");
			}
		});
		getService().setSystemMessagesProvider(new ApplicationMessagesProvider());
	}

	public class ApplicationMessagesProvider implements SystemMessagesProvider {
		@Override
		public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
			final CustomizedSystemMessages customizedSystemMessages = new CustomizedSystemMessages();
			customizedSystemMessages.setSessionExpiredNotificationEnabled(false);
			customizedSystemMessages.setOutOfSyncNotificationEnabled(false);
			customizedSystemMessages.setAuthenticationErrorNotificationEnabled(false);
			customizedSystemMessages.setCommunicationErrorNotificationEnabled(false);
			customizedSystemMessages.setCookiesDisabledNotificationEnabled(false);
			return customizedSystemMessages;
		}
	}

}

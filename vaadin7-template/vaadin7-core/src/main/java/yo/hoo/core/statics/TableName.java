package yo.hoo.core.statics;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import yo.hoo.core.config.pojo.TableConfig;
import yo.hoo.core.config.service.TableConfigService;

import com.vaadin.server.VaadinServlet;

public enum TableName {

	;

	private Class<?> tableClass;

	private TableName(Class<?> tableClass) {
		this.tableClass = tableClass;
	}

	public Class<?> getTableClass() {
		return tableClass;
	}

	private boolean dirty;
	private List<TableConfig> configs;

	public List<TableConfig> getConfigs() {
		if (configs == null || dirty) {
			ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(
					servletContext);
			TableConfigService tableConfigService = webApplicationContext.getBean(TableConfigService.class);
			configs = tableConfigService.list(this);
			dirty = !dirty;
		}
		return configs;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

}

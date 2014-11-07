package yo.hoo.core.config.service;

import java.util.List;

import org.springframework.stereotype.Service;

import yo.hoo.core.config.pojo.TableConfig;
import yo.hoo.core.statics.TableName;
import yo.hoo.core.utils.BaseDAO;

@Service
public class TableConfigService extends BaseDAO<TableConfig> {

	public List<TableConfig> list(TableName tableName) {
		return find("from TableConfig c where c.tableName=?", tableName);
	}

}

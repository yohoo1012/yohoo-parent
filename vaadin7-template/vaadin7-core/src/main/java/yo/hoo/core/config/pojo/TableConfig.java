package yo.hoo.core.config.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import yo.hoo.core.statics.TableName;

/**
 * 表格配置信息
 * 
 * @author h
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "T_BASE_TABLE_CONFIG")
public class TableConfig implements Serializable {

	@Id
	@Column(length = 32)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;// 主键

	@Column(name = "methodName", length = 20)
	private String methodName;

	@Column(name = "columnName", length = 20)
	private String columnName;

	@Enumerated(EnumType.STRING)
	@Column(name = "table_name", length = 32)
	private TableName tableName;

	@Column(name = "sort_id")
	private int sortId;

	@Version
	private int version;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public TableName getTableName() {
		return tableName;
	}

	public void setTableName(TableName tableName) {
		this.tableName = tableName;
	}

	public int getSortId() {
		return sortId;
	}

	public void setSortId(int sortId) {
		this.sortId = sortId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}

package com.bms.entity;

/**
 * @author YeChunBo
 * @time 2017��7��25��
 *
 *   ��˵��
	 * ���²���
	 * policeName
	 * id
	 * dbName
	 * tableName Ĭ��ӵ�����б�
	 * permissionsType ��Ĳ���Ȩ�ޣ�eg:select,update...
	 * policeUser
	 * colPermissionsType �еĲ���Ȩ�ޣ�Ĭ��ӵ������Ȩ��
	 * policeIsEnabled �ò����Ƿ���Ч��Ĭ����Ч��1 ��Ч��0 ��Ч
 */

public class UpdatePoliceReq {
	
	private String policeName;
	private String policeId;
	private String dbName;
	private String tableName = "*";
	private String permissionsType;
	private String policeUser;
	private String colPermissionsType = "*";
	private String policeIsEnabled = "1";
	private String service;
	// hfds �������
	private String hdfsResourcePath;

	// hbase �������
	private String hbaseTableName;
	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getHdfsResourcePath() {
		return hdfsResourcePath;
	}

	public void setHdfsResourcePath(String hdfsResourcePath) {
		this.hdfsResourcePath = hdfsResourcePath;
	}

	public String getPoliceName() {
		return policeName;
	}

	public void setPoliceName(String policeName) {
		this.policeName = policeName;
	}

	public String getPoliceId() {
		return policeId;
	}

	public void setPoliceId(String policeId) {
		this.policeId = policeId;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPermissionsType() {
		return permissionsType;
	}

	public void setPermissionsType(String permissionsType) {
		this.permissionsType = permissionsType;
	}

	public String getPoliceUser() {
		return policeUser;
	}

	public void setPoliceUser(String policeUser) {
		this.policeUser = policeUser;
	}

	public String getColPermissionsType() {
		return colPermissionsType;
	}

	public void setColPermissionsType(String colPermissionsType) {
		this.colPermissionsType = colPermissionsType;
	}

	public String getPoliceIsEnabled() {
		return policeIsEnabled;
	}

	public void setPoliceIsEnabled(String policeIsEnabled) {
		this.policeIsEnabled = policeIsEnabled;
	}

	public String getHbaseTableName() {
		return hbaseTableName;
	}

	public void setHbaseTableName(String hbaseTableName) {
		this.hbaseTableName = hbaseTableName;
	}

	@Override
	public String toString() {
		return "UpdatePoliceReq [policeName=" + policeName + ", policeId=" + policeId + ", dbName=" + dbName
				+ ", tableName=" + tableName + ", permissionsType=" + permissionsType + ", policeUser=" + policeUser
				+ ", colPermissionsType=" + colPermissionsType + ", policeIsEnabled=" + policeIsEnabled + ", service="
				+ service + ", hdfsResourcePath=" + hdfsResourcePath + ", hbaseTableName=" + hbaseTableName + "]";
	}

}

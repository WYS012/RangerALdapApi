package com.bms.entity;

/**
 * @author YeChunBo
 * @time 2017��7��25��
 *
 * ��˵��
 * 
	 * ��������
	 * policeUser ���Զ�Ӧ���û�
	 * dbName :���ݿ⣬������ݿ��ö��ŷָ�
	 * tableName ��������ö��ŷָ�, Ĭ��ӵ�в�����Ӧ���ݿ������б�
	 * permissionsType ������Ӧ��Ȩ�ޣ�����ö��ŷָ�, Ĭ��Ϊӵ������Ȩ��, ֵΪ all
	 * colPermissionsType = "*"; // Ĭ���������ж����Է���
 */

public class CreatePoliceReq {

	private String policeUser;
	private String dbName;
	private String tableName = "*";
	private String permissionsType = "all";
	private String colPermissionsType = "*"; // Ĭ���������ж����Է���

	public String getPoliceUser() {
		return policeUser;
	}

	public void setPoliceUser(String policeUser) {
		this.policeUser = policeUser;
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

	public String getColPermissionsType() {
		return colPermissionsType;
	}

	public void setColPermissionsType(String colPermissionsType) {
		this.colPermissionsType = colPermissionsType;
	}

	@Override
	public String toString() {
		return "CreatePoliceReq [policeUser=" + policeUser + ", dbName=" + dbName + ", tableName=" + tableName
				+ ", permissionsType=" + permissionsType + ", colPermissionsType=" + colPermissionsType + "]";
	}
	
}

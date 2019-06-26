package com.bms.service.rangerimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ranger.plugin.model.RangerPolicy;
import org.jboss.logging.Logger;

import com.bms.entity.UpdatePoliceReq;
import com.bms.utils.PropertyUtil;

/**
 * @author YeChunBo
 * @time 2017��7��25��
 *
 *       ��˵�� :Ranger �ӿڸ�����
 */

public class SupportRangerImpl {

	private static String service = PropertyUtil.getProperty("service");// hive
																		// �ķ�����
	private static String hdfsService = PropertyUtil.getProperty("hdfsService");// hdfs
																				// �ķ�����
	private static String hbaseService = PropertyUtil.getProperty("hbaseService");// hdfs
																					// �ķ�����
	private static Logger log = Logger.getLogger(SupportRangerImpl.class);

	/**
	 * ��װ���²��ԵĶ���
	 * 
	 * @param policeName
	 * @param resourceName
	 *            ��������Ӧ����Դ���ƣ�hive��ӦdbName,hdfs��ӦpathName,hbase��ӦhbaseTableName
	 * @param operatePermissionsType
	 *            ����Ȩ�ޣ�eg:hive��Ӧselect,update,create.. hdfs��Ӧ��write,read..
	 *            hbase��Ӧ write,read,create
	 * @param policeUser
	 *            ���Զ�Ӧ���û�
	 * @param colPermissionsType
	 *            hive��hbase�ж�Ӧ��Ȩ��
	 * @param policeIsEnabled
	 *            �������Ƿ���ã�1���Ϊ����
	 * @param hiveTableName
	 * @param policyType
	 *            ��������Ӧ�����ͣ�eg:hive,hbase,hdfs
	 * @return
	 */
	public static RangerPolicy updateOfPolicy(String policeName, String resourceName, String operatePermissionsType,
			String policeUser, String colPermissionsType, String policeIsEnabled, String hiveTableName,
			String policyType) {

		log.info("UpdateOfPolicy of the info policeName=" + policeName + ",resourceName=" + resourceName + ",policeUser=" + policeUser);
		RangerPolicy rangerPolicy = new RangerPolicy();
		Map<String, RangerPolicy.RangerPolicyResource> resources = new HashMap<String, RangerPolicy.RangerPolicyResource>();

		if (StringUtils.isNotBlank(policeName))
			rangerPolicy.setName(policeName);

		if (StringUtils.isBlank(policeIsEnabled) || "1".equals(policeIsEnabled))
			rangerPolicy.setIsEnabled(true);
		else if ("0".equals(policeIsEnabled))
			rangerPolicy.setIsEnabled(false);

		if ("hdfs".equalsIgnoreCase(policyType)) {
			rangerPolicy.setService(hdfsService);
			RangerPolicy.RangerPolicyResource hdfsPathRangerPolicyResource = new RangerPolicy.RangerPolicyResource();

			ArrayList<String> pathList = new ArrayList<String>();

			if (resourceName.contains(",")) {
				String[] pathArr = resourceName.split(",");
				for (String pathNameS : pathArr) {
					pathList.add(pathNameS);
				}
			} else {
				pathList.add(resourceName);
			}

			pathList.add("/" + policeUser.replace(",", "_") + "_autoCreateFilePath/");// Ĭ��Ϊÿ���������һ��Ψһ��·���������ִ�����Ȩ����ͬ�Ĳ���
			hdfsPathRangerPolicyResource.setValues(pathList);

			hdfsPathRangerPolicyResource.setIsExcludes(false);
			hdfsPathRangerPolicyResource.setIsRecursive(false);

			resources.put("path", hdfsPathRangerPolicyResource);

		} else if ("hbase".equalsIgnoreCase(policyType)) {
			rangerPolicy.setService(hbaseService);
			RangerPolicy.RangerPolicyResource hbaseTableRangerPolicyResource = new RangerPolicy.RangerPolicyResource();
			RangerPolicy.RangerPolicyResource columRangerPolicyResource = new RangerPolicy.RangerPolicyResource();
			RangerPolicy.RangerPolicyResource columFamPolicyResource = new RangerPolicy.RangerPolicyResource();

			ArrayList<String> hbaseTableList = new ArrayList<String>();

			if (resourceName.contains(",")) {
				String[] hbaseTableArr = resourceName.split(",");
				for (String hbaseTableS : hbaseTableArr) {
					hbaseTableList.add(hbaseTableS);
				}
			} else {
				hbaseTableList.add(resourceName);
			}

			hbaseTableList.add(policeUser.replace(",", "_") + "_autoCreateTable");// Ĭ��Ϊÿ���������һ��Ψһ��·���������ִ�����Ȩ����ͬ�Ĳ���
			hbaseTableRangerPolicyResource.setValues(hbaseTableList);

			hbaseTableRangerPolicyResource.setIsExcludes(false);
			hbaseTableRangerPolicyResource.setIsRecursive(false);

			if (StringUtils.isBlank(colPermissionsType))
				columRangerPolicyResource.setValue("*");
			else
				columRangerPolicyResource.setValue(colPermissionsType);

			columRangerPolicyResource.setIsExcludes(false);
			columRangerPolicyResource.setIsRecursive(false);

			columFamPolicyResource.setValue("*");
			columFamPolicyResource.setIsExcludes(false);
			columFamPolicyResource.setIsRecursive(false);

			resources.put("column", columRangerPolicyResource);
			resources.put("column-family", columFamPolicyResource);
			resources.put("table", hbaseTableRangerPolicyResource);

		} else {// Ĭ��ΪHive
			rangerPolicy.setService(service);
			RangerPolicy.RangerPolicyResource dbRangerPolicyResource = new RangerPolicy.RangerPolicyResource();
			RangerPolicy.RangerPolicyResource tablerRangerPolicyResource = new RangerPolicy.RangerPolicyResource();
			RangerPolicy.RangerPolicyResource columRangerPolicyResource = new RangerPolicy.RangerPolicyResource();

			ArrayList<String> dbList = new ArrayList<String>();

			if (resourceName.contains(",")) {
				String[] dbArr = resourceName.split(",");
				for (String dbNameS : dbArr) {
					dbList.add(dbNameS);
				}
			} else {
				dbList.add(resourceName);
			}

			dbList.add(policeUser.replace(",", "_") + "_autoCreateDb");// Ĭ��Ϊÿ���������һ��Ψһ�Ŀ⣬�����ִ�����Ȩ����ͬ�Ĳ���
			dbRangerPolicyResource.setValues(dbList);

			ArrayList<String> tableList = new ArrayList<String>();

			if (hiveTableName.contains(",")) {
				String[] tableArr = hiveTableName.split(",");
				for (String tableNameS : tableArr) {
					tableList.add(tableNameS);
				}
			} else {
				tableList.add(hiveTableName);
			}

			tablerRangerPolicyResource.setValues(tableList);

			dbRangerPolicyResource.setIsExcludes(false);
			dbRangerPolicyResource.setIsRecursive(false);

			if (StringUtils.isBlank(colPermissionsType))
				columRangerPolicyResource.setValue("*");
			else
				columRangerPolicyResource.setValue(colPermissionsType);

			resources.put("database", dbRangerPolicyResource);
			resources.put("table", tablerRangerPolicyResource);
			resources.put("column", columRangerPolicyResource);
		}

		rangerPolicy.setIsAuditEnabled(true);

		List<RangerPolicy.RangerPolicyItem> policyItems = new ArrayList<RangerPolicy.RangerPolicyItem>();

		RangerPolicy.RangerPolicyItem rangerPolicyItem = new RangerPolicy.RangerPolicyItem();
		List<String> users = new ArrayList<String>();
		if (StringUtils.isNotBlank(policeUser)) {
			String[] policeUserArr = policeUser.split("\\,");
			if (policeUserArr.length > 0) {
				for (int i = 0; i < policeUserArr.length; i++) {
					users.add(policeUserArr[i]);
				}
			}
			rangerPolicyItem.setUsers(users);
		}

		List<RangerPolicy.RangerPolicyItemAccess> rangerPolicyItemAccesses = new ArrayList<RangerPolicy.RangerPolicyItemAccess>();

		if (StringUtils.isNotBlank(operatePermissionsType)) {
			String[] operatePermArr = operatePermissionsType.split("\\,");
			RangerPolicy.RangerPolicyItemAccess rangerPolicyItemAccess;
			if (operatePermArr.length > 0) {
				for (int i = 0; i < operatePermArr.length; i++) {
					rangerPolicyItemAccess = new RangerPolicy.RangerPolicyItemAccess();
					rangerPolicyItemAccess.setType(operatePermArr[i]);
					rangerPolicyItemAccess.setIsAllowed(Boolean.TRUE);
					rangerPolicyItemAccesses.add(rangerPolicyItemAccess);
				}
			}
		}

		rangerPolicyItem.setAccesses(rangerPolicyItemAccesses);

		policyItems.add(rangerPolicyItem);

		rangerPolicy.setPolicyItems(policyItems);
		rangerPolicy.setResources(resources);
		return rangerPolicy;
	}

	/**
	 * Ϊ�������Զ������Ĳ��Զ���
	 * 
	 * @param PoliceName
	 * @param policeUser
	 * @param dbName
	 * @param tableName
	 * @param operatePermissionsType
	 * @return
	 */
	public static RangerPolicy createOfPolicy(String PoliceName, String policeUser, String dbName, String tableName,
			String operatePermissionsType) {

		RangerPolicy rangerPolicy = new RangerPolicy();
		rangerPolicy.setService(service);
		rangerPolicy.setName(PoliceName);
		rangerPolicy.setIsAuditEnabled(true);

		Map<String, RangerPolicy.RangerPolicyResource> resources = new HashMap<String, RangerPolicy.RangerPolicyResource>();

		RangerPolicy.RangerPolicyResource dbRangerPolicyResource = new RangerPolicy.RangerPolicyResource();
		RangerPolicy.RangerPolicyResource tablerRangerPolicyResource = new RangerPolicy.RangerPolicyResource();
		RangerPolicy.RangerPolicyResource columRangerPolicyResource = new RangerPolicy.RangerPolicyResource();

		String newPoliceUser = policeUser;
		if (policeUser.contains(",")) {
			newPoliceUser = policeUser.replace(",", "_");
		}

		ArrayList<String> dbList = new ArrayList<String>();

		if (dbName.contains(",")) {
			String[] dbArr = dbName.split(",");
			for (String dbNameS : dbArr) {
				dbList.add(dbNameS);
			}
		} else {
			dbList.add(dbName);
		}

		dbList.add(newPoliceUser + "_autoCreateDb");// Ĭ��Ϊÿ���������һ��Ψһ�Ŀ⣬�����ִ�����Ȩ����ͬ�Ĳ���
		dbRangerPolicyResource.setValues(dbList);

		ArrayList<String> tableList = new ArrayList<String>();

		if (tableName.contains(",")) {
			String[] tableArr = tableName.split(",");
			for (String tableNameS : tableArr) {
				tableList.add(tableNameS);
			}
		} else {
			tableList.add(tableName);
		}

		tablerRangerPolicyResource.setValues(tableList);
		columRangerPolicyResource.setValue("*");
		resources.put("database", dbRangerPolicyResource);
		resources.put("table", tablerRangerPolicyResource);
		resources.put("column", columRangerPolicyResource);

		List<RangerPolicy.RangerPolicyItem> policyItems = new ArrayList<RangerPolicy.RangerPolicyItem>();

		RangerPolicy.RangerPolicyItem rangerPolicyItem = new RangerPolicy.RangerPolicyItem();
		List<String> users = new ArrayList<String>();
		String[] policeUserArr = policeUser.split("\\,");
		if (policeUserArr.length > 0) {
			for (int i = 0; i < policeUserArr.length; i++) {
				users.add(policeUserArr[i]);
			}
		}
		rangerPolicyItem.setUsers(users);

		List<RangerPolicy.RangerPolicyItemAccess> rangerPolicyItemAccesses = new ArrayList<RangerPolicy.RangerPolicyItemAccess>();

		String[] operatePermArr = operatePermissionsType.split("\\,");
		RangerPolicy.RangerPolicyItemAccess rangerPolicyItemAccess;
		if (operatePermArr.length > 0) {
			for (int i = 0; i < operatePermArr.length; i++) {
				rangerPolicyItemAccess = new RangerPolicy.RangerPolicyItemAccess();
				rangerPolicyItemAccess.setType(operatePermArr[i]);
				rangerPolicyItemAccess.setIsAllowed(Boolean.TRUE);
				rangerPolicyItemAccesses.add(rangerPolicyItemAccess);
			}
		}

		rangerPolicyItem.setAccesses(rangerPolicyItemAccesses);

		policyItems.add(rangerPolicyItem);

		rangerPolicy.setPolicyItems(policyItems);
		rangerPolicy.setResources(resources);
		return rangerPolicy;
	}

	/**
	 * ����Ȩ�ޣ������д��Ȩ������ȥ����
	 * @param oldPoliceReq
	 * @param updatePoliceReq
	 * @param permissionsType
	 */
	public static void handlePermissionsType(UpdatePoliceReq oldPoliceReq, UpdatePoliceReq updatePoliceReq,
			String permissionsType) {
		// ���ӵ��д��Ȩ����ȥ��дȨ��
		if (StringUtils.isNotBlank(permissionsType))
			updatePoliceReq.setPermissionsType(permissionsType); // ����ǿ���˵�����ýӿ�ʱ�д���Ҫ���õ�Ȩ�ޣ���ֱ�ӽ���Ȩ�޵�����
		else {
			String permissionsTypeStr = oldPoliceReq.getPermissionsType();
			StringBuffer permissBuf = new StringBuffer();
			if (StringUtils.isNotBlank(permissionsTypeStr)) {
				if (permissionsTypeStr.contains(",")) { // ӵ�ж��Ȩ����д��Ȩ��ȥ��
					String[] permisArr = permissionsTypeStr.split(",");

					for (int i = 0; i < permisArr.length; i++) {
						if (!("create".equalsIgnoreCase(permisArr[i]) || "write".equalsIgnoreCase(permisArr[i])))
							permissBuf.append(permisArr[i]).append(",");
					}

					updatePoliceReq.setPermissionsType(permissBuf.toString().substring(0, permissBuf.lastIndexOf(",")));

				} else { // ֻ��һ��Ȩ����Ϊд��Ȩ���������ΪselectȨ��
					if ("create".equalsIgnoreCase(permissionsTypeStr)) {
						permissBuf.append("select");
					} else if ("write".equalsIgnoreCase(permissionsTypeStr)) {
						permissBuf.append("read");
					} else {
						permissBuf.append(permissionsTypeStr);
					}
					updatePoliceReq.setPermissionsType(permissBuf.toString());
				}
			}
		}
	}

}

package com.bms.controller;

import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bms.entity.CreatePoliceReq;
import com.bms.entity.UpdatePoliceReq;
import com.bms.service.rangerimpl.RangerImpl;
import com.bms.service.rangerimpl.SupportRangerImpl;

/**
 * @author YeChunBo
 * @time 2017��8��2��
 *
 *       ��˵��
 */

@SpringBootApplication
@RestController
public class RangerController {

	RangerImpl rangerImpl = new RangerImpl();
	private static Logger log = Logger.getLogger(RangerController.class);

	/**
	 * ��ѯ������Ч�Ĳ���
	 * 
	 * @return
	 */
	@RequestMapping("/query_valid_police")
	@ResponseBody
	String queryValidPolice() {
		log.info("QueryValidPolice......");
		return rangerImpl.getAllValidPolice();
	}

	/**
	 * ��������
	 * 
	 * @param policyUser���û����������û�֮���ö��ŷָӢ�ķ���
	 * @param dbname�����ݿ⣬�أ�����ö��ŷָ�
	 * @param tablename�����ǣ�����û��û�Ӣ�ķ����ŷָ,
	 *            eg:hive_test,hbase_test
	 * @param permissionstype���Ա�Ĳ���Ȩ�ޣ��ǣ�Ĭ��Ϊ
	 *            all, eg:select,update,drop...
	 * @return
	 */
	@RequestMapping("/create_policy")
	@ResponseBody
	Boolean createPolice(@RequestParam(value = "policyuser", required = true) String policyUser,
			@RequestParam(value = "dbname", required = true) String dbname,
			@RequestParam(value = "tablename", required = false, defaultValue = "*") String tablename,
			@RequestParam(value = "permissionstype", required = false, defaultValue = "all") String permissionstype) {

		CreatePoliceReq req = new CreatePoliceReq();

		req.setPoliceUser(policyUser);
		req.setDbName(dbname);
		req.setTableName(tablename);
		req.setPermissionsType(permissionstype);
		log.info("CreatePolice of req=" + req.toString());
		return rangerImpl.createPolice(req);
	}

	/**
	 * ���²��ԣ�ע������ĸ��»Ὣԭ�����õĶ�Ӧ���Ը����ǵ������磬policyUser��Ϊhive��ԭ�����policyUserΪhbase,hdfs
	 * ,���޸����������Ե��û���ֻ��hive����������ͬ��
	 * 
	 * @param policyid
	 * @param policyUser
	 * @param policyname
	 * @param dbname
	 * @param tablename
	 * @param policeIsEnabled�����Կ����ԣ�0����Ч��1����Ч
	 * @param permissionsType
	 * @return
	 */
	@RequestMapping("/update_policy_by_id")
	@ResponseBody
	Boolean updatePolicyById(@RequestParam(value = "policyId", required = true) String policyId,
			@RequestParam(value = "policyUser", required = true) String policyUser,
			@RequestParam(value = "policyName", required = true) String policyName,
			@RequestParam(value = "dbName", required = true) String dbName,
			@RequestParam(value = "tableName", required = false, defaultValue = "*") String tableName,
			@RequestParam(value = "policeIsEnabled", required = false, defaultValue = "1") String policeIsEnabled,
			@RequestParam(value = "permissionstype", required = false, defaultValue = "all") String permissionsType) {

		UpdatePoliceReq updatePoliceReq = new UpdatePoliceReq();

		updatePoliceReq.setPoliceId(policyId);

		if (StringUtils.isNotBlank(dbName))
			updatePoliceReq.setDbName(dbName);

		if (StringUtils.isNotBlank(tableName))
			updatePoliceReq.setTableName(tableName);

		if (StringUtils.isNotBlank(policyName))
			updatePoliceReq.setPoliceName(policyName);

		if (StringUtils.isNotBlank(policyUser))
			updatePoliceReq.setPoliceUser(policyUser);

		if (StringUtils.isNotBlank(permissionsType))
			updatePoliceReq.setPermissionsType(permissionsType);

		if (StringUtils.isNotBlank(policeIsEnabled))
			updatePoliceReq.setPoliceIsEnabled(policeIsEnabled);

		log.info("UpdatePolicyById of req=" + updatePoliceReq.toString());
		return rangerImpl.updatePolicyById(updatePoliceReq);
	}

	/**
	 * ���²��ԣ�ע������Ĳ������û�д��򱣴�ԭ�ȵģ����ȸ��ݲ���������ò��Ե����ԣ��������û�������Ҳ��������������¸�����
	 * 
	 * @param policyName
	 * @param policyUser
	 * @param resourceName
	 * @param tableName
	 * @param permissionsType
	 * @param policyType
	 * @param quartFlag
	 * @return
	 */
	@RequestMapping("/update_policy_by_name")
	@ResponseBody
	Boolean updatePolicyByName(@RequestParam(value = "policyName", required = true) String policyName,
			@RequestParam(value = "policyUser", required = false) String policyUser,
			@RequestParam(value = "resourceName", required = false) String resourceName,
			@RequestParam(value = "tableName", required = false) String tableName,
			@RequestParam(value = "permissionsType", required = false) String permissionsType,
			@RequestParam(value = "policyType", required = false, defaultValue = "hive") String policyType,
			@RequestParam(value = "quartFlag", required = false, defaultValue = "1") String quartFlag) {

		String policyJsonStr = getPolicyByName(policyName, policyType);

		// ��ȡ�����²���ԭ�ȴ��ڵĲ��Բ���װ��java����
		UpdatePoliceReq oldPoliceReq = RangerImpl.transJsonToObject(policyJsonStr, policyType);

		UpdatePoliceReq updatePoliceReq = new UpdatePoliceReq();
		updatePoliceReq.setPoliceName(policyName);

		// ����Ƕ�ʱ������ø÷�������quartFlag ��������1
		if (!"1".equals(quartFlag)) {
			// Ϊ�˷�ֹ��ʱ�������޸Ĳ��ԣ����Ե��û����̳������õ�ʹ�������ѯ�����Ĳ������û��д��Ȩ���򲻽����޸Ĳ��ԵĲ���
			if (!(oldPoliceReq.getPermissionsType().contains("create")
					|| oldPoliceReq.getPermissionsType().contains("write"))) {
				return true;
			} else {
				SupportRangerImpl.handlePermissionsType(oldPoliceReq, updatePoliceReq, permissionsType);
			}
		} else {
			// �����ⲿ�ӿڵ���
			if (StringUtils.isNotBlank(permissionsType))
				updatePoliceReq.setPermissionsType(permissionsType);
			else {
				updatePoliceReq.setPermissionsType(oldPoliceReq.getPermissionsType());
			}
		}

		if (StringUtils.isNotBlank(resourceName)) {
			if ("hdfs".equalsIgnoreCase(policyType)) {
				updatePoliceReq.setHdfsResourcePath(resourceName);
			} else if ("hbase".equalsIgnoreCase(policyType)) {
				updatePoliceReq.setHbaseTableName(resourceName);
			} else {
				updatePoliceReq.setDbName(resourceName);
			}
		} else {
			if ("hdfs".equalsIgnoreCase(policyType)) {
				updatePoliceReq.setHdfsResourcePath(oldPoliceReq.getHdfsResourcePath());
			} else if ("hbase".equalsIgnoreCase(policyType)) {
				updatePoliceReq.setHbaseTableName(oldPoliceReq.getHbaseTableName());
			} else {
				updatePoliceReq.setDbName(oldPoliceReq.getDbName());
			}
		}

		if (StringUtils.isNotBlank(tableName))
			updatePoliceReq.setTableName(tableName);
		else
			updatePoliceReq.setTableName(oldPoliceReq.getTableName());

		if (StringUtils.isNotBlank(policyUser))
			updatePoliceReq.setPoliceUser(policyUser);
		else
			updatePoliceReq.setPoliceUser(oldPoliceReq.getPoliceUser());

		log.info("UpdatePolicyByName of req=" + updatePoliceReq.toString());
		return rangerImpl.updatePolicyByName(updatePoliceReq, policyType);
	}

	/**
	 * ͨ��������ȡ�øò��ԣ����ص��Ǹò����������ԣ�json��ʽ
	 * 
	 * @param policyName
	 * @return
	 */
	@RequestMapping("/get_policy_by_name")
	@ResponseBody
	String getPolicyByName(@RequestParam(value = "policyName", required = true) String policyName,
			@RequestParam(value = "policyType", required = false, defaultValue = "hive") String policyType) {

		log.info("GetPolicyByName of policyName=" + policyName);

		return rangerImpl.getPolicyByName(policyName, policyType);
	}

	/**
	 * ͨ������id ɾ������
	 * 
	 * @param policeId
	 * @return
	 */
	@RequestMapping("/delete_police_by_police_id")
	@ResponseBody
	Boolean deletePoliceByPoliceId(@RequestParam(value = "policeId", required = true) String policeId) {

		log.info("DeletePoliceByPoliceId of policeId=" + policeId);

		return rangerImpl.deletePoliceByPoliceId(policeId);
	}

	/**
	 * ͨ�������� ɾ������
	 * 
	 * @param policeName
	 * @return
	 */
	@RequestMapping("/delete_police_by_police_name")
	@ResponseBody
	Boolean deletePoliceByPoliceName(@RequestParam(value = "policeName", required = true) String policeName) {
		log.info("DeletePoliceByPoliceName of policeName=" + policeName);
		return rangerImpl.deletePoliceByPoliceName(policeName);
	}

	/**
	 * ͨ���û�����ʼʱ���ȡ������ݿ���ʵļ�¼��
	 * 
	 * @param userName
	 * @param startDate
	 *            ��ʼʱ�䣬MM/dd/YYYY 08/29/2017
	 * @return
	 */
	@RequestMapping("/get_user_visit_info")
	@ResponseBody
	String getUserVisitInfo(@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "startDate", required = false, defaultValue = "") String startDate) {
		log.info("GetUserVisitInfo of userName=" + userName);

		return rangerImpl.getUserVisitInfo(userName, startDate);
	}

	/**
	 * ��ȡ��ȺCPUʹ�����������CPU����ֵ���ٷ�����
	 * 
	 * @return
	 */
	@RequestMapping("/get_cluster_cpu_info")
	@ResponseBody
	String getClusterCPUInfo() {
		log.info("GetClusterCPUInfo ......");
		return rangerImpl.getClusterCPUInfo();
	}

}

package com.bms.service;

import com.bms.entity.CreatePoliceReq;
import com.bms.entity.UpdatePoliceReq;

/**
 * @author YeChunBo
 * @time 2017��7��24��
 *
 * ��˵��:Ranger �ӿ�
 */

public interface Ranger {
	
	/**
	 * ��ȡ������Ч�Ĳ���
	 * @return
	 */
	public String getAllValidPolice();
	
	/**
	 * ��������
	 * @param policeUser ���Զ�Ӧ���û�
	 * @param dbName :���ݿ⣬������ݿ��ö��ŷָӢ�ķ���
	 * @param tableName ��������ö��ŷָ�
	 * @param permissionsType ������Ӧ��Ȩ�ޣ�����ö��ŷָ�,eg :drop, all, select, update, create, index, lock, alter
	 * @return
	 */
	public boolean createPolice(CreatePoliceReq createRequest);
	
	/**
	 * ͨ���������ƻ�ȡ����
	 * @param policyName
	 * @param policyType �������ͣ�hive��hdfs/hbase
	 * @return
	 */
	public String getPolicyByName(String policyName, String policyType);

	/**
	 * ͨ��policeName ɾ������
	 * @param policeName
	 * @return
	 */
	public boolean deletePoliceByPoliceName(String policeName);
    
	/**
	 * ͨ��policeId ɾ������
	 * @param policeId
	 * @return
	 */
	public boolean deletePoliceByPoliceId(String policeId);
	
	/**
	 * �޸Ĳ���
	 * @param updatePoliceReq
	 * @return
	 */
	public boolean updatePolicyById(UpdatePoliceReq updatePoliceReq);
	
	/**
	 * �����û�������ȡ���û������ݿ�ķ��ʼ�¼
	 * @param userName
	 * @param startDate
	 * @return
	 */
	public String getUserVisitInfo(String userName, String startDate);
}

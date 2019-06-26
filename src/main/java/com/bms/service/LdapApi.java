package com.bms.service;

import java.util.List;

/**
* @author YeChunBo
* @time 2017��7��27�� 
*
* ��˵�� 
*/

public interface LdapApi {
	
	/**
	 * ��ѯ
	 * @param searchDN
	 * @param filter
	 */
	public List<String> queryLdap(String searchDN, String filter);
	
	/**
	 * ������Ŀ
	 * @param baseDN
	 * @param uid
	 * @param userPwd
	 * @return 0:�Ѵ��ڣ�1�������ɹ�; 2: ����ʧ��
	 */
	public Integer createEntry(String baseDN, String uid, String userPwd);
	
	/**
	 * ɾ����Ŀ
	 * @param requestDN
	 * @return 0:�����ڣ�1��ɾ���ɹ�; 2: ɾ��ʧ��
	 */
	public Integer deleteEntry(String requestDN);
	
	/**
	 * ����shell�ű�����keytab
	 * @param userName
	 * @return
	 */
	public boolean createKeytab(String userName);
	
}

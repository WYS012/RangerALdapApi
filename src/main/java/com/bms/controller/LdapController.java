package com.bms.controller;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bms.service.ldapimpl.LdapApiImpl;

/**
 * @author YeChunBo
 * @time 2017��8��2��
 *
 *       ��˵�� Ldap api�ӿڿ�����
 */

@SpringBootApplication
@RestController
public class LdapController {

	private static Logger log = Logger.getLogger(LdapController.class);
	public static final String DEFAULT_LDAP_PWD = "zwzx_dsj"; // ldap user Ĭ�ϵ��û�����
	private static final Integer operateEntryFail = 2;
	LdapApiImpl ldapApiImpl = new LdapApiImpl();

	/**����ldap �û�
	 * ����������http://localhost:8567/create_ldap_user?uid=bms_test5&pwd=123
	 * @param basedN���Ǳ��裬Ĭ��Ϊ��ou=people,dc=hadoop,dc=apache,dc=org
	 * @param uid������
	 * @param pwd���Ǳ��裬Ĭ��Ϊ��zwzx_dsj
	 * @return
	 */
	@RequestMapping("/create_ldap_user")
	@ResponseBody
	Integer createLdapUser(
			@RequestParam(value = "basedN", required = false, defaultValue = "ou=people,dc=hadoop,dc=apache,dc=org") String basedN,
			@RequestParam(value = "uid", required = true) String uid,
			@RequestParam(value = "pwd", required = false, defaultValue = DEFAULT_LDAP_PWD) String pwd) {

		log.info("LdapController createEntry the base DN is: " + basedN + " ,and the uid is: " + uid + " ,and the usePwd is: " + pwd);
		
		return ldapApiImpl.createEntry(basedN, uid, pwd);
	}

	
	
	
	/**
	 * ��ѯ����Ldap�û�
	 * @param searchDN���Ǳ������
	 * @param filter���Ǳ������
	 * @return
	 */
	@RequestMapping("/query_ldap_user")
	@ResponseBody
	List<String> queryLdapUser(
			@RequestParam(value = "searchDN", required = false, defaultValue = "ou=people,dc=hadoop,dc=apache,dc=org") String searchDN,
			@RequestParam(value = "filter", required = false, defaultValue = "objectClass=person") String filter) {
	
		log.info("LdapController of searchDN: " + searchDN + ", filter :" + filter);

		return ldapApiImpl.queryLdap(searchDN, filter);
	}

	
	
	/**
	 * ɾ��Ldap�û�
	 * @param uid���û�����eg:hive
	 * @return
	 */
	@RequestMapping("/delete_ldap_user")
	@ResponseBody
	Integer deleteLdapUser(@RequestParam(value = "uid", required = true) String uid) {
		// ����ɾ��admin,��Ϊadmin�ǹ����û����û��������Ҫɾ����ͨ��Ambari��AD studio���߽���ɾ��
		if(uid.equals("admin")){
			log.info("DeleteLdapUser of admin is not allow.");
			return operateEntryFail;
		}
		log.info("LdapController of deleteLdapUser: " + uid);
		uid = "uid=" + uid + ",ou=people,dc=hadoop,dc=apache,dc=org";
		
		return ldapApiImpl.deleteEntry(uid);
	}
	
}

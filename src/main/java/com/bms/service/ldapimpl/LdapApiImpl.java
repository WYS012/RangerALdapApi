package com.bms.service.ldapimpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;

import com.bms.service.LdapApi;
import com.bms.utils.PropertyUtil;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.controls.SubentriesRequestControl;

/**
 * @author YeChunBo
 * @time 2017��7��27��
 *
 *       ��˵�� Ldap java api ����
 */

public class LdapApiImpl implements LdapApi {

	private static Logger log = Logger.getLogger(LdapApiImpl.class);
	// ��ǰ������Ϣ
	private static String ldapHost = PropertyUtil.getProperty("ldapHost");
	private static String ldapPort = PropertyUtil.getProperty("ldapPort");
	private static String ldapBindDN = PropertyUtil.getProperty("ldapBindDN");
	private static String ldapPassword = PropertyUtil.getProperty("ldapPassword");
	private static LDAPConnection connection = null;

	/** entry �Ѵ��� */
	private static final Integer EntryIsExist = 0;
	/** entry ������ */
	private static final Integer EntryIsNotExist = 3;
	/** entry �����ɹ� */
	private static final Integer operateEntrySuccess = 1;
	/** entry ����ʧ�� */
	private static final Integer operateEntryFail = 2;

	static {
		if (connection == null) {
			try {
				connection = new LDAPConnection(ldapHost, Integer.parseInt(ldapPort), ldapBindDN, ldapPassword);
			} catch (Exception e) {
				log.error("Connect to ldap is failed, the fail message is��" + e.getMessage());
			}
		}
	}

	public Integer createEntry(String baseDN, String uid, String userPwd) {

		Integer operateFlag = new Integer(operateEntryFail);
		log.info("CreateEntry the base DN is: " + baseDN + " ,and the uid is: " + uid + " ,and the usePwd is: "
				+ userPwd);

		String entryDN = "uid=" + uid + "," + baseDN;
		try {
			SearchResultEntry entry = connection.getEntry(entryDN);
			if (entry == null) {
				// �������򴴽�
				ArrayList<Attribute> attributes = new ArrayList<Attribute>();
				attributes.add(new Attribute("objectClass", "organizationalPerson", "person", "inetOrgPerson", "top"));
				attributes.add(new Attribute("sn", "person"));
				attributes.add(new Attribute("cn", "person"));
				attributes.add(new Attribute("uid", uid));

				// ��������������Ϊ������uid����Ϊ������
				if ("".equals(userPwd) || userPwd == null)
					attributes.add(new Attribute("userPassword", uid));
				else
					attributes.add(new Attribute("userPassword", userPwd));

				try {
					connection.add(entryDN, attributes); // Ldap��ʱ�ڴ����û����׳��쳣�������䴴���û���Ȼ�ɹ����������������keytab�ű�
				} catch (Exception e) {
					entry = connection.getEntry(entryDN);
					if (entry != null) { // ����������û��ǿ���˵���û��Ѵ���
						operateFlag = operateEntrySuccess;
					} else {
						operateFlag = operateEntryFail;
						log.error("Create entry of [" + entryDN + "] is failed, the error message is: " + e.getMessage());
					}
				}

				operateFlag = operateEntrySuccess;
				log.info("CreateEntry of [" + entryDN + "] is successed, and the operateFlag is " + operateFlag);

				boolean createKeytabFlag = false;
				if (operateFlag == operateEntrySuccess) {
					// ���Ldap�û������ɹ���ͬʱҲ�������Ӧ��keytab
					createKeytabFlag = createKeytab(uid);

					// ����shell�ű�ͬ��Ldap�û���Ranger��,�ű���Ҫssh��ȥ��Rangerϵͳ
					String ldap2RangerCommand = PropertyUtil.getProperty("ldapUserSync2RangerPath");
					boolean flag = exceteShell(ldap2RangerCommand);
					log.info(ldap2RangerCommand + " excete is " + flag);

//					// ����shell�ű�ͬ��Ldap�û���Ambari��,�ű���Ҫssh��ȥ��Ambariϵͳ
//					String Ldap2AmbariCommand = PropertyUtil.getProperty("ldapUserSync2AmbariUserPath");
//					boolean ldap2AmbariFlag = exceteShell(Ldap2AmbariCommand);
//					log.info(Ldap2AmbariCommand + " excete is " + ldap2AmbariFlag);
				}
				log.info("CreateKeytab of [" + entryDN + "] is:" + createKeytabFlag);
			} else {
				operateFlag = EntryIsExist;
				log.warn("The entry of [" + entryDN + "] already exists.");
			}
		} catch (Exception e) {
			operateFlag = operateEntryFail;
			log.error("Create entry of [" + entryDN + "] is failed, the error message is: " + e.getMessage());
		}
		return operateFlag;
	}

	public List<String> queryLdap(String searchDN, String filter) {
		log.info("QueryLdap the searchDn is: " + searchDN + " ,and the filter is: " + filter);
		ArrayList<String> entryList = new ArrayList<String>();
		try {
			SearchRequest searchRequest = new SearchRequest(searchDN, SearchScope.SUB, "(" + filter + ")");
			searchRequest.addControl(new SubentriesRequestControl());
			SearchResult searchResult = connection.search(searchRequest);

			log.info("A total of [" + searchResult.getSearchEntries().size() + "] entry was queried. ");

			int index = 1;
			for (SearchResultEntry entry : searchResult.getSearchEntries()) {
				entryList.add(entry.getDN());
				log.info((index++) + "\t" + entry.getDN());
			}
		} catch (Exception e) {
			log.error("Query failed, the fail message is��" + e.getMessage());
		}
		return entryList;
	}

	public Integer deleteEntry(String requestDN) {

		Integer deleteFlag = new Integer(EntryIsNotExist);
		log.info("Delete entry of requestDN " + requestDN);

		try {
			SearchResultEntry entry = connection.getEntry(requestDN);

			if (entry == null) {
				log.warn("DeleteEntry of [" + requestDN + "] is not exist.");
				return deleteFlag;
			}
			// ɾ��
			connection.delete(requestDN);
			deleteFlag = operateEntrySuccess;
			log.info("Delete of [" + requestDN + "] is successed.");
		} catch (Exception e) {
			deleteFlag = operateEntryFail;
			log.error("Delete of [" + requestDN + "] is failed the error message is : " + e.getMessage());
		}
		return deleteFlag;
	}

	public boolean createKeytab(String userName) {
		boolean createKeytabFlag = true;
		InputStreamReader stdISR = null;
		InputStreamReader errISR = null;
		Process process = null;
		String command = PropertyUtil.getProperty("keytabShellPath") + " " + userName;
		try {
			process = Runtime.getRuntime().exec(command);
			String line = null;

			stdISR = new InputStreamReader(process.getInputStream());
			BufferedReader stdBR = new BufferedReader(stdISR);
			while ((line = stdBR.readLine()) != null) {
				log.info("CreateKeytab line:" + line); // ��ִ�нű��������뵽��־��
			}

			errISR = new InputStreamReader(process.getErrorStream());
			BufferedReader errBR = new BufferedReader(errISR);
			while ((line = errBR.readLine()) != null) {
				log.error("CreateKeytab error line:" + line); // ��ִ�нű������г��ֵĴ����뾯�����뵽��־��
			}
		} catch (Exception e) {
			createKeytabFlag = false;
			log.error("Excute shell is failed, errMassage:" + e.getMessage());
		} finally {
			try {
				if (stdISR != null) {
					stdISR.close();
				}
				if (errISR != null) {
					errISR.close();
				}
				if (process != null) {
					process.destroy();
				}
			} catch (IOException e) {
				createKeytabFlag = false;
				log.error("Excute shell is failed, errMassage:" + e.getMessage());
			}
		}
		return createKeytabFlag;
	}

	/**
	 * ִ��shell�ű�
	 * 
	 * @param command
	 * @return
	 */
	public boolean exceteShell(String command) {
		boolean createKeytabFlag = true;
		InputStreamReader stdISR = null;
		InputStreamReader errISR = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			String line = null;

			stdISR = new InputStreamReader(process.getInputStream());
			BufferedReader stdBR = new BufferedReader(stdISR);
			while ((line = stdBR.readLine()) != null) {
				log.info(command + " excete info:" + line); // ��ִ�нű��������뵽��־��
			}

			errISR = new InputStreamReader(process.getErrorStream());
			BufferedReader errBR = new BufferedReader(errISR);
			while ((line = errBR.readLine()) != null) {
				log.error(command + " excete info:" + line); // ��ִ�нű������г��ֵĴ����뾯�����뵽��־��
			}
		} catch (Exception e) {
			createKeytabFlag = false;
			log.error(command + " ,Excute shell is failed, errMassage:" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (stdISR != null) {
					stdISR.close();
				}
				if (errISR != null) {
					errISR.close();
				}
				if (process != null) {
					process.destroy();
				}
			} catch (IOException e) {
				createKeytabFlag = false;
				log.error(command + ",Excute shell is failed, errMassage:" + e.getMessage());
				e.printStackTrace();
			}
		}
		return createKeytabFlag;
	}
	
	
//	 public static void main(String[] args) {
//	
//	 String filter = "objectClass=person";
//	
//	 LdapApiImpl ldapApiImpl = new LdapApiImpl();
//	
//	 // ����entry
//	// Integer intFlag =
//	 ldapApiImpl.createEntry("ou=people,dc=hadoop,dc=apache,dc=org",
//	 "bms_test411", "");
//	// System.out.println(intFlag);
//	
//	 // ɾ��entry
//	// Integer deleteflag =
////	 ldapApiImpl.deleteEntry("uid=bms_test6,ou=people,dc=hadoop,dc=apache,dc=org");
//	// System.out.println("deleteEntryFlag is " + deleteflag);
//	
//	 // ��ѯentry
//	// List<String> entryList =
////	 ldapApiImpl.queryLdap("ou=people,dc=hadoop,dc=apache,dc=org", filter);
//	// for (String entry : entryList) {
//	// System.out.println(entry);
//	// }
//	
//	// ldapApiImpl.queryLdap("ou=people,dc=hadoop,dc=apache,dc=org", filter);
//	 }

}

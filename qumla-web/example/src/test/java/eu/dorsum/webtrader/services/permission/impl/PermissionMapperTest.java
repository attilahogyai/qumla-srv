package eu.dorsum.webtrader.services.permission.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.dorsum.webtrader.domain.AuthorizationInfo;
import eu.dorsum.webtrader.domain.permission.PermissionToGroupRq;
import eu.dorsum.webtrader.domain.permission.Permission;
import eu.dorsum.webtrader.domain.permission.PermissionGroup;
import eu.dorsum.webtrader.domain.permission.PermissionGroupRq;
import eu.dorsum.webtrader.domain.permission.PermissionRq;
import eu.dorsum.webtrader.domain.user.UserRq;
import eu.dorsum.webtrader.services.test.AbstractMyBatisMapperTest;

public class PermissionMapperTest extends AbstractMyBatisMapperTest<PermissionMapper> {

	@Before
	public void before() {
		//Test all databases
		initDb(PermissionMapper.class, TestDb.values());
	}
	
	@Test
	public void testGetUserPermissionGroups() throws Exception {
		for (PermissionMapper mapper : mappers.values()) {
			UserRq request = new UserRq();
			AuthorizationInfo authorizationInfo = new AuthorizationInfo();
			authorizationInfo.setUser("john.smith");
			request.setAuthorizationInfo(authorizationInfo);
			request.setLanguageId(1);
			List<PermissionGroup> groups = mapper.getUserPermissionGroups(request);
			assertNotNull(groups);
		}
	}
	
	@Test
	public void testGetGroupPermissions() throws Exception {
		for (PermissionMapper mapper : mappers.values()) {
			PermissionToGroupRq request = new PermissionToGroupRq();
			request.setGroupCode("ADVISOR");
			List<Permission> permissions = mapper.getGroupPermissions(request);
			assertNotNull(permissions);
		}
	}
	
	@Test
	public void testInsertPermission() throws Exception {
		PermissionRq permission = getPermissionRequest();
		for (PermissionMapper mapper : mappers.values()) {
			permission.setId(null);
			mapper.insertPermissionToDb(permission);
			assertNotNull(permission.getId());
		}
	}
	
	private PermissionRq getPermissionRequest() {
		PermissionRq permission = new PermissionRq();
		permission.setCode("TEST");
		permission.setDescription("description");
		permission.setLanguageId(1);
		AuthorizationInfo authorizationInfo = new AuthorizationInfo();
		authorizationInfo.setUser("testuser");
		permission.setAuthorizationInfo(authorizationInfo);
		return permission;
	}

	@Test
	public void testGetPermission() throws Exception {
		PermissionRq permission = new PermissionRq();
		permission.setId(1);
		permission.setLanguageId(1);
		for (PermissionMapper mapper : mappers.values()) {
			List<Permission> perm = mapper.getPermissions(permission);
			if (perm.isEmpty() == false) {
				Permission p = perm.get(0);
				assertEquals(permission.getId().toString(), p.getId());
				assertNotNull(p.getDescription());
				assertNotNull(p.getCode());
			}
		}
	}
	
	@Test
	public void tetGetAllPermission() throws Exception {
		PermissionRq permission = new PermissionRq();
		permission.setLanguageId(1);
		for (PermissionMapper mapper : mappers.values()) {
			mapper.getPermissions(permission);
		}
	}
	
	@Test
	public void testDeletePermission() throws Exception {
		PermissionRq permission = new PermissionRq();
		permission.setId(-1);
		for (PermissionMapper mapper : mappers.values()) {
			mapper.deletePermissionFromDb(permission);
		}
	}
	
	@Test
	public void testUpdatePermission() throws Exception {
		PermissionRq permission = getPermissionRequest();
		for (PermissionMapper mapper : mappers.values()) {
			mapper.insertPermissionToDb(permission);
			permission.setDescription("desc3");
			permission.setLanguageId(3);
			mapper.updatePermissionInDb(permission);
			Permission updatedPerm = mapper.getPermissions(permission).get(0);
			assertEquals("desc3", updatedPerm.getDescription());
		}
	}
	
	@Test
	public void testGetPermissionGroups() throws Exception {
		PermissionGroupRq permissionGroup = new PermissionGroupRq();
		permissionGroup.setLanguageId(1);
		for (PermissionMapper mapper : mappers.values()) {
			mapper.getPermissionGroups(permissionGroup);
		}
	}
	
	@Test
	public void testInsertPermissionGroup() throws Exception {
		PermissionGroupRq request = getPermissionGroupRequest();
		for (PermissionMapper mapper : mappers.values()) {
			request.setId(null);
			mapper.insertPermissionGroupToDb(request);
			assertNotNull(request.getId());
		}
	}

	private PermissionGroupRq getPermissionGroupRequest() {
		PermissionGroupRq req = new PermissionGroupRq();
		req.setLanguageId(1);
		req.setCode("TEST");
		req.setDescription("unit test");
		AuthorizationInfo authorizationInfo = new AuthorizationInfo();
		authorizationInfo.setUser("test");
		req.setAuthorizationInfo(authorizationInfo);
		return req;
	}
	
	@Test
	public void testDeletePermissionGroup() throws Exception {
		PermissionGroupRq request = new PermissionGroupRq();
		request.setId(-1);
		for (PermissionMapper mapper : mappers.values()) {
			mapper.deletePermissionGroupFromDb(request);
		}
	}
	
	@Test
	public void testUpdatePermissionGroup() throws Exception {
		PermissionGroupRq request = getPermissionGroupRequest();
		for (PermissionMapper mapper : mappers.values()) {
			mapper.insertPermissionGroupToDb(request);
			request.setDescription("desc3");
			request.setLanguageId(3);
			mapper.updatePermissionGroupInDb(request);
			PermissionGroup updatedGroup = mapper.getPermissionGroups(request).get(0);
			assertEquals("desc3", updatedGroup.getDescription());
		}
	}
	
	@Test
	public void testInsertPermissionToGroup() throws Exception {
		PermissionToGroupRq request = getPermissionToGroupRequest();
		for (PermissionMapper mapper : mappers.values()) {
			PermissionRq permission = getPermissionRequest();
			mapper.insertPermissionToDb(permission);
			request.setPermissionId(permission.getId());
			
			PermissionGroupRq group = getPermissionGroupRequest();
			mapper.insertPermissionGroupToDb(group);
			request.setGroupId(group.getId());
			mapper.insertPermissionToGroupInDb(request);
		}
	}

	private PermissionToGroupRq getPermissionToGroupRequest() {
		PermissionToGroupRq request = new PermissionToGroupRq();
		request.setGroupId(1);
		request.setPermissionId(1);
		AuthorizationInfo authorizationInfo = new AuthorizationInfo();
		authorizationInfo.setUser("testuser");
		request.setAuthorizationInfo(authorizationInfo);
		return request;
	}
}

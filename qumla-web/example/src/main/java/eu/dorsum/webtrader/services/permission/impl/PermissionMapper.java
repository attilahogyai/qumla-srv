package eu.dorsum.webtrader.services.permission.impl;

import java.util.List;

import eu.dorsum.webtrader.domain.permission.Permission;
import eu.dorsum.webtrader.domain.permission.PermissionGroupRq;
import eu.dorsum.webtrader.domain.permission.PermissionRq;
import eu.dorsum.webtrader.domain.permission.PermissionToGroupRq;
import eu.dorsum.webtrader.services.permission.PermissionService;

public interface PermissionMapper extends PermissionService {

	public void insertPermissionToDb(PermissionRq permission);
	
	public List<Permission> getPermissions(PermissionRq request);

	public void deletePermissionFromDb(PermissionRq permission);

	public void updatePermissionInDb(PermissionRq request);

	public void insertPermissionGroupToDb(PermissionGroupRq permission);

	public void deletePermissionGroupFromDb(PermissionGroupRq request);

	public void updatePermissionGroupInDb(PermissionGroupRq request);

	public void insertPermissionToGroupInDb(PermissionToGroupRq request);

}

package eu.dorsum.webtrader.services.permission.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.dorsum.webtrader.domain.permission.Permission;
import eu.dorsum.webtrader.domain.permission.PermissionGroup;
import eu.dorsum.webtrader.domain.permission.PermissionGroupRq;
import eu.dorsum.webtrader.domain.permission.PermissionRq;
import eu.dorsum.webtrader.domain.permission.PermissionToGroupRq;
import eu.dorsum.webtrader.domain.user.UserRq;
import eu.dorsum.webtrader.services.permission.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

	private static final Logger logger = Logger.getLogger(PermissionServiceImpl.class);
	
	@Autowired
	private PermissionMapper mapper;
	
	@Override
	public List<PermissionGroup> getUserPermissionGroups(UserRq request) {
		return mapper.getUserPermissionGroups(request);
	}
	
	@Override
	public List<Permission> getGroupPermissions(PermissionToGroupRq request) {
		return mapper.getGroupPermissions(request);
	}

	@Override
	public Permission insertPermission(PermissionRq permission) {
		mapper.insertPermissionToDb(permission);
		Permission inserted = mapper.getPermissions(permission).get(0);
		return inserted;
	}

	@Override
	public Permission deletePermission(PermissionRq permission) {
		List<Permission> toDelete = mapper.getPermissions(permission);
		mapper.deletePermissionFromDb(permission);
		if (toDelete.isEmpty()) {
			return null;
		}
		return toDelete.get(0);
	}

	@Override
	public Permission updatePermission(PermissionRq request) {
		mapper.updatePermissionInDb(request);
		List<Permission> updatedList = mapper.getPermissions(request);
		if (updatedList.isEmpty()) {
			return null;
		}
		return updatedList.get(0);
	}

	@Override
	public List<Permission> getPermissions(PermissionRq request) {
		return mapper.getPermissions(request);
	}

	@Override
	public PermissionGroup insertPermissionGroup(PermissionGroupRq permissionGroup) {
		mapper.insertPermissionGroupToDb(permissionGroup);
		PermissionGroup inserted = mapper.getPermissionGroups(permissionGroup).get(0);
		return inserted;
	}

	@Override
	public List<PermissionGroup> getPermissionGroups(PermissionGroupRq permissionGroup) {
		return mapper.getPermissionGroups(permissionGroup);
	}

	@Override
	public PermissionGroup deletePermissionGroup(PermissionGroupRq request) {
		List<PermissionGroup> toDelete = mapper.getPermissionGroups(request);
		mapper.deletePermissionGroupFromDb(request);
		if (toDelete.isEmpty()) {
			return null;
		}
		return toDelete.get(0);
	}

	@Override
	public PermissionGroup updatePermissionGroup(PermissionGroupRq request) {
		mapper.updatePermissionGroupInDb(request);
		List<PermissionGroup> updatedList = mapper.getPermissionGroups(request);
		if (updatedList.isEmpty()) {
			return null;
		}
		return updatedList.get(0);
	}

	@Override
	public List<Permission> insertPermissionToGroup(PermissionToGroupRq request) {
		PermissionToGroupRq groupPermissionsRequest = new PermissionToGroupRq();
		groupPermissionsRequest.setGroupId(request.getGroupId());
		groupPermissionsRequest.setGroupCode(request.getGroupCode());
		List<Permission> groupPermissions = mapper.getGroupPermissions(groupPermissionsRequest);
		
		if (permissionAlreadyAssignedToTheGroup(groupPermissions, request.getPermissionId())) {
			logger.info("Permission (" + request.getPermissionId() + ") already assigned to group " + request.getGroupId());
			return groupPermissions;
		}
		
		mapper.insertPermissionToGroupInDb(request);
		return mapper.getGroupPermissions(groupPermissionsRequest);
	}

	private boolean permissionAlreadyAssignedToTheGroup(List<Permission> groupPermissions, Integer permissionId) {
		for (Permission permission : groupPermissions) {
			if (permission.getId().equals(permissionId.toString())) {
				return true;
			}
		}
		return false;
	}

}

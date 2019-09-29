package inventory.model;

import java.util.Map;

public class AuthForm {
	
	private int roleId;
	private int menuId;
	private int permission;
	
	public AuthForm() {
		
	}

	public AuthForm(int roleId, int menuId, int permission) {
		this.roleId = roleId;
		this.menuId = menuId;
		this.permission = permission;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getPermission() {
		return permission;
	}

	public void setPermission(int permission) {
		this.permission = permission;
	}

}

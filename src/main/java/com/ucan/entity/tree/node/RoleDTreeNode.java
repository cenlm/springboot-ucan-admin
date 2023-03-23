package com.ucan.entity.tree.node;

import com.ucan.entity.Role;

/**
 * @Description: 角色DTree节点模型
 * @author liming.cen
 * @date 2023年2月19日 下午3:49:15
 */
public class RoleDTreeNode extends DTreeNode<RoleDTreeNode,Role> {
    /**
     * 角色Id
     */
    private String roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 节点位置
     */
    private Integer position;

    /***
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String modifyTime;
    /**
     * 备注
     */
    private String remarks;

    public String getRoleId() {
	return roleId;
    }

    public void setRoleId(String roleId) {
	this.roleId = roleId;
    }

    public String getRoleName() {
	return roleName;
    }

    public void setRoleName(String roleName) {
	this.roleName = roleName;
    }

    public Integer getPosition() {
	return position;
    }

    public void setPosition(Integer position) {
	this.position = position;
    }

    public String getCreateTime() {
	return createTime;
    }

    public void setCreateTime(String createTime) {
	this.createTime = createTime;
    }

    public String getModifyTime() {
	return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
	this.modifyTime = modifyTime;
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

}

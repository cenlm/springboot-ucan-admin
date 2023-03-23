package com.ucan.entity.tree.node;

/**
 * @Description: 权限树节点模型（树表格）
 * @author liming.cen
 * @date 2023年2月14日 14:57:30
 */
public class PermissionTreeNode extends TreeNode<PermissionTreeNode> {
    /**
     * 权限Id
     */
    private String permissionId;
    /**
     * 权限名称
     */
    private String permissionName;
    /**
     * 权限类型
     */
    private String permissionType;
    /**
     * 权限编码
     */
    private String permissionCode;
    /**
     * 上级节点
     */
    private String parentId;
    /**
     * 上级节点名称
     */
    private String parentName;
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
    /**
     * 权限字体图标
     */
    private String icon;
    /**
     * 节点链接
     */
    private String url;
    /**
     * 系统编码
     */
    private String sysCode;
    private boolean open = true;

    public String getPermissionId() {
	return permissionId;
    }

    public void setPermissionId(String permissionId) {
	this.permissionId = permissionId;
    }

    public String getPermissionName() {
	return permissionName;
    }

    public void setPermissionName(String permissionName) {
	this.permissionName = permissionName;
    }

    public String getPermissionType() {
	return permissionType;
    }

    public void setPermissionType(String permissionType) {
	this.permissionType = permissionType;
    }

    public String getParentId() {
	return parentId;
    }

    public void setParentId(String parentId) {
	this.parentId = parentId;
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

    public String getIcon() {
	return icon;
    }

    public void setIcon(String icon) {
	this.icon = icon;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getSysCode() {
	return sysCode;
    }

    public void setSysCode(String sysCode) {
	this.sysCode = sysCode;
    }

    public boolean isOpen() {
	return open;
    }

    public void setOpen(boolean open) {
	this.open = open;
    }

    public String getPermissionCode() {
	return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
	this.permissionCode = permissionCode;
    }

    public String getParentName() {
	return parentName;
    }

    public void setParentName(String parentName) {
	this.parentName = parentName;
    }

}

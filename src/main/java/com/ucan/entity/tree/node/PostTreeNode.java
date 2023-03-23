package com.ucan.entity.tree.node;

/**
 * @Description:  职位树节点模型
 * @author liming.cen
 * @date 2023年2月13日 23:37:00
 */
public class PostTreeNode extends TreeNode<PostTreeNode> {

    /**
     * 职位ID
     */
    private String postId;
    /**
     * 职位名称
     */
    private String postName;
    /**
     * 所属组织
     */
    private String orgId;
    /**
     * 职位编码
     */
    private String postCode;
    /**
     * 上级节点
     */
    private String parentId;
    /**
     * 上级节点
     */
    private String parentName;

    /**
     * 职位字体图标
     */
    private String icon;

    public String getPostId() {
	return postId;
    }

    public void setPostId(String postId) {
	this.postId = postId;
    }

    public String getPostName() {
	return postName;
    }

    public void setPostName(String postName) {
	this.postName = postName;
    }

    public String getOrgId() {
	return orgId;
    }

    public void setOrgId(String orgId) {
	this.orgId = orgId;
    }

    public String getPostCode() {
	return postCode;
    }

    public void setPostCode(String postCode) {
	this.postCode = postCode;
    }

    public String getParentId() {
	return parentId;
    }

    public void setParentId(String parentId) {
	this.parentId = parentId;
    }

    public String getIcon() {
	return icon;
    }

    public void setIcon(String icon) {
	this.icon = icon;
    }

    public String getParentName() {
	return parentName;
    }

    public void setParentName(String parentName) {
	this.parentName = parentName;
    }

}

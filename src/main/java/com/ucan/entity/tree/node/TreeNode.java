package com.ucan.entity.tree.node;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Layui v2.7.6 树节点模型（树表格）
 * @author liming.cen
 * @date 2023年2月13日 23:35:00
 */
public class TreeNode<T extends TreeNode> {
    /**
     * 节点唯一索引值，用于对指定节点进行各类操作
     */
    private String id;
    /**
     * 节点标题
     */
    private String title;
    /**
     * 节点字段名，一般对应表字段名
     */
    private String field;
    /**
     * 点击节点弹出新窗口对应的 url。需开启 isJump 参数
     */
    private String href;
    /**
     * 节点是否初始展开，默认 false
     */
    private boolean spread;
    /**
     * 节点是否初始为选中状态（如果开启复选框的话），默认 false
     */
    private boolean checked;
    /**
     * 节点是否为禁用状态。默认 false
     */
    private boolean disabled;
    /**
     * 节点在同级结构中的位置
     */
    private Integer position;
    /**
     * 子节点,支持设定选项同父节点。[{title: '子节点1', id: '111'}]
     */

    private List<T> children = new ArrayList<>();

    /**
     * 孩子节点排序
     */
    public void sortChildren() {
	this.children.sort((m1, m2) -> {
	    int orderBy1 = m1.getPosition();
	    int orderBy2 = m2.getPosition();
	    return orderBy1 < orderBy2 ? -1 : (orderBy1 == orderBy2 ? 0 : 1);
	});
	// 对每个节点的下一层节点进行排序
	for (T n : children) {
	    if (n != null && n.getChildren() != null) {
		n.sortChildren();
	    }
	}
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getField() {
	return field;
    }

    public void setField(String field) {
	this.field = field;
    }

    public String getHref() {
	return href;
    }

    public void setHref(String href) {
	this.href = href;
    }

    public boolean isSpread() {
	return spread;
    }

    public void setSpread(boolean spread) {
	this.spread = spread;
    }

    public boolean isChecked() {
	return checked;
    }

    public void setChecked(boolean checked) {
	this.checked = checked;
    }

    public boolean isDisabled() {
	return disabled;
    }

    public void setDisabled(boolean disabled) {
	this.disabled = disabled;
    }

    public List<T> getChildren() {
	return children;
    }

    public void setChildren(List<T> children) {
	this.children = children;
    }

    public Integer getPosition() {
	return position;
    }

    public void setPosition(Integer position) {
	this.position = position;
    }

}

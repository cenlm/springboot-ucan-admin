package com.ucan.entity.tree.node;

import java.util.ArrayList;
import java.util.List;

import com.ucan.entity.tree.response.CheckArr;

/**
 * * @Description: dtree树节点模型
 * 
 * @author liming.cen
 * @date 2023年2月19日 下午2:57:20
 * @param <T> DTreeNode的子类
 * @param <E> 用户需要存储在树节点上的额外数据的数据类型
 */
public class DTreeNode<T extends DTreeNode, E> {
    /**
     * 节点id
     */
    private String id;

    /**
     * 节点标题
     */
    private String title;
    /**
     * 父节点id
     */
    private String parentId;
    /**
     * 节点所在层级
     */
    private String level;
    /**
     * 是否展开节点
     */
    private Boolean spread;
    /**
     * 是否最后一级节点
     */
    private Boolean last;
    /**
     * 是否隐藏
     */
    private Boolean hide;
    /**
     * 是否禁用
     */
    private Boolean disabled;
    /**
     * 节点前的第一个图标
     */
    private String ficonClass;
    /**
     * 节点前的第二个图标class
     * 
     * 
     */
    private String iconClass;
    /**
     * 节点在同级结构中的位置
     */
    private Integer position;
    /**
     * 表示用户自定义需要存储在树节点中的数据
     */
    private E basicData;
    /**
     * 复选框标记类
     */
    private List<CheckArr> checkArr = new ArrayList<>();
    /**
     * 子节点容器
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

    /**
     * checkArr 复选框列表，表示节点前要开启多少个复选框。此方法执行几次，节点前就增加几个复选框
     * 
     * @param type    复选框选中形式： 0上下级联、1半选、2单向级联、3独立、4仅存一个
     * @param checked 选中状态：0-未选中，1-选中，2-半选
     */
    public void setTypeAndChecked(String type, String checked) {
	CheckArr caArr = new CheckArr();
	caArr.setType(type);
	caArr.setChecked(checked);
	checkArr.add(caArr);
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

    public String getParentId() {
	return parentId;
    }

    public void setParentId(String parentId) {
	this.parentId = parentId;
    }

    public String getLevel() {
	return level;
    }

    public void setLevel(String level) {
	this.level = level;
    }

    public Boolean getSpread() {
	return spread;
    }

    public void setSpread(Boolean spread) {
	this.spread = spread;
    }

    public Boolean getLast() {
	return last;
    }

    public void setLast(Boolean last) {
	this.last = last;
    }

    public Boolean getHide() {
	return hide;
    }

    public void setHide(Boolean hide) {
	this.hide = hide;
    }

    public Boolean getDisabled() {
	return disabled;
    }

    public void setDisabled(Boolean disabled) {
	this.disabled = disabled;
    }

    public Integer getPosition() {
	return position;
    }

    public void setPosition(Integer position) {
	this.position = position;
    }

    public String getIconClass() {
	return iconClass;
    }

    public void setIconClass(String iconClass) {
	this.iconClass = iconClass;
    }

    public String getFiconClass() {
	return ficonClass;
    }

    public void setFiconClass(String ficonClass) {
	this.ficonClass = ficonClass;
    }

    public E getBasicData() {
	return basicData;
    }

    public void setBasicData(E basicData) {
	this.basicData = basicData;
    }

    public List<CheckArr> getCheckArr() {
	return checkArr;
    }

    public void setCheckArr(List<CheckArr> checkArr) {
	this.checkArr = checkArr;
    }

    public List<T> getChildren() {
	return children;
    }

    public void setChildren(List<T> children) {
	this.children = children;
    }

}

package com.ucan.entity.tree.response;

/**
 * @Description: 复选框标记类
 * @author liming.cen
 * @date 2023年2月19日 下午3:13:02
 */
public class CheckArr {
    /** 复选框标记 数据中指定了type属性，则type必须从0开始 */
    private String type;
    /** 复选框是否选中 0-未选中，1-选中，2-半选 */
    private String checked;

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getChecked() {
	return checked;
    }

    public void setChecked(String checked) {
	this.checked = checked;
    }

}

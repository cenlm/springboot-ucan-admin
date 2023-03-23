package com.ucan.entity.tree.status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ucan.entity.tree.node.DTreeNode;
import com.ucan.entity.tree.response.CheckArr;

/**
 * @Description: 节点复选框状态设置类
 * @author liming.cen
 * @date 2023年2月21日 上午9:18:14
 */
public class NodeStatus<T extends DTreeNode> {
    /**
     * 节点数据【半选中】状态回显设置：<br>
     * 通过计算已拼装的权限树节点数据，来重新设置节点的复选框状态的值（checkArr）。<br>
     * 通过递归的方式遍历所有节点来与要回显的权限数据进行匹配：<br>
     * 1.如果节点有直接子节点（非孙子节点），且所有直接子节点都包含在要回显的数据中，则<br>
     * 该节点设置为选中状态，否则为半选中状态。<br>
     * 2.如果当前节点没有子节点，则判断一下节点是否包含在回显数据中，包含则设置为选中，否则设为未选中。<br>
     * 3.如果当前节点被选中了，那么他的所有子孙节点都会被选中<br>
     * 4.子节点判断方式：通过判断子节点的个数跟（子节点集合与要回显的数据集合）的交集个数是否相等即可。
     * 
     * @param nodes      已拼装完成的全部节点数据
     * @param matchedIds 要回显的Id数据集合
     * @param isAll      首次调用该方法，请传入false<br>
     *                   说明：判断当前节点是否已被选中。如果当前节点被选中，那么他的所有子节点都将被选中
     * @return
     */
    public List<T> judgeNoAllStatusToShowTree(List<T> nodes, List<String> matchedIds, boolean isAll) {
	if (nodes.size() > 0 && matchedIds.size() > 0) {
	    nodes.forEach(item -> {
		// 计算节点是否有子节点
		List<T> children = item.getChildren();
		if (children.size() > 0) {// 如果有子节点，则与matchedIds进行交集计算
		    if (isAll) {// 如果当前节点已被选中，则他的所有子节点都会被选中
			List<CheckArr> checkArr = item.getCheckArr();
			checkArr.forEach(checkedArr -> {
			    checkedArr.setType("1");
			    checkedArr.setChecked("1");
			});
			judgeNoAllStatusToShowTree(children, matchedIds, true);
		    } else {
			if (matchedIds.contains(item.getId())) {// 如果当前已被匹配，则设置为被选中状态
			    List<CheckArr> checkArr = item.getCheckArr();
			    checkArr.forEach(checkedArr -> {
				checkedArr.setType("1");
				checkedArr.setChecked("1");
			    });
			    judgeNoAllStatusToShowTree(children, matchedIds, true);
			} else {
			    List<String> ids = new ArrayList<>();
			    children.forEach(item1 -> {
				ids.add(item1.getId());
			    });
			    List<String> intersection = ids.stream().filter(perid -> matchedIds.contains(perid))
				    .collect(Collectors.toList());
			    if (intersection.size() == children.size()) {// id交集数据长度等于children，说明子节点被全选了，此时父节点要全选
				List<CheckArr> checkArr = item.getCheckArr();
				checkArr.forEach(checkedArr -> {
				    checkedArr.setType("1");
				    checkedArr.setChecked("1");
				});
			    } else if (intersection.size() == 0) {// 如果都没有被选中，则当前节点设置为未选中
				List<CheckArr> checkArr = item.getCheckArr();
				checkArr.forEach(checkedArr -> {
				    checkedArr.setType("1");
				    checkedArr.setChecked("0");
				});
			    } else {
				List<CheckArr> checkArr = item.getCheckArr();
				checkArr.forEach(checkedArr -> {
				    checkedArr.setType("1");
				    checkedArr.setChecked("2");
				});
			    }
			    judgeNoAllStatusToShowTree(children, matchedIds, false);
			}
		    }

		} else {// 如果没有子节点，则直接判断是否节点匹配
		    if (matchedIds.contains(item.getId())) {// 匹配则是选中状态
			List<CheckArr> checkArr = item.getCheckArr();
			checkArr.forEach(checkedArr -> {
			    checkedArr.setType("1");
			    checkedArr.setChecked("1");
			});
		    } else {
			if (isAll) {// 如果根节点被选中了，则所有子节点都会被选中
			    List<CheckArr> checkArr = item.getCheckArr();
			    checkArr.forEach(checkedArr -> {
				checkedArr.setType("1");
				checkedArr.setChecked("1");
			    });
			} else {
			    List<CheckArr> checkArr = item.getCheckArr();
			    checkArr.forEach(checkedArr -> {
				checkedArr.setType("1");
				checkedArr.setChecked("0");
			    });
			}

		    }
		}
	    });
	}
	return nodes;
    }

    /**
     * 节点数据【上下级联】状态回显设置：<br>
     * 通过计算已拼装的权限树节点数据，来重新设置节点的复选框状态的值（checkArr）。<br>
     * 通过递归的方式遍历所有节点来与要回显的权限数据进行匹配：<br>
     * 1.只要当前节点有直接子节点（非孙子节点），包含在要回显的数据中，则<br>
     * 该节点设置为选中状态。如果没有一个子节点被选中，则当前节点状态设为未选中。<br>
     * 2.如果当前节点没有子节点，则判断当前节点是否包含在回显数据中，如果包含则设置为选中，否则设为未选中。<br>
     * 3.子节点判断方式：通过判断是否有子节点被选中即可。<br>
     * 
     * @param nodes      已拼装完成的全部节点数据
     * @param matchedIds 要回显的Id数据集合
     * @return
     */
    public List<T> judgeAllStatusToShowTree(List<T> nodes, List<String> matchedIds) {
	if (nodes.size() > 0 && matchedIds.size() > 0) {
	    nodes.forEach(item -> {
		// 计算节点是否有子节点
		List<T> children = item.getChildren();
		if (children.size() > 0) {// 如果有子节点，则与matchedIds进行交集计算
		    List<String> ids = new ArrayList<>();
		    children.forEach(item1 -> {
			ids.add(item1.getId());
		    });
		    List<String> intersection = ids.stream().filter(perid -> matchedIds.contains(perid))
			    .collect(Collectors.toList());
		    if (intersection.size() > 0) {// 只要有子节点被选中，父节点就要选中
			List<CheckArr> checkArr = item.getCheckArr();
			checkArr.forEach(checkedArr -> {
			    checkedArr.setType("0");
			    checkedArr.setChecked("1");
			});
		    } else {// 没有一个子节点被选中，父节点就不选中
			List<CheckArr> checkArr = item.getCheckArr();
			checkArr.forEach(checkedArr -> {
			    checkedArr.setType("0");
			    checkedArr.setChecked("0");
			});
		    }
		    judgeAllStatusToShowTree(children, matchedIds);
		} else {// 如果没有子节点，则直接判断是否节点匹配
		    if (matchedIds.contains(item.getId())) {// 匹配则是选中状态
			List<CheckArr> checkArr = item.getCheckArr();
			checkArr.forEach(checkedArr -> {
			    checkedArr.setType("0");
			    checkedArr.setChecked("1");
			});
		    } else {// 节点未被选中
			List<CheckArr> checkArr = item.getCheckArr();
			checkArr.forEach(checkedArr -> {
			    checkedArr.setType("0");
			    checkedArr.setChecked("0");
			});
		    }
		}
	    });
	}
	return nodes;
    }

    /**
     * 节点数据【独立】状态回显设置：<br>
     * 描述：没有上下级联和半选状态，数据匹配到哪个节点，哪个节点就被选中，不影响其他任何节点
     * 
     * @param nodes
     * @param matchedIds
     * @return
     */
    public List<T> judgeSelfStatusToShowTree(List<T> nodes, List<String> matchedIds) {
	if (nodes.size() > 0 && matchedIds.size() > 0) {
	    nodes.forEach(item -> {
		// 判断当前节点状态
		if (matchedIds.contains(item.getId())) {// 数据匹配到当前节点，则直接设置选中
		    List<CheckArr> checkArr = item.getCheckArr();
		    checkArr.forEach(checkedArr -> {
			checkedArr.setType("3");
			checkedArr.setChecked("1");
		    });
		} else {
		    List<CheckArr> checkArr = item.getCheckArr();
		    checkArr.forEach(checkedArr -> {
			checkedArr.setType("3");
			checkedArr.setChecked("0");
		    });
		}
		// 计算节点是否有子节点
		List<T> children = item.getChildren();
		if (children.size() > 0) {// 如果有子节点，则判断子节点状态
		    children.forEach(child -> {
			if (matchedIds.contains(child.getId())) {
			    List<CheckArr> checkArr = child.getCheckArr();
			    checkArr.forEach(checkedArr -> {
				checkedArr.setType("3");
				checkedArr.setChecked("1");
			    });
			} else {
			    List<CheckArr> checkArr = child.getCheckArr();
			    checkArr.forEach(checkedArr -> {
				checkedArr.setType("3");
				checkedArr.setChecked("0");
			    });
			}
		    });
		    judgeSelfStatusToShowTree(children, matchedIds);
		}
	    });
	}
	return nodes;
    }
}

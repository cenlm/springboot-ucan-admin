package com.ucan.utils.page;

import com.ucan.entity.page.PageParameter;

/**
 * @Description:
 * @author liming.cen
 * @date 2023年2月11日 下午4:16:57
 */
public class PageUtil {
    /**
     * 设置PageParameter
     */
    public static void setPageParameter(PageParameter page, int totalCount) {
	int totalPage = totalCount / page.getPageSize() + (totalCount % page.getPageSize() == 0 ? 0 : 1);
	int prePage = page.getCurrentPage() == 1 ? 1 : page.getCurrentPage() - 1;
	int nextPage = page.getCurrentPage() == totalPage ? totalPage : page.getCurrentPage() + 1;
	page.setTotalCount(totalCount);
	page.setTotalPages(totalPage);
	page.setPrePage(prePage);
	page.setNextPage(nextPage);
	page.setEndPage(totalPage);
	// 分页sql索引(limit offset,pageSize ),从0开始
	int beginRow = (page.getCurrentPage() - 1) * page.getPageSize();
	if (page.getCurrentPage() - 1 < 0) {
	    beginRow = 0;
	}
	page.setBeginRow(beginRow);
    }
}

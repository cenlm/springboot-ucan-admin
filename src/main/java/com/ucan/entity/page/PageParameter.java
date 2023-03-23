package com.ucan.entity.page;

/**
 * @Description: 分页参数对象
 * @author liming.cen
 * @date 2023年1月10日 上午11:11:12
 */

public class PageParameter {
    /**
     * 首页
     */
    private int firstPage = 1;
    /**
     * 尾页
     */
    private int endPage;
    /**
     * 当前页
     */
    private int currentPage;
    /**
     * 上一页
     */
    private int prePage;
    /**
     * 下一页
     */
    private int nextPage;

    /**
     * 每页显示记录数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int totalPages;
    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * limit beginRow,pageSize
     */
    private int beginRow;

    public PageParameter(int currentPage, int pageSize) {
	this.currentPage = currentPage;
	this.pageSize = pageSize;
    }

    public PageParameter(String currentPage, String pageSize) {
	this.currentPage = Integer.parseInt(currentPage);
	this.pageSize = Integer.parseInt(pageSize);
    }

    public int getCurrentPage() {
	return currentPage;
    }

    public void setCurrentPage(int currentPage) {
	this.currentPage = currentPage;
    }

    public int getPrePage() {
	return prePage;
    }

    public void setPrePage(int prePage) {
	this.prePage = prePage;
    }

    public int getNextPage() {
	return nextPage;
    }

    public void setNextPage(int nextPage) {
	this.nextPage = nextPage;
    }

    public int getTotalPages() {
	return totalPages;
    }

    public void setTotalPages(int totalPages) {
	this.totalPages = totalPages;
    }

    public int getTotalCount() {
	return totalCount;
    }

    public void setTotalCount(int totalCount) {
	this.totalCount = totalCount;
    }

    public int getPageSize() {
	return pageSize;
    }

    public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
    }

    public int getFirstPage() {
	return firstPage;
    }

    public int getEndPage() {
	return endPage;
    }

    public void setEndPage(int endPage) {
	this.endPage = endPage;
    }

    public int getBeginRow() {
        return beginRow;
    }

    public void setBeginRow(int beginRow) {
        this.beginRow = beginRow;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }
    

}

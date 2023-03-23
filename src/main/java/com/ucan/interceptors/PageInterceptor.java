package com.ucan.interceptors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;

import com.ucan.entity.page.PageParameter;

/**
 * @Description:分页拦截器（重构sql语句进行分页）
 * @author liming.cen
 * @date 2023年1月10日 上午9:09:08
 */
@Intercepts({
	@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class PageInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
	RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
	MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
		SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
//	// 获取configuration对象
//	Configuration configuration = (Configuration) metaObject.getValue("delegate.configuration");
	// 获取mappedStatement对象
	MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
	// 获取statementId
	String statementId = mappedStatement.getId();
	String pageStatementId = ".*Page$";
	PageParameter page = null;
	if (statementId.matches(pageStatementId)) {// 只对以 Page 结尾的statement进行拦截处理
	    // 获取当前连接
	    Connection con = (Connection) invocation.getArgs()[0];
	    // 获取boundSql对象
	    BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
	    // 获取原来的sql语句
	    String sql = boundSql.getSql();
	    page = (PageParameter) metaObject.getValue("delegate.boundSql.parameterObject.page");
	    if (null == page) {
		throw new NullPointerException("分页参数对象不能为空！");
	    } else {
		String pageSql = buildPageSql(page, sql);
		metaObject.setValue("delegate.boundSql.sql", pageSql);
		int totalCounts = getTotalCounts(con, sql, mappedStatement, boundSql);
		setPageParameter(page, totalCounts);
		
	    }
	}

	return invocation.proceed();
    }

    /**
     * 构建分页sql语句
     * 
     * @return
     */
    private String buildPageSql(PageParameter page, String sql) {
	// 当前页
	int currPage = page.getCurrentPage();
	// 每页显示的记录数
	int pageSize = page.getPageSize();
	// 分页sql索引(limit offset,pageSize ),从0开始
	int beginRow = (currPage - 1) * pageSize;
	if (currPage - 1 < 0) {
	    beginRow = 0;
	}
	StringBuilder sb = new StringBuilder(100);
	sb.append(sql);
	sb.append(" limit " + beginRow + "," + pageSize);

	return sb.toString();
    }

    /**
     * 获取总记录数
     * 
     * @return
     */
    private int getTotalCounts(Connection connection, String sql, MappedStatement mappedStatement, BoundSql boundSql) {
	String countSql = "select count(*) from (" + sql + ") as total";
	PreparedStatement prepare = null;
	ResultSet rs = null;
	int total = 0;
	try {
	    prepare = connection.prepareStatement(countSql);
	    // 使用原来的BoundSql、MappedStatement对象来获取相关信息，使用这些信息生成countSql的BoundSql
	    BoundSql countBSQL = new BoundSql(mappedStatement.getConfiguration(), countSql,
		    boundSql.getParameterMappings(), boundSql.getParameterObject());
	    // 最后通过ParameterHandler来对预编译countSql 进行参数设置
	    ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement,
		    boundSql.getParameterObject(), countBSQL);
	    parameterHandler.setParameters(prepare);
	    rs = prepare.executeQuery();
	    if (rs.next()) {
		total = rs.getInt(1);
	    }
	} catch (SQLException e) {
	    // TODO: handle exception
	} finally {
	    try {
		if (rs != null) {
		    rs.close();
		}

	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	    try {
		if (prepare != null) {
		    prepare.close();
		}

	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return total;
    }

    /**
     * 设置PageParameter
     */
    private void setPageParameter(PageParameter page, int totalCount) {
	int totalPage = totalCount / page.getPageSize() + (totalCount % page.getPageSize() == 0 ? 0 : 1);
	int prePage = page.getCurrentPage() == 1 ? 1 : page.getCurrentPage() - 1;
	int nextPage = page.getCurrentPage() == totalPage ? totalPage : page.getCurrentPage() + 1;
	page.setTotalCount(totalCount);
	page.setTotalPages(totalPage);
	page.setPrePage(prePage);
	page.setNextPage(nextPage);
	page.setEndPage(totalPage);

    }

    @Override
    public Object plugin(Object target) {
	if (target instanceof StatementHandler) {// 只对StatementHandler接口进行拦截代理
	    return Plugin.wrap(target, this);
	} else {
	    return target;
	}
    }

    @Override
    public void setProperties(Properties properties) {

    }

}

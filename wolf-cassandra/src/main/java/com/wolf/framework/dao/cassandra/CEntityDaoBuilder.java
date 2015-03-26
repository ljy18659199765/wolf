package com.wolf.framework.dao.cassandra;

import com.datastax.driver.core.Session;
import com.wolf.framework.dao.ColumnHandler;
import com.wolf.framework.dao.Entity;
import com.wolf.framework.dao.inquire.InquireByKeyFilterHandlerImpl;
import com.wolf.framework.dao.inquire.InquireByKeyFromDatabaseHandlerImpl;
import com.wolf.framework.dao.inquire.InquireByKeyHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * 实体数据访问对象创建类
 *
 * @author aladdin
 * @param <T>
 */
public final class CEntityDaoBuilder<T extends Entity> {

    //表空间
    private final String keyspace;
    //table name
    private final String table;
    //是否是counter
    private final boolean counter;
    //key
    private final ColumnHandler keyHandler;
    //column
    private final List<ColumnHandler> columnHandlerList;
    //实体class
    private final Class<T> clazz;
    //
    private final CEntityDaoContext<T> entityDaoContext;
    //
    private final CassandraAdminContext cassandraAdminContext;

    public CEntityDaoBuilder(String keyspace, String tableName, boolean counter, ColumnHandler keyHandler, List<ColumnHandler> columnHandlerList, Class<T> clazz, CEntityDaoContext<T> entityDaoContext, CassandraAdminContext cassandraAdminContext) {
        this.keyspace = keyspace;
        this.table = tableName;
        this.counter = counter;
        this.keyHandler = keyHandler;
        if (columnHandlerList == null) {
            this.columnHandlerList = new ArrayList<ColumnHandler>(0);
        } else {
            this.columnHandlerList = columnHandlerList;
        }
        this.clazz = clazz;
        this.entityDaoContext = entityDaoContext;
        this.cassandraAdminContext = cassandraAdminContext;
    }

    public CEntityDao<T> build() {
        if (this.keyspace.isEmpty()) {
            throw new RuntimeException("Error when building CEntityDao. Cause: tableName is empty");
        }
        if (this.table.isEmpty()) {
            throw new RuntimeException("Error when building CEntityDao. Cause: tableName is empty");
        }
        if (this.clazz == null) {
            throw new RuntimeException("Error when building CEntityDao. Cause: clazz is null");
        }
        if (this.keyHandler == null) {
            throw new RuntimeException("Error when building CEntityDao. Cause: key is null");
        }
        //session
        final Session session = this.cassandraAdminContext.getSession();
        //构造CassandraHandler
        CassandraHandler cassandraHandler;
        if(this.counter) {
            //counter 表
            cassandraHandler = new CassandraCounterHandlerImpl(session, this.keyspace, this.table, this.keyHandler.getColumnName(), this.columnHandlerList);
        } else {
            //普通表
            cassandraHandler = new CassandraHandlerImpl(session, this.keyspace, this.table, this.keyHandler.getColumnName(), this.columnHandlerList);
        }
        this.cassandraAdminContext.putCassandraHandler(this.clazz, cassandraHandler, this.keyspace, this.table);
        //
        //---------------------------构造根据key查询数据库entity处理对象
        InquireByKeyHandler<T> inquireByKeyHandler = new InquireByKeyFromDatabaseHandlerImpl<T>(
                cassandraHandler,
                this.clazz,
                this.columnHandlerList);
        inquireByKeyHandler = new InquireByKeyFilterHandlerImpl<T>(inquireByKeyHandler);
        
        CEntityDao<T> entityDao = new CEntityDaoImpl(
                null,
                null,
                null,
                null);
        return entityDao;
    }
}

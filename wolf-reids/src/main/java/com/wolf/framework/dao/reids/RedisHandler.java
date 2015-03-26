package com.wolf.framework.dao.reids;

import com.wolf.framework.dao.DatabaseHandler;
import com.wolf.framework.dao.condition.InquirePageContext;
import com.wolf.framework.dao.condition.InquireIndexPageContext;
import com.wolf.framework.dao.delete.DeleteHandler;
import com.wolf.framework.dao.insert.InsertHandler;
import com.wolf.framework.dao.update.UpdateHandler;
import java.util.List;

/**
 *
 * @author aladdin
 */
public interface RedisHandler extends DatabaseHandler, UpdateHandler, DeleteHandler, InsertHandler{
    
    public String META_DBINDEX = "META_DBINDEX";
    
    public String META_SEQUENCE = "META_SEQUENCE";
    
    public String SEQUENCE_DBINDEX = "DBINDEX";
    
    /**
     * 返回该表所在存放主键索引的key
     * @return 
     */
    public String getTableIndexKey();

    /**
     * 返回存放该列值索引的key
     * @param columnName
     * @param columnValue
     * @return 
     */
    public String getColumnIndexKey(String columnName, String columnValue);
    
    /**
     * 更新指定主键值在索引用的排序得分
     * @param keyValue
     * @param sorce 
     */
    public void updateKeySorce(String keyValue, long sorce);
    
    /**
     * 更新指定主键值在列值索引中的排序得分
     * @param keyValue
     * @param columnName
     * @param columnValue
     * @param sorce 
     */
    public void updateIndexKeySorce(String keyValue, String columnName, String columnValue, long sorce);

    /**
     * 分页查询主键索引,根据主键得分正序排列
     * @param inquirePageContext
     * @return 
     */
    public List<String> inquireKeys(InquirePageContext inquirePageContext);

    /**
     * 分页查询主键索引,根据主键得分倒序排列
     * @param inquirePageContext
     * @return 
     */
    public List<String> inquireKeysDESC(InquirePageContext inquirePageContext);

    /**
     * 分页查询列值索引,根据主键得分正序排列
     * @param inquireRedisIndexContext
     * @return 
     */
    public List<String> inquireKeysByIndex(InquireIndexPageContext inquireRedisIndexContext);

    /**
     * 分页查询列值索引,根据主键得分倒序排列
     * @param inquireRedisIndexContext
     * @return 
     */
    public List<String> inquireKeysByIndexDESC(InquireIndexPageContext inquireRedisIndexContext);

    /**
     * 查询某列值索引的总记录数
     * @param indexName
     * @param indexValue
     * @return 
     */
    public long countByIndex(String indexName, String indexValue);

    /**
     * 指定某行记录某个列值自增
     * @param keyValue
     * @param columnName
     * @param value
     * @return 
     */
    public long increase(String keyValue, String columnName, long value);
    
    /**
     * 指定某个sorted set的类型的扩展列,增加一个键值
     * @param keyValue
     * @param sortedSetName
     * @param value
     * @param score 
     */
    public void sortedSetAdd(String keyValue, String sortedSetName, String value, long score);
    
    /**
     * 指定某个sorted set的类型的扩展列,删除一个键值
     * @param keyValue
     * @param sortedSetName
     * @param value 
     */
    public void sortedSetRemove(String keyValue, String sortedSetName, String value);
    
    /**
     * 获取指定sorted set的类型的扩展列的所有键值,正序排列，最多取前200
     * @param keyValue
     * @param sortedSetName
     * @return 
     */
    public List<String> sortedSet(String keyValue, String sortedSetName);
    
    /**
     * 获取指定sorted set的类型的扩展列的所有键值,倒序排列，最多取前200
     * @param keyValue
     * @param sortedSetName
     * @return 
     */
    public List<String> sortedSetDESC(String keyValue, String sortedSetName);
    
    /**
     * 清除指定sorted set的类型的扩展列的所有键值
     * @param keyValue
     * @param sortedSetName 
     */
    public void sortedSetClear(String keyValue, String sortedSetName);
    
    
    /**
     * 清空表
     */
    public void truncate();
}

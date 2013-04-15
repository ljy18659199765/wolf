package com.wolf.framework.context;

import com.wolf.framework.config.FrameworkConfig;
import com.wolf.framework.dao.Entity;
import com.wolf.framework.hbase.HTableHandler;
import com.wolf.framework.hbase.HTableHandlerImpl;
import com.wolf.framework.service.Service;
import java.io.IOException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedSimpleDataSource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hbase.HBaseConfiguration;

/**
 * 全局信息构造类
 *
 * @author aladdin
 */
public final class ApplicationContextBuilder<T extends Entity, K extends Service> extends AbstractApplicationContextBuilder<T, K> {

    private final Properties properties;
    private EmbeddedSimpleDataSource embeddedSimpleDataSource = null;

    public ApplicationContextBuilder(final Properties properties) {
        this.properties = properties;
    }

    @Override
    protected String getAppPath() {
        return this.properties.getProperty("appPath");
    }

    @Override
    public void build() {
        this.baseBuild();
        ApplicationContext.CONTEXT.ready();
    }

    @Override
    protected String getCompileModel() {
        String compileModel = this.properties.getProperty("compileModel");
        if(compileModel == null) {
            compileModel = FrameworkConfig.SERVER;
        }
        return compileModel;
    }

    @Override
    protected String[] getPackageNames() {
        String packageName = this.properties.getProperty("packageName");
        String[] pack = packageName.split(",");
        return pack;
    }

    @Override
    protected HTableHandler hTableHandlerBuild() {
        String hbaseZookeeperQuorum = this.properties.getProperty("hbaseZookeeperQuorum");
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", hbaseZookeeperQuorum);
        HTableHandler hTableHandler = new HTableHandlerImpl(config);
        return hTableHandler;
    }

    @Override
    protected FileSystem fileSystemBuild() {
        String fsDefaultName = this.properties.getProperty("fsDefaultName");
        Configuration config = new Configuration();
        config.set("fs.default.name", fsDefaultName);
        FileSystem dfs;
        try {
            dfs = FileSystem.get(config);
        } catch (IOException ex) {
            this.logger.error("init hdfs file system error....see log");
            throw new RuntimeException(ex);
        }
        return dfs;
    }

    @Override
    protected DataSource dataSourceBuild() {
        DataSource dataSource;
        String dataBaseType = this.properties.getProperty("dataBaseType");
        String dataBaseName = this.properties.getProperty("dataBaseName");
        if (dataBaseType.equals("JNDI")) {
            try {
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup(dataBaseName);
        } catch (NamingException ex) {
            this.logger.error("get dataSource from JNDI error:", ex);
            throw new RuntimeException(ex);
        }
        } else {
            this.embeddedSimpleDataSource = new EmbeddedSimpleDataSource();
            this.embeddedSimpleDataSource.setDatabaseName(dataBaseName);
            this.embeddedSimpleDataSource.setCreateDatabase("create");
            dataSource = this.embeddedSimpleDataSource;
            ApplicationContext.CONTEXT.setEmbeddedSimpleDataSource(this.embeddedSimpleDataSource);
        }
        return dataSource;
    }
}

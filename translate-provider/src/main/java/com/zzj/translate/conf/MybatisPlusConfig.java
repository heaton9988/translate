//package com.zzj.translate.conf;
//
//import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
//import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
//import com.baomidou.mybatisplus.core.MybatisConfiguration;
//import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
//import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
//import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
//import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.type.JdbcType;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Configuration
//@EnableConfigurationProperties(MybatisPlusProperties.class)
//public class MybatisPlusConfig {
//
//    @Autowired
//    private MybatisPlusProperties mybatisPlusProperties;
//
//    public MybatisPlusConfig() {
//        log.info("加载:" + MybatisPlusConfig.class.getSimpleName());
//    }
//
//    /*
//     * 分页插件，自动识别数据库类型
//     * 多租户，请参考官网【插件扩展】
//     */
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//
//        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL); // 方言
//        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
//        // paginationInterceptor.setOverflow(false);
//        // 设置最大单页限制数量，-1不受限制
//        paginationInterceptor.setMaxLimit(-1L);
//        interceptor.addInnerInterceptor(paginationInterceptor);
//        return interceptor;
//    }
//
//    /**
//     * Mybatisplus兼容mybatis配置
//     *
//     * @return
//     */
//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return i -> i.setObjectWrapperFactory(new MybatisMapWrapperFactory());
//    }
//
//    @Bean(name = "dbDict")
//    @ConfigurationProperties(prefix = "spring.datasource.dbDict")
//    @Primary
//    public DataSource dbDict() {
//        log.info("加载数据源dbDict");
//        return DruidDataSourceBuilder.create().build();
//    }
//
//
//    /**
//     * 动态数据源配置
//     *
//     * @return
//     */
//    @Bean(name = "multipleDataSource")
//    public DataSource multipleDataSource(@Qualifier("dbDict") DataSource dbDict) {
//        MultipleDataSource multipleDataSource = new MultipleDataSource();
//        Map<Object, Object> targetDataSources = new HashMap<>();
//        targetDataSources.put("dbDict", dbDict);
//        //添加数据源
//        multipleDataSource.setTargetDataSources(targetDataSources);
//        //设置默认数据源
//        multipleDataSource.setDefaultTargetDataSource(dbDict);
//        return multipleDataSource;
//    }
//
//
//    /**
//     * 多数据源切换，mapper.xml 手动设计加载
//     *
//     * @return
//     * @throws Exception
//     */
//    @Bean("sqlSessionFactory")
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
//        sqlSessionFactory.setDataSource(multipleDataSource(dbDict()));
//        //解决 mybatis.mapper-locations 失效的问题
//        if (!ObjectUtils.isEmpty(this.mybatisPlusProperties.resolveMapperLocations())) {
//            sqlSessionFactory.setMapperLocations(this.mybatisPlusProperties.resolveMapperLocations());
//        }
//
//        MybatisConfiguration configuration = new MybatisConfiguration();
//        //configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
//        configuration.setJdbcTypeForNull(JdbcType.NULL);
//        configuration.setMapUnderscoreToCamelCase(true);
//        configuration.setCacheEnabled(false);
//        sqlSessionFactory.setConfiguration(configuration);
//        sqlSessionFactory.setPlugins(new Interceptor[]{ //PerformanceInterceptor(),OptimisticLockerInterceptor()
//                mybatisPlusInterceptor() //添加分页功能
//        });
//        //sqlSessionFactory.setGlobalConfig(globalConfiguration());
//        return sqlSessionFactory.getObject();
//    }
//}
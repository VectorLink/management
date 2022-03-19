package com.hair.management.generate;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.Data;

import java.io.File;
import java.util.*;

/**
 * <p>
 * 代码生成器父类
 * </p>
 *
 * @author Caratacus
 */
@Data
public class SuperGenerator {

    private boolean xml;
    private boolean controller;

    /**
     * db name 以配置文件中的多数据原db name为准
     */
    String db;
    /**
     * 获取TemplateConfig
     *
     * @return
     */
    protected TemplateConfig getTemplateConfig() {
        TemplateConfig templateConfig = new TemplateConfig().setXml(null);
        if (!controller) {
            templateConfig.setController(null);
        }
        return templateConfig;
    }

    /**
     * 获取InjectionConfig
     *
     * @return
     */
    InjectionConfig getInjectionConfig() {
        return new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                this.setMap(map);
            }
        }.setFileOutConfigList(Collections.<FileOutConfig>singletonList(new FileOutConfig(
                "/templates/mapper.xml.vm") {
            // 自定义输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
                return getResourcePath() + "/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        }));
    }

    /**
     * 获取PackageConfig
     *
     * @return
     */
    protected PackageConfig getPackageConfig() {
        return new PackageConfig()
                .setParent("com.hair.management")
                .setController("controller")
                .setEntity("dao.entity")
                .setMapper("dao")
                .setService("service")
                .setServiceImpl("service.impl");
    }

    /**
     * 获取StrategyConfig
     *
     * @param tableName
     * @return
     */
    protected StrategyConfig getStrategyConfig(String... tableName) {
        List<TableFill> tableFillList = getTableFills();
        return new StrategyConfig()
                .setCapitalMode(false)// 全局大写命名
//                .setTablePrefix("v3_")// 去除前缀
                .setNaming(NamingStrategy.underline_to_camel)// 表名生成策略
                //自定义实体父类
//                .setSuperEntityClass("org.crown.framework.model.BaseModel")
                // 自定义实体，公共字段
//                .setSuperEntityColumns("id")
                .setTableFillList(tableFillList)
                // 自定义 mapper 父类
                .setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper")
                // 自定义 controller 父类
//                .setSuperControllerClass("com.litb.core.controller.BasicController")
                // 自定义 service 实现类父类
                .setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl")
                // 自定义 service 接口父类
                .setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService")
                // 【实体】是否生成字段常量（默认 false）
                .setEntityColumnConstant(false)
                // 【实体】是否为构建者模型（默认 false）
                .setEntityBuilderModel(false)
                // 【实体】是否为lombok模型（默认 false）<a href="https://projectlombok.org/">document</a>
                .setEntityLombokModel(true)
                // Boolean类型字段是否移除is前缀处理
                .setEntityBooleanColumnRemoveIsPrefix(false)
                .setRestControllerStyle(true)
                .setInclude(tableName);

    }

    /**
     * 获取TableFill策略
     *
     * @return
     */
    protected List<TableFill> getTableFills() {
        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(new TableFill("createTime", FieldFill.INSERT));
        tableFillList.add(new TableFill("updateTime", FieldFill.INSERT_UPDATE));
        tableFillList.add(new TableFill("createUid", FieldFill.INSERT));
        tableFillList.add(new TableFill("updateUid", FieldFill.INSERT_UPDATE));
        return tableFillList;
    }

    /**
     * 获取DataSourceConfig
     *
     * @return
     */
    protected DataSourceConfig getDataSourceConfig() {
        return new DataSourceConfig()
                .setDbType(DbType.MYSQL)// 数据库类型
                .setTypeConvert(new MySqlTypeConvert() {
                    @Override
                    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                        if (fieldType.toLowerCase().equals("bit")) {
                            return DbColumnType.BOOLEAN;
                        }
                        if (fieldType.toLowerCase().equals("tinyint")) {
                            return DbColumnType.BOOLEAN;
                        }
                        if (fieldType.toLowerCase().equals("date")) {
                            return DbColumnType.LOCAL_DATE;
                        }
                        if (fieldType.toLowerCase().equals("time")) {
                            return DbColumnType.LOCAL_TIME;
                        }
                        if (fieldType.toLowerCase().equals("datetime")) {
                            return DbColumnType.LOCAL_DATE_TIME;
                        }
                        return super.processTypeConvert(globalConfig, fieldType);
                    }
                })
                .setDriverName("com.mysql.jdbc.Driver")
                .setUsername(com.hair.management.generate.YmlConfig.getValue("spring.datasource.druid.username"))
                .setPassword(com.hair.management.generate.YmlConfig.getValue("spring.datasource.druid.password"))
                .setUrl(com.hair.management.generate.YmlConfig.getValue("spring.datasource.druid.url"));
    }

    /**
     * 获取GlobalConfig
     *
     * @return
     */
    protected GlobalConfig getGlobalConfig() {
        return new GlobalConfig()
                .setOutputDir(getJavaPath())//输出目录
                .setFileOverride(false)// 是否覆盖文件
                .setActiveRecord(false)// 开启 activeRecord 模式
                .setEnableCache(false)// XML 二级缓存
                .setBaseResultMap(false)// XML ResultMap
                .setBaseColumnList(false)// XML columList
                .setKotlin(false) //是否生成 kotlin 代码
                .setOpen(false)
//                .setAuthor("zoukai") //作者
                //自定义文件命名，注意 %s 会自动填充表实体属性！
                .setEntityName("%s")
                .setMapperName("%sMapper")
                .setXmlName("%sMapper")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sRestController");
    }


    /**
     * 获取根目录
     *
     * @return
     */
    private String getRootPath() {
        String file = Objects.requireNonNull(SuperGenerator.class.getClassLoader().getResource("")).getFile();
        return new File(file).getParentFile().getParent();
    }

    /**
     * 获取JAVA目录
     *
     * @return
     */
    protected String getJavaPath() {
        String javaPath = getRootPath() + "/src/main/java";
        System.err.println(" Generator Java Path:【 " + javaPath + " 】");
        return javaPath;
    }

    /**
     * 获取Resource目录
     *
     * @return
     */
    protected String getResourcePath() {
        String resourcePath = getRootPath() + "/src/main/resources";
        System.err.println(" Generator Resource Path:【 " + resourcePath + " 】");
        return resourcePath;
    }

    /**
     * 获取AutoGenerator
     *
     * @param tableName
     * @return
     */
    protected AutoGenerator getAutoGenerator(String... tableName) {
        AutoGenerator autoGenerator = new AutoGenerator()
                // 全局配置
                .setGlobalConfig(getGlobalConfig())
                // 数据源配置
                .setDataSource(getDataSourceConfig())
                // 策略配置
                .setStrategy(getStrategyConfig(tableName))
                // 包配置
                .setPackageInfo(getPackageConfig())
                // 模版配置
                .setTemplate(getTemplateConfig());
        if (xml) {
            // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
            autoGenerator.setCfg(getInjectionConfig());
        }
        return autoGenerator;
    }

}

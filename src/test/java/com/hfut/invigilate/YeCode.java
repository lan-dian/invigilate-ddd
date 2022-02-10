package com.hfut.invigilate;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

public class YeCode {
    public static void main(String[] args) {
        AutoGenerator mg =  new AutoGenerator();
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath+"/src/main/java");
        gc.setAuthor("常珂洁");
        gc.setOpen(false);
        gc.setSwagger2(true);
        mg.setGlobalConfig(gc);
        DataSourceConfig dsc = new DataSourceConfig();
        //数据库表名
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/invigilate?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mg.setDataSource(dsc);
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.hfut.invigilate");//包名
        mg.setPackageInfo(pc);
        InjectionConfig cfg = new InjectionConfig() {
            public void initMap() {

            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mg.setCfg(cfg);
        mg.setTemplate(new TemplateConfig().setXml(null));
        StrategyConfig sc = new StrategyConfig();
        //表名称
        sc.setInclude("request_log","config","department","exam","exchange","exchange_record","invigilate","request_log","user","user_department","user_role");
        sc.setNaming(NamingStrategy.underline_to_camel);
        sc.setColumnNaming(NamingStrategy.underline_to_camel);
        sc.setEntityLombokModel(true);
        sc.setLogicDeleteFieldName("deleted");
        sc.setRestControllerStyle(true);
        sc.setChainModel(true);
        sc.setEntityBooleanColumnRemoveIsPrefix(true);
        TableFill create_time = new TableFill("create_time", FieldFill.INSERT);
        TableFill update_time = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(create_time);
        tableFills.add(update_time);
        sc.setTableFillList(tableFills);
//        sc.setVersionFieldName("version");
        sc.setRestControllerStyle(true);
        sc.setControllerMappingHyphenStyle(true);
        mg.setStrategy(sc);
        mg.execute();
    }
}

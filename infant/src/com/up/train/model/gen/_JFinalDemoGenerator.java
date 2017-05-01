package com.up.train.model.gen;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.c3p0.C3p0Plugin;

import javax.sql.DataSource;

/**
 * 在数据库表有任何变动时，运行一下 main 方法，极速响应变化进行代码重构
 */
public class _JFinalDemoGenerator {
    public static DataSource getDataSource() {
//        String jdbc = "jdbc:mysql://172.0.0.1:3306/infant?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
        String jdbc = "jdbc:mysql://localhost:3306/train?useUnicode=true&characterEncoding=utf8&autoReconnect=true";
        String user = "root";
        String password = "123";
        C3p0Plugin c3p0Plugin = new C3p0Plugin(jdbc, user, password);
        c3p0Plugin.start();
        return c3p0Plugin.getDataSource();
    }

    /**
     * 自动生成model
     *
     * @param args
     */
    public static void main(String[] args) {
//		 base model 所使用的包名
        String baseModelPackageName = "com.up.train.model.base";
        // base model 文件保存路径
        String baseModelOutputDir = PathKit.getWebRootPath() + "/../src/com/up/train/model/base";

        // model 所使用的包名 (MappingKit 默认使用的包名)
        String modelPackageName = "com.up.train.model";
        // model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
        String modelOutputDir = baseModelOutputDir + "/..";

        // 创建生成器
        Generator gernerator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
        // 添加不需要生成的表名
        gernerator.addExcludedTable("adv");
        // 设置是否在 Model 中生成 dao 对象
        gernerator.setGenerateDaoInModel(true);
        // 设置是否生成字典文件
        gernerator.setGenerateDataDictionary(true);
        // 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
        gernerator.setRemovedTableNamePrefixes("t_");
        // 生成
        gernerator.generate();
//        PropKit.use("a_little_config");
//        String json=PropKit.get("JSON");
//        List<Exp> expList=getExpList(json);
//        Db.batchSave(expList,expList.size());
    }

//    public static List<Exp> getExpList(String json){
//        LogisticsBean logisticsBean=new Gson().fromJson(json,LogisticsBean.class);
//        List<LogisticsData> list=logisticsBean.getShowapi_res_body().getExpressList();
//        List<Exp> exps=new ArrayList<Exp>();
//        for (int i=0;i<list.size();i++){
//            Exp exp=new Exp();
//            LogisticsData data=list.get(i);
//            exp.setImgUrl(data.getImgUrl());
//            exp.setSimpleName(data.getSimpleName());
//            exp.setPhone(data.getPhone());
//            exp.setExpName(data.getExpName());
//            exp.setUrl(data.getUrl());
//            exp.setNote(data.getNote());
//            exps.add(exp);
//        }
//        return exps;
//
//    }


}





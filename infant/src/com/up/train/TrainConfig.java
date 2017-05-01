package com.up.train;

import com.jfinal.config.*;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;
import com.up.common.controller.ErrorController;
import com.up.infant.controller.ewm.EwmController;
import com.up.train.model._MappingKit;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/18 0018. 20:47
 */
public class TrainConfig extends JFinalConfig{

    private final Logger logger = Logger.getLogger(TrainConfig.class);

    /**
     * 配置常量
     * @param me
     */
    public void configConstant(Constants me) {
        me.setEncoding("utf-8");
        // 加载少量必要配置，随后可用PropKit.get(...)获取值
        //配置log4j

        PropKit.use("db_config_train");
        boolean dev=PropKit.getBoolean("devMode", new Boolean(false)).booleanValue();
        me.setDevMode(dev);
        me.setError401View("/common/error.jsp");
        me.setError404View("/common/error.jsp");
        me.setError500View("/common/error.jsp");
        me.setError403View("/common/error.jsp");
        me.setViewType(ViewType.JSP);

        PropertyConfigurator.configure(PropKit.use("log4j").getProperties());
    }


    /**
     * 配置路由
     * @param me
     */
    public void configRoute(Routes me) {
        me.add(new TrainRotes());
        me.add("common", ErrorController.class);
        me.add("ewm", EwmController.class);

    }

    /**
     * 配置插件
     * @param me
     */
    public void configPlugin(Plugins me) {
        C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
//        C3p0Plugin c3p0Plugin = createC3p0Plugin();
        me.add(c3p0Plugin);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
        me.add(arp);
        _MappingKit.mapping(arp);

    }

    /**
     * 配置全局拦截器
     * @param me
     */
    public void configInterceptor(Interceptors me) {
//        me.add(new AuthInterceptors());
    }

    /**
     * 配置处理器
     * @param me
     */
    public void configHandler(Handlers me) {

    }

    /**
     * 项目启动成功
     */
    public void afterJFinalStart() {
        super.afterJFinalStart();
        logger.info("项目启动成功");
    }

    /**
     * 项目关闭之前
     */
    public void beforeJFinalStop() {
        super.beforeJFinalStop();
        logger.info("服务关闭");
    }


    public static C3p0Plugin createC3p0Plugin() {
        C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
        return c3p0Plugin;
    }
}

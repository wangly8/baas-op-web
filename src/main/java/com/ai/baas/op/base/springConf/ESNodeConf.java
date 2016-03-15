package com.ai.baas.op.base.springConf;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.ai.baas.op.base.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jackieliu on 16/2/24.
 */
@Configuration
@PropertySource("classpath:/context/es-config.properties")
public class ESNodeConf {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${esserver.cluster.name}")
    private String clusterName;

    @Value("${esserver.path.home}")
    private String pathHome;

    @Value("${esserver.path.data}")
    private String pathData;

    @Value("${esserver.path.plugins}")
    private String pluginsPath;

    /**
     * 创建索引节点
     * @return
     */
    @Bean(name = "esNode",destroyMethod = "stop")
    public Node buileNode(){
        String esHome = System.getProperty("es.home.path");
        if (StringUtil.isBlank(esHome))
            esHome = this.getClass().getClassLoader().getResource("").getPath()+pathHome;

        // 设置setting
        Map<String, String> settingMap = new HashMap<String, String>();
        //集群名称
        settingMap.put("cluster.name", clusterName);
        //es根目录
        settingMap.put("path.home", esHome);
        if (!StringUtil.isBlank(pathData))
            settingMap.put("path.data", pathData);//es数据存放路径
        if (!StringUtil.isBlank(pluginsPath))
            settingMap.put("path.plugins",pluginsPath);//es插件目录

        Settings settings = ImmutableSettings.settingsBuilder().put(settingMap)
                .build();
        // 创建并启动节点
        NodeBuilder nodeBuilder = NodeBuilder.nodeBuilder();
        nodeBuilder.settings(settings);
        //创建并启动节点
        return nodeBuilder.node();
        //仅用来创建节点
//        return nodeBuilder.build();
    }

    @Bean(name="esClient")
    public Client genClient(){
        return buileNode().client();
    }

}

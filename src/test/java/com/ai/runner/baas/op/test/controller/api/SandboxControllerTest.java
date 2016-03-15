package com.ai.runner.baas.op.test.controller.api;

import com.ai.baas.op.api.sandbox.param.APICallSetting;
import com.ai.baas.op.api.sandbox.param.APICallSettingReq;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackieliu on 16/3/1.
 */
public class SandboxControllerTest {
    private RestTemplate restTemplate = new RestTemplate();
    private String serverUrl = "http://localhost:8080/optstester";

    @Test
    public void checkRegistryAvailable(){
        String actionUrl = serverUrl+"/sandbox/checkRegistryAvailable";
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("registryURL","zookeeper://127.0.0.1:2181");

        ResponseEntity<String> re = restTemplate.postForEntity(actionUrl,map,String.class);
        System.out.println("statu code:"+re.getStatusCode()+"\r\nresponse body:"+re.getBody());
    }

    @Test
    public void saveAPICallSetting(){
        String uploadUrl = serverUrl +"/sandbox/saveAPICallSetting";
        APICallSetting callSetting = new APICallSetting();
        callSetting.setAuthor("pength");
        callSetting.setBriefComment("商品销售查询统计接口-按商品统计");
        callSetting.setDetailComment("商品销售查询统计接口-按商品统计");
        callSetting.setEditorType("1");
        callSetting.setFirstSetting(false);
        callSetting.setIndexId(-1050622691);
        callSetting.setInterfaceName("com.ai.runner.center.product.api.productsale.interfaces.IProductSaleSV");
        callSetting.setMethod("productSaleQryById");
        callSetting.setOwner("runner-product-center");
        callSetting.setOwnerType("runner-center");
        callSetting.setPublishDate("2016-01-20 15:29:52");
        callSetting.setReturnComment("");
        callSetting.setReturnJavaType("java.util.List");
        callSetting.setReturnJson("{}");
        APICallSettingReq settingReq = new APICallSettingReq();
        settingReq.setEditorType("1");
        settingReq.setInterfaceName("com.ai.runner.center.product.api.productsale.interfaces.IProductSaleSV");
        settingReq.setJavaType("com.ai.runner.center.product.api.productsale.param.ProductSaleByIdQryVo");
        settingReq.setMethod("productSaleQryById");
        settingReq.setParamComment("");
        settingReq.setParamName("vo");
        settingReq.setSort(0);
        settingReq.setJson("{\"tenantId\":\"CLC-BYD\",\"productId\":1001375,\"productName\":\"最后一把测试123\",\"channelId\":\"402\"}");
        List<APICallSettingReq> settingReqList = new ArrayList<>();
        settingReqList.add(settingReq);
        callSetting.setReqSettings(settingReqList);
        String data = JSONObject.toJSONString(callSetting);
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("data",data);

        ResponseEntity<String> re = restTemplate.postForEntity(uploadUrl,map,String.class);
        System.out.println("statu code:"+re.getStatusCode()+"\r\nresponse body:"+re.getBody());
        /*Map<String, Object> re = restTemplate.postForObject(uploadUrl,map,Map.class);
        for(Map.Entry<String, Object> entry: re.entrySet()){
            System.out.println(entry.getKey() +":"+entry.getValue());
        }*/
    }
}

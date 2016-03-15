package com.ai.runner.baas.op.test.controller.api;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by jackieliu on 16/2/26.
 */
public class APIControllerTest {
    private RestTemplate restTemplate = new RestTemplate();
    private String serverUrl = "http://localhost:8080/runnerstest";


    @Test
    public void uploadIndexDataFile(){
        String uploadUrl = serverUrl +"/api/indexdata/upload";
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//        FileSystemResource resource = new FileSystemResource(new File("F:\\icfrontfile.jpg"));
        Resource resource = new ClassPathResource("runner-common-center_apis_data.json");
        map.add("dataFile",resource);

        ResponseEntity<String> re = restTemplate.postForEntity(uploadUrl,map,String.class);
        System.out.println("statu code:"+re.getStatusCode()+"\r\nresponse body:"+re.getBody());
        /*Map<String, Object> re = restTemplate.postForObject(uploadUrl,map,Map.class);
        for(Map.Entry<String, Object> entry: re.entrySet()){
            System.out.println(entry.getKey() +":"+entry.getValue());
        }*/
    }
}

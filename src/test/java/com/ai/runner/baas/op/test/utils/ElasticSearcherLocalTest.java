package com.ai.runner.baas.op.test.utils;

import junit.framework.Assert;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.baas.op.constants.ElasticIndex;
import com.ai.baas.op.constants.ElasticType;
import com.ai.baas.op.util.ElasticSearchUtil;
import com.ai.baas.op.util.ElasticSearcherLocal;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * Created by jackieliu on 16/2/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:context/core-context.xml"})
public class ElasticSearcherLocalTest {

//    @Test
    public void optIndexByElasticIndex(){
        int pageNo = 0,pageSize = 1;
        ElasticIndex esIndex = ElasticIndex.SONAR;
        ElasticType esType = ElasticType.DATA;
        ElasticSearcherLocal es = ElasticSearchUtil.getElasticSearcher();
        long time = System.currentTimeMillis();
        String esId1 = "tenantIdes"+(time+1),esId2 = "tenantIdes"+(time+1);

        //添加
        String source = "{\"pageNo\": 5,\"pageSize\": 6,\"tenantId\": \"BIS-ST000";
        es.addIndex(esIndex, esType, esId1,source+1+"\"}");
        es.addIndex(esIndex, esType, esId1,source+2+"\"}");
        SearchResponse response = queryES(esIndex,esType,"BIS-ST000");
        Assert.assertEquals(2,response.getHits().totalHits());

        //更新
        String upSource = source+5+"\"}";
        es.updateIndex(esIndex,esType,esId1,upSource);
        String qSource = getSourceById(esIndex,esType,esId1);
        Assert.assertEquals(upSource,qSource);

        //删除
        es.deleteIndex(esIndex,esType,esId1);
        es.deleteIndex(esIndex,esType,esId2);
        response = queryES(esIndex,esType,"BIS-ST000");
        Assert.assertEquals(0,response.getHits().totalHits());
    }

    private SearchResponse queryES(ElasticIndex elasticIndex,ElasticType elasticType,String tenantId){
        QueryBuilder queryBuilder = QueryBuilders.matchPhrasePrefixQuery("tenantId",tenantId);
        Client client = ElasticSearchUtil.getElasticSearcher().getClient(elasticIndex);
        return client.prepareSearch(elasticIndex.name().toLowerCase()).setTypes(elasticType.name().toLowerCase())
                .setQuery(queryBuilder)
                .execute()
                .actionGet();
    }

    private String getSourceById(ElasticIndex elasticIndex,ElasticType elasticType,String DetailIndexId) {
        Client client = ElasticSearchUtil.getElasticSearcher().getClient(ElasticIndex.API);
        SearchResponse response = client.prepareSearch(elasticIndex.name().toLowerCase())
                .setTypes(elasticType.name().toLowerCase())
                .setQuery(termQuery("_id", DetailIndexId)).execute().actionGet();
        if (response.getHits().getTotalHits() == 0) {
            return null;
        }
        return response.getHits().getHits()[0].getSourceAsString();
    }
}

package com.ai.baas.op.util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.node.Node;

import com.ai.baas.op.constants.ElasticIndex;
import com.ai.baas.op.constants.ElasticType;

public class ElasticSearcherLocal {

	private Node node;

    private static Map<String, Client> clients = new ConcurrentHashMap<String, Client>();
    
    public Client getClient(ElasticIndex elasticIndex) {
        return getClient(elasticIndex.name());
    }

    public Client getClient(String elasticIndex) {
        if (clients.containsKey(elasticIndex)) {
            return clients.get(elasticIndex);
        }
        //采用本地node方式,离线模式使用本地模式
        Client client = node.client();

        if (!client.admin().indices().prepareExists(elasticIndex.toLowerCase()).execute()
                .actionGet().isExists()) {
            client.admin().indices().prepareCreate(elasticIndex.toLowerCase()).execute()
                    .actionGet();
        }
        XContentBuilder apiNewVerMapping = null;
        try {
            //设置api_version_new中的owner和ownerType不进行分词
            apiNewVerMapping = XContentFactory.jsonBuilder().startObject()
                    .startObject(ElasticType.API_VERSION_NEW.name().toLowerCase()).startObject("properties")
                    .startObject("owner").field("type","string").field("index","not_analyzed").endObject()
                    .startObject("ownerType").field("type","string").field("index","not_analyzed").endObject()
                    .endObject().endObject().endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PutMappingRequest mappingRequest = Requests.putMappingRequest(elasticIndex.toLowerCase())
                .type(ElasticType.API_VERSION_NEW.name().toLowerCase()).source(apiNewVerMapping);
        client.admin().indices().putMapping(mappingRequest).actionGet();
        clients.put(elasticIndex, client);
        return client;

    }
	
    public void setNode(Node node) {
		this.node = node;
	}
    
    public boolean addIndex(ElasticIndex index, ElasticType type, String id, String source) {
        IndexResponse response = getClient(index)
                .prepareIndex(index.name().toLowerCase(), type.name().toLowerCase(), id)
                .setRefresh(true).setSource(source).execute().actionGet();
        return response.isCreated();
    }

    public boolean updateIndex(ElasticIndex index, ElasticType type, String id, String source) {
        UpdateResponse response = getClient(index)
                .prepareUpdate(index.name().toLowerCase(), type.name().toLowerCase(), id)
                .setDoc(source).execute().actionGet();
        return response.isCreated();
    }

    public boolean deleteIndex(ElasticIndex index, ElasticType type, String id) {
        DeleteResponse response = getClient(index)
                .prepareDelete(index.name().toLowerCase(), type.name().toLowerCase(), id).execute()
                .actionGet();
        return response.isFound();
    }

    public boolean addIndex(String index, String type, String id, String source) {
        IndexResponse response = getClient(index)
                .prepareIndex(index.toLowerCase(), type.toLowerCase(), id).setRefresh(true)
                .setSource(source).execute().actionGet();
        return response.isCreated();
    }

    public boolean updateIndex(String index, String type, String id, String source) {
        UpdateResponse response = getClient(index)
                .prepareUpdate(index.toLowerCase(), type.toLowerCase(), id).setDoc(source)
                .execute().actionGet();
        return response.isCreated();
    }

    public boolean deleteIndex(String index, String type, String id) {
        DeleteResponse response = getClient(index)
                .prepareDelete(index.toLowerCase(), type.toLowerCase(), id).execute().actionGet();
        return response.isFound();
    }
    
}

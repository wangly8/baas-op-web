package com.ai.baas.op.util;

import java.util.Map;

import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.highlight.HighlightField;

public final class ElasticSearchUtil {

    private static ElasticSearcherLocal searcher;

    /**
     * 获取搜索器
     * 
     * @return
     * @author zhangchao
     */
    public static ElasticSearcherLocal getElasticSearcher() {
        return searcher;
    }

    private ElasticSearchUtil() {
    }
    
    public static void setSearcher(ElasticSearcherLocal searcher) {
		ElasticSearchUtil.searcher = searcher;
	}

	public static String getHightLightFieldValue(Map<String, HighlightField> m, String fieldName) {
        if (m == null || fieldName == null || !m.containsKey(fieldName)) {
            return null;
        }
        HighlightField f = m.get(fieldName);
        Text[] titleTexts = f.fragments();
        String name = "";
        for (Text text : titleTexts) {
            name += text;
        }
        return name;
    }
}

package com.ai.baas.op.controller.api;

import com.ai.baas.op.api.apisearch.IAPISearchSV;
import com.ai.baas.op.api.apisearch.param.APIOwnerType;
import com.ai.baas.op.api.apisearch.param.APISearchKey;
import com.ai.baas.op.api.apisearch.param.APISearchResult;
import com.ai.baas.op.base.exception.SystemException;
import com.ai.baas.op.base.model.ResponseData;
import com.ai.baas.op.base.util.StringUtil;
import com.ai.baas.op.constants.ElasticIndex;
import com.ai.baas.op.constants.ElasticType;
import com.ai.baas.op.util.ElasticSearchUtil;
import com.ai.runner.apicollector.util.JavaDocletUtil;
import com.ai.runner.apicollector.vo.APIClassDoc;
import com.ai.runner.apicollector.vo.APIClassFieldDoc;
import com.ai.runner.apicollector.vo.APIDoc;
import com.ai.runner.base.vo.PageInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.lucene.queryparser.flexible.core.util.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.util.Iterator;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@RestController
@RequestMapping("/api")
public class APIController {

    private static final Logger LOG = LoggerFactory.getLogger(APIController.class);

    @Autowired
    IAPISearchSV iapiSearchSV;

    /**
     * 显示首页
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("api/index");
    }
    
    
    @RequestMapping("/page")
    public ModelAndView page(Model uiModel,HttpServletRequest request) {
//        List<APIOwnerType> apiOwnerTypes = iapiSearchSV.getAPIOwnerTypes();
//        uiModel.addAttribute("apiOwnerTypes", apiOwnerTypes);
    	String id=request.getParameter("id");
    	if(null==id)
    	{
    		id="1";
    	}
    	request.setAttribute("currentPage", id);
        ModelAndView view = new ModelAndView("api/page");
        return view;
    }

    /**
     * 显示帮助页面
     * @return
     */
    @RequestMapping("/help.html")
    public ModelAndView helpView(){
        return new ModelAndView("api/help");
    }

    /**
     * 获取apiOwnerTypes数据
     * @return
     */
    @RequestMapping("/apiownertypes")
    public ResponseData<List<APIOwnerType>> getApiOwnerTypes(){
        ResponseData<List<APIOwnerType>> responseData;
        try{
            List<APIOwnerType> apiOwnerTypes = iapiSearchSV.getAPIOwnerTypes();
            responseData = new ResponseData<List<APIOwnerType>>(ResponseData.AJAX_STATUS_SUCCESS,
                    "服务数据查询成功", apiOwnerTypes);
        }catch (Exception e){
            LOG.error("服务数据查询失败",e);
            responseData = new ResponseData<List<APIOwnerType>>(ResponseData.AJAX_STATUS_FAILURE,
                    "服务数据查询失败:" + e.getMessage());
        }
        return responseData;
    }

    /**
     * 显示搜索页面
     * @param request
     * @return
     */
    @RequestMapping("/tosearch.html")
    public ModelAndView tosearch(HttpServletRequest request) {
        String owner = request.getParameter("owner");
        String ownerType = request.getParameter("ownerType");
        String keywords = request.getParameter("keywords");
        String limitException = request.getParameter("limitException");
        String allowDelete = request.getParameter("allowDelete");
        ModelAndView view = new ModelAndView("api/search");
        view.addObject("owner", owner).addObject("ownerType", ownerType)
                .addObject("keywords", keywords).addObject("limitException", limitException)
                .addObject("allowDelete", allowDelete);
        return view;
    }

    /**
     * 进行搜索
     * @param queryCond
     * @return
     */
    @RequestMapping("/search")
    public ResponseData<PageInfo<APISearchResult>> search(String queryCond) {
        ResponseData<PageInfo<APISearchResult>> responseData = null;
        try {
            if (StringUtil.isBlank(queryCond)) {
                throw new SystemException("查询条件为空");
            }
            /* 转换成被测试数据对象 */
            APISearchKey vo = JSON.parseObject(queryCond, APISearchKey.class);
            if (vo == null) {
                throw new SystemException("查询条件为空");
            }
            PageInfo<APISearchResult> result = iapiSearchSV.searchAPIDocs(vo);
            responseData = new ResponseData<PageInfo<APISearchResult>>(
                    ResponseData.AJAX_STATUS_SUCCESS, "查询成功", result);
        } catch (Exception e) {
            responseData = new ResponseData<PageInfo<APISearchResult>>(
                    ResponseData.AJAX_STATUS_FAILURE, "查询失败:" + e.getMessage());
        }
        return responseData;
    }

    /**
     * 搜索并显示指定owner,指定interfaceName,指定methodName,指定版本的详细信息
     * @param request
     * @return
     */
    @RequestMapping("/apidetail.html")
    public ModelAndView apidetail(HttpServletRequest request) {
        String owner = request.getParameter("owner");
        String interfaceName = request.getParameter("interfaceName");
        String methodName = request.getParameter("methodName");
        String version = request.getParameter("version");
        if (StringUtil.isBlank(owner)) {
            throw new SystemException("API提供者不能为空");
        }
        if (StringUtil.isBlank(interfaceName)) {
            throw new SystemException("API接口类不能为空");
        }
        if (StringUtil.isBlank(methodName)) {
            throw new SystemException("API方法不能为空");
        }
//        if (StringUtil.isBlank(version)) {
//            throw new SystemException("API版本不能为空");
//        }
        int id = JavaDocletUtil.getAPIHashCode(interfaceName, methodName);
        String data = iapiSearchSV.getAPIVersionNew(StringUtil.toString(id));
        if (StringUtil.isBlank(data)) {
            throw new SystemException("API的这个版本没有找到，请先导入相关数据");
        }
        APIDoc apiDoc = JSONObject.parseObject(data, APIDoc.class);
        request.setAttribute("apiDoc", apiDoc);
        ModelAndView view = new ModelAndView("api/apidetail");
        return view;
    }

    /**
     * 检索并显示指定索引id的api信息
     * @param request
     * @return
     */
    @RequestMapping("/getAPIVersionNewDetail.html")
    public ModelAndView getAPIVersionNewDetail(HttpServletRequest request) {
        String indexId = request.getParameter("indexId");
        if (StringUtil.isBlank(indexId)) {
            throw new SystemException("缺少API的索引信息");
        }
        String data = iapiSearchSV.getAPIVersionNew(indexId);
        if (StringUtil.isBlank(data)) {
            throw new SystemException("API的这个版本没有发布，请联系服务负责人先发布");
        }
        APIDoc apiDoc = JSONObject.parseObject(data, APIDoc.class);
        request.setAttribute("apiDoc", apiDoc);
        ModelAndView view = new ModelAndView("api/apidetail");
        return view;
    }

    /**
     * 获取指定索引id的参数类详细信息
     * @param pIndexId
     * @return
     */
    @RequestMapping("/getSubClassFields")
    public ResponseData<List<APIClassFieldDoc>> getSubClassFields(String pIndexId) {
        ResponseData<List<APIClassFieldDoc>> responseData = null;
        try {
            if (StringUtil.isBlank(pIndexId)) {
                throw new SystemException("获取子属性错误，缺少上级属性索引ID");
            }
            String data = iapiSearchSV.getAPIClassDetail(pIndexId);
            if (StringUtil.isBlank(data)) {
                throw new SystemException("获取子属性错误，缺少索引数据");
            }
            APIClassDoc classDoc = JSONObject.parseObject(data, APIClassDoc.class);
            responseData = new ResponseData<List<APIClassFieldDoc>>(
                    ResponseData.AJAX_STATUS_SUCCESS, "查询成功", classDoc.getClassFields());
        } catch (Exception e) {
            responseData = new ResponseData<List<APIClassFieldDoc>>(
                    ResponseData.AJAX_STATUS_FAILURE, "查询失败:" + e.getMessage());
        }
        return responseData;
    }

    /**
     * 上传索引数据文件
     * @Autowired Liu Tong
     * @return
     */
    @RequestMapping("/indexdata/upload")
    public ResponseData<String> uploadIndexDataFile(@RequestParam("dataFile") MultipartFile file){
        ResponseData<String> responseData = null;
        try {
            if (file == null || file.isEmpty())
                throw new SystemException("文件为空");
            JSONObject allObject = (JSONObject)JSON.parse(file.getBytes());
            JSONArray apiVerNew = allObject.getJSONArray("apiVersionNews");
            JSONArray classDetail = allObject.getJSONArray("apiClassDetails");
            JSONArray apiTemplates = allObject.getJSONArray("apiTemplates");
            //添加api的索引
            addIndexOfJson(apiVerNew,ElasticType.API_VERSION_NEW);
            //添加类别规范索引
            addIndexOfJson(classDetail,ElasticType.CLASS_DETAIL);
            //添加参数模板索引
            Iterator<Object> iterator = apiTemplates.iterator();
            while (iterator.hasNext()){
                JSONObject jsonObject = (JSONObject) iterator.next();
                int id = (jsonObject.getString("owner")+"."+jsonObject.getString("interfaceName")+"."+jsonObject.getString("method")).hashCode();
                jsonObject.put("id",id);
                ElasticSearchUtil.getElasticSearcher().addIndex(
                        ElasticIndex.API, ElasticType.API_TEMPLATES, StringUtils.toString(id),jsonObject.toJSONString());
            }
//            System.out.println(allObject.toString());
            if(LOG.isDebugEnabled()){
                LOG.debug("索引文件导入成功,导入API[{}]条,参数类[{}]条,参数模板[{}]条",apiVerNew.size(),classDetail.size(),apiTemplates.size());
            }
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "数据导入成功", null);
        } catch (Exception e) {
            LOG.error("索引文件导入失败,",e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "数据导入失败:"
                    + e.getMessage());
        }
        return responseData;
    }

    private void addIndexOfJson(JSONArray jsonArray,ElasticType elasticType){
        Iterator<Object> iterator = jsonArray.iterator();
        while (iterator.hasNext()){
            JSONObject jsonObject = (JSONObject) iterator.next();
            String id = jsonObject.getInteger("id").toString();
            ElasticSearchUtil.getElasticSearcher().addIndex(
                    ElasticIndex.API, elasticType,id,jsonObject.toJSONString());
        }
    }

    /**
     * 查询最新版API数据,默认为第一页的数据,每页显示10条
     * @param isFilter 是否需要过滤指定条件
     * @return
     */
    @RequestMapping(value = "/api_version_new/query",produces={"application/json;charset=UTF-8"})
    @ResponseBody
    public String queryApi(
            @RequestParam(value="isFilter",required = false) boolean isFilter
    ){
        Client client = ElasticSearchUtil.getElasticSearcher().getClient(ElasticIndex.API);
        BoolQueryBuilder builder = boolQuery();
        if (isFilter) {
            builder.must(termQuery("ownerType", "runner-center"));
            builder.must(termQuery("interfaceName","com.ai.runner.center.common.api.staff.interfaces.IGnStaffQuerySV".toLowerCase()));
            builder.must(termQuery("owner", "runner-common-center"));
            //success
//            builder.must(termQuery("author","gaogang"));
        }

        SearchResponse response = client.prepareSearch(ElasticIndex.API.name().toLowerCase())
                .setTypes(ElasticType.API_VERSION_NEW.name().toLowerCase()).setQuery(builder)
                .addHighlightedField("apiCode").addHighlightedField("interfaceName")
                .addHighlightedField("methodName").addHighlightedField("owner")
                .addHighlightedField("groupId").addHighlightedField("artifactId")
                .addHighlightedField("briefComment").addHighlightedField("detailComment")
                .addHighlightedField("author").addHighlightedField("restRelativeURL")
                .setHighlighterPreTags("<span style=\"color:red\"><b>")
                .setHighlighterPostTags("</b></span>")
                .setFrom(0).setSize(10).execute().actionGet();

        return response.toString();
    }

    /**
     * 查询类别规范
     * @return
     */
    @RequestMapping(value = "/class_detail/query",produces={"application/json;charset=UTF-8"})
    public String queryClassDetail(){
        Client client = ElasticSearchUtil.getElasticSearcher().getClient(ElasticIndex.API);
        BoolQueryBuilder builder = boolQuery();

        SearchResponse response = client.prepareSearch(ElasticIndex.API.name().toLowerCase())
                .setTypes(ElasticType.CLASS_DETAIL.name().toLowerCase()).setQuery(builder)
                .setFrom(0).setSize(10).execute().actionGet();

        return response.toString();
    }

    /**
     * 查询参数模板
     * @return
     */
    @RequestMapping(value = "/api_templates/query",produces={"application/json;charset=UTF-8"})
    public String queryAPITemplates(){
        Client client = ElasticSearchUtil.getElasticSearcher().getClient(ElasticIndex.API);
        BoolQueryBuilder builder = boolQuery();

        SearchResponse response = client.prepareSearch(ElasticIndex.API.name().toLowerCase())
                .setTypes(ElasticType.API_TEMPLATES.name().toLowerCase()).setQuery(builder)
                .setFrom(0).setSize(10).execute().actionGet();
        return response.toString();
    }


}

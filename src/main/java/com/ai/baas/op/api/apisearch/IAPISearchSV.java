package com.ai.baas.op.api.apisearch;

import com.ai.baas.op.api.apisearch.param.APIOwnerType;
import com.ai.baas.op.api.apisearch.param.APISearchKey;
import com.ai.baas.op.api.apisearch.param.APISearchResult;
import com.ai.runner.base.exception.CallerException;
import com.ai.runner.base.vo.PageInfo;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.List;

@Path("APISearch")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IAPISearchSV {

    /**
     * 获取在线网站API的信息
     * 
     * @return
     * @author zhangchao
     * @ApiDocMethod
     */
    String getAPIStatistics() throws CallerException;

    /**
     * 获取所有服务的归属类型列表
     * 
     * @return
     * @author zhangchao
     * @ApiDocMethod
     */
    List<APIOwnerType> getAPIOwnerTypes() throws CallerException;

    /**
     * 服务API综合查询
     * 
     * @param searchKey
     * @return
     * @author zhangchao
     * @ApiDocMethod
     */
    PageInfo<APISearchResult> searchAPIDocs(APISearchKey searchKey) throws CallerException;

    String getAPIVersionNew(String apiDetailIndexId) throws CallerException;

    /**
     * 根据参数类索引ID获取API参数类的属性列表
     * 
     * @param fieldIndexId
     * @return
     * @author zhangchao
     * @ApiDocMethod
     */
    String getAPIClassDetail(String apiParamIndexId) throws CallerException;

    /**
     * 根据索引删除API信息
     * 
     * @param indexId
     * @throws CallerException
     * @author zhangchao
     * @ApiDocMethod
     */
    void deleteAPINew(String indexId) throws CallerException;

}

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>服务参数模板设置</title>
<%@ include file="/inc/inc.jsp"%>
<style type="text/css">
    code {
      background-color: #f5f5f5;
    }
  </style>
</head>
<body
	class="theme-whbl  pace-done fixed-header fixed-leftmenu fixed-footer">
	<div id="theme-wrapper">

		<%@ include file="/inc/head.jsp"%>


		<div id="page-wrapper" class="container">
			<div class="row">
				<%@ include file="/inc/leftmenu.jsp"%>

				<div id="content-wrapper">
					<div class="row">
						<div class="col-lg-12">
							<div class="row">
								<div class="col-lg-12">
									<ol class="breadcrumb">
										<li><span>首页</span></li>
										<li><span><a href="../api/tosearch.html?owner=<c:out value="${apiCallSetting.owner}"/>&ownerType=<c:out value="${apiCallSetting.ownerType}"/>&keywords=<c:out value="${apiCallSetting.method}"/>&activemenu=m_api">离线服务搜索</a></span></li>
										<li class="active"><span>参数模板设置</span></li>
									</ol>
								</div>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-lg-12">
							<div class="main-box">
								<header class="main-box-header clearfix">
									<h2>参数模板设置</h2>
								</header>
								<div class="main-box-body clearfix">
									<div class="form-horizontal" role="form">
										<div class="form-group">
											<label class="col-lg-2 control-label">提供者</label>
											<div class="col-lg-8">
												<input type="text" class="form-control" id="owner" value="<c:out value="${apiCallSetting.owner}"/>" readonly>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">服务接口</label>
											<div class="col-lg-8">
												<input type="text" class="form-control" id="interfaceName" value="<c:out value="${apiCallSetting.interfaceName}"/>" readonly>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">方法</label>
											<div class="col-lg-8">
																								<small class="red"><c:out value="${apiCallSetting.briefComment}"/></small>
												<input type="text" class="form-control" id="method" value="<c:out value="${apiCallSetting.method}"/>" readonly>
												<a href="<%=_base %>/api/getAPIVersionNewDetail.html?indexId=<c:out value="${apiCallSetting.indexId}"/>&activemenu=m_api" target="_blank">查看规范</a>
											</div>
										</div>
										<c:forEach items="${apiCallSetting.reqSettings}" var="reqParam" varStatus="status">
											<div class="form-group" name="DIV_REQ_PARAM_SETTING">
												<label class="col-lg-2 control-label">入参${reqParam.sort}<br>(${reqParam.paramName})</label>
												<div class="col-lg-8">
												    ${reqParam.javaType}
													<input type="hidden" id="sort" value="${reqParam.sort}"/>
													<input type="hidden" id="paramName" value="${reqParam.paramName}"/>
													<c:if test="${reqParam.editorType==1 }">
														<div id="REQ_JSONEDITOR"  style="height: 300px" paramjson="<c:out value="${reqParam.json}"/>"></div>
													</c:if>
													<c:if test="${reqParam.editorType==2 }">
														<input type="text" class="form-control" id="REQ_JSONTEXT" name="REQ_JSONTEXT" value="<c:out value="${reqParam.json}"/>" />
													</c:if>
													
												</div>
											</div>
										</c:forEach>
										
										<c:if test="${apiCallSetting.editorType!=3 }">
											<div class="form-group" name="DIV_RESP_PARAM_SETTING">
												<label class="col-lg-2 control-label">返回参数</label>
												<div class="col-lg-8">
												    ${apiCallSetting.returnJavaType} 
													<c:if test="${apiCallSetting.editorType==1 }">
														<div id="RESP_JSONEDITOR"  style="height: 300px"  paramjson="<c:out value="${apiCallSetting.returnJson}"/>"></div>
													</c:if>
													<c:if test="${apiCallSetting.editorType==2 }">
														<input type="text" class="form-control" id="RESP_JSONTEXT" name="RESP_JSONTEXT" value="<c:out value="${apiCallSetting.returnJson}"/>"/>
													</c:if>
													
												</div>
											</div>
										</c:if>
										<div class="form-group">
											<div class="col-lg-offset-2 col-lg-10">
												<button type="button" class="btn btn-success" id="BTN_SUBMIT">提交</button>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- aaa -->
					<%@ include file="/inc/foot.jsp"%>
				</div>
			</div>
		</div>
	</div>
	
	 <script type="text/javascript">
	 	var apiSettingIndexId="<c:out value="${apiCallSetting.indexId}"/>";
		(function () {
			seajs.use([ 'app/sandbox/apireqparamset' ], function(SearchAPIReqParams) {
				var pager = new SearchAPIReqParams({
					element : document.body
				});
				pager.render();
			});
		})();
	</script>
</body>
</html>


<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>服务测试</title>
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
										<li class="active"><span>服务测试</span></li>
									</ol>
								</div>
							</div>
						</div>
					</div>

					<div class="row" id="DIV_API">
						<div class="col-lg-12">
							<div class="main-box">
								<header class="main-box-header clearfix">
									<h2>填写测试信息</h2>
								</header>
								<div class="main-box-body clearfix">
									<div id="reg-form" class="form-horizontal ui-form" role="form">
										<div class="form-group ui-form-item">
											<label class="col-lg-2 control-label ui-label"><span class="ui-form-required">*</span>注册中心地址</label>
											<div class="col-lg-8">
												<div class="input-group">
													<input type="text" class="form-control" id="registryURL" name="registryURL" size="10" class="ui-input"
														   value="zookeeper://10.1.228.222:19181">
      												<span class="input-group-btn">
        												<button class="btn btn-lg btn-primary" type="button" id="HrefConnectTest">连通性测试</button>
      												</span>
												</div><!-- /input-group -->
												<div class="ui-form-explain">请确保测试站点可以连接到您的服务注册中心</div>
											</div>
										</div>
										<div class="form-group ui-form-item">
											<label class="col-lg-2 control-label ui-label">提供者</label>
											<div class="col-lg-8">
												<input type="text"   class="form-control" id="owner" value="<c:out value="${apiCallSetting.owner}"/>" readonly>
											</div>
										</div>
										<div class="form-group ui-form-item">
											<label class="col-lg-2 control-label ui-label">服务接口</label>
											<div class="col-lg-8">
												<input type="text"   class="form-control" name="interfaceName" id="interfaceName" value="<c:out value="${apiCallSetting.interfaceName}"/>" readonly>
											</div>
										</div>
										<div class="form-group ui-form-item">
											<label class="col-lg-2 control-label ui-label">方法</label>
											<div class="col-lg-8">
												<small class="red"><c:out value="${apiCallSetting.briefComment}"/></small>
												<input type="text" class="form-control" name="method" id="method" value="<c:out value="${apiCallSetting.method}"/>" readonly>
												<a href="<%=_base %>/api/getAPIVersionNewDetail.html?indexId=<c:out value="${apiCallSetting.indexId}"/>&activemenu=m_api" target="_blank">查看规范</a>
											</div>
										</div>
										
										<c:forEach items="${apiCallSetting.reqSettings}" var="reqParam" varStatus="status">
											<div class="form-group ui-form-item" name="DIV_REQ_PARAM_SETTING"  id="DIV_REQ_PARAM_${reqParam.paramName}">
												<label class="col-lg-2 control-label ui-label">入参${reqParam.sort}<br>(${reqParam.paramName})</label>
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
										<div class="form-group">
											<div class="col-lg-offset-2 col-lg-10">
												<button type="button" class="btn btn-success" id="BTN_TEST">执行测试</button>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					
					<div class="row" style="display:none" id="DIV_TEST_RESULT">
						<div class="col-lg-12">
							<div class="main-box">
								<header class="main-box-header clearfix">
									<h2>测试结果</h2>
								</header>
								<div class="main-box-body clearfix">
									<div class="form-horizontal ui-form" role="form">
										<c:if test="${apiCallSetting.editorType!=3 }">
											<div class="form-group ui-form-item" name="DIV_RESP_PARAM_SETTING">
												<label class="col-lg-2 control-label ui-label">测试成功信息</label>
												<div class="col-lg-8">
												    ${apiCallSetting.returnJavaType} 
													<c:if test="${apiCallSetting.editorType==1 }">
														<div id="RESP_JSONEDITOR"  style="height: 300px"></div>
													</c:if>
													<c:if test="${apiCallSetting.editorType==2 }">
														<textarea class="form-control" id="RESP_JSONTEXT" rows="8" readonly></textarea>
													</c:if> 
												</div>
											</div>
										</c:if>
										<c:if test="${apiCallSetting.editorType==3 }">
											<div class="form-group ui-form-item" name="DIV_RESP_PARAM_SETTING">
												<label class="col-lg-2 control-label ui-label">测试成功信息</label>
												<div class="col-lg-8">
												    ${apiCallSetting.returnJavaType} 
													<textarea class="form-control" id="RESP_JSONTEXT" rows="8" readonly>测试成功，无返回信息</textarea>
												</div>
											</div>
										</c:if>
										<div class="form-group ui-form-item" id="DIV_FAILURE_RESULT" style="display:none">
											<label class="col-lg-2 control-label ui-label">测试失败信息</label>
											<div class="col-lg-8">
												<textarea class="form-control" id="RESP_FAILURETEXT" rows="8" readonly></textarea>
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
		var apiOwner = "<c:out value="${apiCallSetting.owner}"/>",
				apiInterfaceName = "<c:out value="${apiCallSetting.interfaceName}"/>",
				apiMethod = "<c:out value="${apiCallSetting.method}"/>";
		var editType = "<c:out value="${apiCallSetting.editorType}" /> ";
		(function() {
			seajs.use([ 'app/sandbox/mocktest' ], function(APIMocktest) {
				var pager = new APIMocktest({
					element : document.body
				});
				pager.render();
			});
		})();
	</script>
</body>
</html>


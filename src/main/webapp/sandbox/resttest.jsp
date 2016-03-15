<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>REST服务测试</title>
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
										<li class="active"><span>REST服务测试</span></li>
									</ol>
								</div>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-lg-12">
							<div class="main-box">
								<header class="main-box-header clearfix">
									<h2>填写测试信息</h2>
								</header>
								<div class="main-box-body clearfix">
									<div id="rest-form" class="form-horizontal ui-form" role="form">
										<div class="form-group ui-form-item">
											<label class="col-lg-2 control-label ui-label"><span class="ui-form-required">*</span>HTTP地址</label>
											<div class="col-lg-8">
												<input type="text" class="form-control" id="restURL" name="restURL"
													   value="http://10.1.228.222:10771/customer/<c:out value="${apiRest.restRelativeURL}"/>" class="ui-input" >
												<div class="ui-form-explain">请确保地址可以正常访问</div>
											</div>
										</div>
										<div class="form-group ui-form-item">
											<label class="col-lg-2 control-label ui-label">REST请求方式</label>
											<div class="col-lg-8">
												<input type="text" class="form-control" id="restMethod" name="restMethod" size="10" value="<c:out value="${apiRest.restMethod}"/>" readonly>
											</div>
										</div>
										<div class="form-group ui-form-item">
											<label class="col-lg-2 control-label ui-label">映射的服务接口</label>
											<div class="col-lg-8">
												<input type="text"   class="form-control" id="interfaceName" value="<c:out value="${apiRest.interfaceName}"/>" readonly>
											</div>
										</div>
										<div class="form-group ui-form-item">
											<label class="col-lg-2 control-label ui-label">映射的方法</label>
											<div class="col-lg-8">
												<input type="text" class="form-control" id="method" value="<c:out value="${apiRest.methodName}"/>" readonly>
											</div>
										</div>
										
										<c:forEach items="${apiRest.restParams}" var="reqParam" varStatus="status">
											<div class="form-group ui-form-item" name="DIV_REQ_PARAM_SETTING"  id="DIV_REQ_PARAM_${reqParam.paramName}">
												<label class="col-lg-2 control-label ui-label">入参${status.index}<br>(${reqParam.paramName})</label>
												<div class="col-lg-8">
												   由${reqParam.javaType}映射
													<input type="hidden" id="paramName" value="${reqParam.paramName}"/>
													<c:if test="${reqParam.editorType==1 }">
														<div id="REQ_JSONEDITOR"  style="height: 300px" paramjson="<c:out value="${reqParam.paramValue}"/>"></div>
													</c:if>
													<c:if test="${reqParam.editorType==2 }">
														<input type="text" class="form-control" id="REQ_JSONTEXT" name="REQ_JSONTEXT" value="<c:out value="${reqParam.paramValue}"/>" />
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
										<div class="form-group ui-form-item" id="DIV_SUCCESS_RESULT" style="display:none">
											<label class="col-lg-2 control-label ui-label">测试成功信息</label>
											<div class="col-lg-8">
											   <div id="RESP_JSONEDITOR"  style="height: 300px"></div>
											</div>
										</div> 
										<div class="form-group ui-form-item" id="DIV_FAILURE_RESULT" style="display:none">
											<label class="col-lg-2 control-label ui-label">测试失败信息</label>
											<div class="col-lg-8">
												<textarea class="form-control" id="RESP_JSONTEXT" rows="8" readonly></textarea>
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

	<script>
		(function() {
			seajs.use('app/sandbox/resttest', function(APIResttest) {
				var pager = new APIResttest({
					element : document.body
				});
				pager.render();
			});
		})();
	</script>
</body>
</html>


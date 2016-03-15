<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>服务详情</title>
<%@ include file="/inc/inc.jsp"%>
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
										<li><a href="${_base}/api/index.html?activemenu=m_home">首页</a></li> 
										<li><span><a href="../api/tosearch.html?activemenu=m_api">离线服务搜索</a></span></li>
										<li class="active"><span>服务详情</span></li>
									</ol>
								</div>
							</div>
						</div>
					</div>
						<div class="row">
								<div class="col-lg-12">
									<div class="main-box clearfix">
										<header class="main-box-header clearfix" >
											<h2>
												<span class="emerald">服务：<font color="red"><c:out value="${apiDoc.interfaceName}"/></font>
												<br>方法：<font color="red"><c:out value="${apiDoc.methodName}"/></font>   版本：<font color="red"><c:out value="${apiDoc.version}"/></font>
												</span>
											</h2>
											<div class="desc"><c:out value="${apiDoc.briefComment}"/></div>
											<small class="gray">服务管理者:<font color="red"><c:out value="${apiDoc.author}"/> </font>&nbsp;&nbsp;&nbsp;索引发布时间:<font color="red"><c:out value="${apiDoc.publishDate}"/></font></small>
										</header>
									</div>
								</div>
							</div>

					<!-- 入参开始 -->
							<div class="row">
								<div class="col-lg-12">
									<div class="main-box clearfix">
										<header class="main-box-header clearfix">
											<h2>服务入参规范</h2>
										</header>
										<div class="main-box-body clearfix">
											<div class="table-responsive">
												<c:if test="${fn:length(apiDoc.inAPIParamDocs)<=0}">
													此服务没有入参信息
												</c:if>
												<c:if test="${fn:length(apiDoc.inAPIParamDocs)>=0}">
												<table class="table tree-1 table-bordered table-striped table-condensed">
									               	<thead>
														<tr>
														<th>参数名称</th>
														<th>参数类型</th>
														<th>参数说明</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach var="APIParamDoc" items="${apiDoc.inAPIParamDocs}" varStatus="varStatus">
										                <tr class="treegrid-<c:out value="${APIParamDoc.id}"/>" id="treegrid-<c:out value="${APIParamDoc.id}"/>" indexId="<c:out value="${APIParamDoc.id}"/>" <c:if test="${APIParamDoc.canUnfold==true}"> name="TR_IN_EXPANDER_NAME"</c:if>>
										                    <td><c:out value="${APIParamDoc.name}"/> <c:if test="${APIParamDoc.canUnfold==true}"><b><a href="javascript:void(0)">包含子属性</a></b> </c:if></td><td><c:out value="${APIParamDoc.className}"/></td><td><c:out value="${APIParamDoc.commentText}"/></td>
										                </tr>
										                </c:forEach>
									                </tbody>       
									            </table>
									            </c:if>	
											</div>
										</div>
									</div>
								</div>
							</div>
							<!-- 入参结束 -->
					<!-- 出参开始 -->
							<div class="row">
								<div class="col-lg-12">
									<div class="main-box clearfix">
										<header class="main-box-header clearfix">
											<h2>服务返回参数规范</h2>
										</header>
										<div class="main-box-body clearfix">
											<div class="table-responsive">
												<c:if test="${apiDoc.returnAPIParamDoc==null}">
													此服务没有返回信息
												</c:if>
												<c:if test="${apiDoc.returnAPIParamDoc!=null}">
												<table class="table tree-2 table-bordered table-striped table-condensed">
									               	<thead>
														<tr>
														<th>参数名称</th>
														<th>参数类型</th>
														<th>参数说明</th>
														</tr>
													</thead>
													<tbody>
										                <tr class="treegrid-<c:out value="${apiDoc.returnAPIParamDoc.id}"/>" id="treegrid-<c:out value="${apiDoc.returnAPIParamDoc.id}"/>" indexId="<c:out value="${apiDoc.returnAPIParamDoc.id}"/>" <c:if test="${apiDoc.returnAPIParamDoc.canUnfold==true}"> name="TR_OUT_EXPANDER_NAME"</c:if>>
										                    <td><c:out value="${apiDoc.returnAPIParamDoc.name}"/> <c:if test="${apiDoc.returnAPIParamDoc.canUnfold==true}"><b><a href="javascript:void(0)">包含子属性</a></b> </c:if></td><td><c:out value="${apiDoc.returnAPIParamDoc.className}"/><c:if test="${apiDoc.returnAPIParamDoc.generic==true}">&lt;
										                    	<c:forEach var="genericAPIParamDoc" items="${apiDoc.returnAPIParamDoc.genericAPIParamDocs}" varStatus="varStatus">
										               				<c:if test="${genericAPIParamDoc.canUnfold==true}"><b><a href="javascript:void(0)"><c:out value="${genericAPIParamDoc.name}"/></a></b> </c:if>
										              			 </c:forEach>
										                    &gt;</c:if></td><td><c:out value="${apiDoc.returnAPIParamDoc.commentText}"/></td>
										                </tr>
									                </tbody>       
									            </table>
									            </c:if>	
											</div>
										</div>
									</div>
								</div>
							</div>
							<!-- 入参结束 -->
						<!-- 签名异常 -->
							<div class="row">
								<div class="col-lg-12">
									<div class="main-box clearfix">
										<header class="main-box-header clearfix">
											<h2>签名异常信息</h2>
										</header>
										<div class="main-box-body clearfix">
											<div class="table-responsive">
												<c:out value="${apiDoc.exceptions}"/>
											</div>
										</div>
									</div>
								</div>
							</div>
					<%@ include file="/inc/foot.jsp"%>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		 (function() {
			seajs.use([ 'app/api/apidetail' ], function(APIDetailPager) {
				var pager = new APIDetailPager({
					element : document.body
				});
				pager.render();
			});
		})(); 
	</script>

	<script id="APIInParamSubClassFieldsImpl" type="text/x-jsrender">
	<tr class="treegrid-{{:id}} treegrid-parent-{{:pid}}" id="treegrid-{{:id}}" pid="treegrid-parent-{{:pid}}" indexId="{{:id}}" {{if canUnfold==true}} name="TR_IN_EXPANDER_NAME" {{/if}} >
		<td>{{:paramName}} {{if canUnfold==true}}<b><a href="javascript:void(0)">包含子属性</a></b>{{/if}}</td><td>{{:className}}</td><td>{{:commentText}}</td>
	</tr>
	</script>
	
	<script id="APIOutParamSubClassFieldsImpl" type="text/x-jsrender">
	<tr class="treegrid-{{:id}} treegrid-parent-{{:pid}}" id="treegrid-{{:id}}" pid="treegrid-parent-{{:pid}}" indexId="{{:id}}" {{if canUnfold==true}} name="TR_OUT_EXPANDER_NAME" {{/if}} >
		<td>{{:paramName}} {{if canUnfold==true}}<b><a href="javascript:void(0)">包含子属性</a></b>{{/if}}</td><td>{{:className}}</td><td>{{:commentText}}</td>
	</tr>
	</script>


</body>
</html>


<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<title>运营家服务离线测试</title>
	<%@ include file="/inc/inc.jsp"%>
</head>
<body class="theme-whbl  pace-done fixed-header fixed-leftmenu fixed-footer">
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
										<li class="active"><span>离线服务搜索</span></li>
									</ol>
								</div>
							</div>
						</div>
					</div>


					<!-- aa -->

					<div class="row">
						<div class="col-lg-12">
							<div class="main-box clearfix">
								<header class="main-box-header clearfix" >
									<h2>
										<span class="emerald" id="HEADER_TOTAL_SPAN">搜索服务
										</span>
									</h2>
									<small class="gray"><c:if test="${not empty owner}">当前搜索的服务提供者:<font color="red"><span id="ownerId"><c:out value="${owner}"/></span> </font><span  id="ownerTypeId" hidden><c:out value="${ownerType}"/></span></c:if><span id="limitExceptionId" hidden><c:out value="${limitException}"/></span><c:if test="${limitException=='0'}"><font color="red">搜索范围为声明了异常的服务</font> </c:if><c:if test="${limitException=='1'}"><font color="red">搜索范围为没有声明异常的服务</font> </c:if>您可以输入服务接口的类，方法，服务编码，服务描述的关键字。例如："getSysParam"或者"获取字典参数"</small>
								</header>
								<div class="main-box-body clearfix">
									<div id="search-form">
										<div class="input-group">
											<input type="text" class="form-control input-lg"
												id="API_KEY" data-toggle="tooltip" data-placement="bottom" title="输入服务关键字，回车开始搜索" value="<c:out value="${keywords}"/>"/>
											<div class="input-group-btn">
												<button class="btn btn-lg btn-primary" type="button" id="BTN_SEARCH">
													<i class="fa fa-search"></i> Search
												</button>
											</div>
										</div>
									</div>
									<small class="gray" id="SEARCH_RESULT_TIPS"></small>
									<ul id="search-results">
										
									</ul>
									
								</div>
								<div id="pagination-content"></div>
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
		(function () {
			seajs.use(['app/api/search'], function(APISearchPager) {
				var pager = new APISearchPager({
					element : document.body,
					owner: "<c:out value="${owner}"/>",
					ownerType: "<c:out value="${ownerType}"/>"
				});
				pager.render();
			});
		})();
	</script>

	<script id="SearchResultImpl" type="text/x-jsrender">
	<li>
		<h3 class="title">
			<a>
					<span><a class="table-link" href="${_base}/api/apidetail.html?activemenu=m_api&owner={{:owner}}&ownerType={{:ownerType}}&interfaceName={{:interfaceName}}&methodName={{:methodName}}&version={{:version}}">{{:interfaceNameHighlight}}</span>#<span>{{:methodNameHighlight}}</a></span>
			</a>
		</h3>
		<div class="clearfix">
			<div class="desc">{{:briefCommentHighlight}}</div>
		    <div class="desc"><b>[所有版本信息]:</b>{{for apiVersions ~owner=owner ~ownerType=ownerType ~interfaceName=interfaceName ~methodName=methodName}} <font color="blue"><a class="table-link" href="${_base}/api/apidetail.html?activemenu=m_api&owner={{:~owner}}&ownerType={{:~ownerType}}&interfaceName={{:~interfaceName}}&methodName={{:~methodName}}&version={{:version}}">{{:version}}</a> </font> &nbsp; {{/for}}</div>
			<div class="desc"><b>[API_CODE]:</b><font color="blue">{{:apiCodeHighlight}}</font></div>
			<div class="desc"><b>[签名异常]:</b><font color="red">{{:exceptions}}</font></div>
			<div class="desc"><b>[提供者]:</b><a href="${_base}/api/tosearch.html?owner={{:owner}}&ownerType={{:ownerType}}&activemenu=m_api"> <font color="blue">{{:ownerHighlight}}</font></a></div>
			<div class="desc"><b>[仓库组]:</b><font color="blue">{{:groupIdHighlight}}</font> <b>[构件名]:</b><font color="blue">{{:artifactIdHighlight}}</font></div>
			<div class="desc"><b>[负责人]:</b><font color="blue">{{:authorHighlight}}</font></div>
			<div class="desc"><b>[REST支持]:</b><font color="red">{{if restSupported==true}} 支持 {{else}} 不支持 {{/if}}</font></div>
			{{if restSupported==true}}
			<div class="desc"><b>[REST地址]:</b><font color="blue">http://ip:port/xx/{{:restRelativeURLHighlight}}</font></div>
			{{/if}}
			<div class="desc"><b>[详细说明]:</b>{{:detailCommentHighlight}}</div>
			<div class="link-title">
				<footer class="story-footer"> 
					{{if isSetted==false}}
					<a href="${_base}/sandbox/apireqparamset.html?indexId={{:id}}&activemenu=m_api"> 
						<i class="fa fa-pencil"></i>   设置模板
					</a>
					{{else}}
					<a href="${_base}/sandbox/apireqparamset.html?indexId={{:id}}&activemenu=m_api"> 
						<i class="fa fa-pencil"></i>   修改模板
					</a>
					
					<a href="${_base}/sandbox/toMockTest.html?indexId={{:id}}&activemenu=m_api"> 
						<i class="fa fa-jsfiddle"></i> dubbo测试
					</a>
					{{if restSupported==true}}
					<a href="${_base}/sandbox/resttest.html?indexId={{:id}}&activemenu=m_api"> 
						<i class="fa fa-jsfiddle"></i> rest测试
					</a>
					{{/if}}
					{{/if}}

					<c:if test="${allowDelete=='ASIAINFORUNNER128UHXUYSHAMD5OPOQOUAJHDGH9787GDHSGAGG'}">
						<a href="javascript:void(0)" name="HrefAPIDelete" indexId="{{:id}}"> 
							<i class="fa fa-wrench"></i> 作废
						</a>
					</c:if>
				</footer>
			</div>
		</div>
	</li>
	</script>
</body>
</html>
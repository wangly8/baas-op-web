<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>运营家服务离线测试</title>
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
										<li><span><a
												href="../api/index.html?activemenu=m_api">首页</a></span></li>
										<li class="active"><span>离线服务数据导入</span></li>
									</ol>
								</div>
							</div>
						</div>
					</div>


					<div class="row">
						<div class="col-lg-12">
							<div class="main-box clearfix">
								<header class="main-box-header clearfix">
								<h2>
									<span class="emerald">下载并导入服务数据 </span>
								</h2>
								<div class="main-box-body clearfix">
									&nbsp;&nbsp;&nbsp;&nbsp;若您测试的服务提供者不在下面的模块分类中，您需要按照<a
										href="../api/help.html">使用帮助</a>从运营家服务在线网站【<a href="http://124.207.3.10:28033/runner-osp-web" target="_blank">外网</a> <a href="http://10.1.234.160:14021/runner-osp-web" target="_blank">内网</a>】下载模块服务数据。然后点击
									<button type="button" id="BTN_UPLOAD" class="btn btn-success">导入</button>
								</div>
								</header>
							</div>
						</div>
					</div>
					<div id="OwnerStatDIV" class="row">
						
					</div>

					<%@ include file="/inc/foot.jsp"%>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		(function() {
			seajs.use([ 'app/api/index' ], function(APIIndexPager) {
				var pager = new APIIndexPager({
					element : document.body
				});
				pager.render();
			});
		})();
	</script>

<script id="ownerStatTemplate" type="text/x-jsrender">
{{if data.length==0}}
	没有任何服务信息,请点击上面的提示进行导入
{{else}}
	<div class="main-box clearfix">
		<div class="tabs-wrapper profile-tabs">
			<ul class="nav nav-tabs">
				{{for data}}
					<li {{if #index==0}} class="active" {{/if}} ><a href="#tab-{{:ownerType}}" data-toggle="tab">{{:ownerTypeName}}[{{:ownerCount}}/{{:serviceCount}}]</a></li>
				{{/for}}
			</ul>
			<div class="tab-content">
				{{for data}}
					<div class="tab-pane fade {{if #index==0}}in active{{/if}}" id="tab-{{:ownerType}}">
						{{if ownerStatistics.length==0}}
							此分类下没有任何模块
						{{else}}
							{{for ownerStatistics}}
								<div class="col-lg-4 col-sm-6 col-xs-12" style="color: white;">
									<div class="main-box infographic-box {{:color}}">
										<i class="fa fa-user"></i> 
										<span class="headline">{{:ownerName}}</span>
										<span class="value" > 
											<a style="color: white;" href="../api/tosearch.html?activemenu=m_api&owner={{:owner}}&ownerType={{:ownerType}}">{{:apiCount}}个服务 </a>
										</span>
									</div>
								</div>
							{{/for}}
						{{/if}}
					</div>
				{{/for}}
			</div>
		</div>
	</div>
{{/if}}
</script>

</body>
</html>


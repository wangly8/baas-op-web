<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>帮助</title>
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
										<li class="active"><span>使用帮助</span></li>
									</ol>
								</div>
							</div>
						</div>
					</div>


					<div class="row">
						<div class="col-md-12">
							<div class="main-box clearfix">
								<div class="main-box-body clearfix">
									<div>
										<header class="main-box-body clearfix">
										<h3>目录</h3>
										</header>
									</div>
									<ol class="clearfix">
										<li><h4>
												<a href="#step1">安装准备</a>
											</h4></li>
										<li><h4>
												<a href="#step2">软件安装</a>
											</h4> <a href="#step2.1"><span>2.1安装步骤</span></a><br>
												 <a href="#step2.2"><span>2.2轻松访问</span></a><br>
										</li>
										<li>
										<h4>
											<a href="#step3">使用说明</a>
										</h4> <a href="#step3.1"><span>3.1离线服务导入</span></a><br>
										 <a href="#step3.2"><span>3.2离线服务测试</span></a><br>
										
										 </li>
									</ol>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="main-box clearfix">
								<div class="main-box-body clearfix">
									<h4>
										<a id="step1" href="#"><span>1.安装准备</span></a>
									</h4>
									<p>&nbsp;&nbsp;离线测试工具旨在给您提供一个方便测试的平台，由于我们的平台是用java语言开发，所以您需要安装jdk，并配置好环境变量，安装清单如下：</p>
									<ol>
										<li>Jdk1.6+</li>
										<li>OPT-STester</li>
									</ol>
									<br>
										<h4>
											<a id="step2" href="javascript:void(0);"><span>2.软件安装</span></a>
										</h4>
										<h5>
											<a id="step2.1" href="javascript:void(0);"><strong>2.1.安装步骤</strong></a>
										</h5>
										<ol>
											<li><p>先安装jdk，并配置环境变量，过程就不描述了。</p></li>
											<li><p>解压工程包，解压后的目录如下</p></li>
											<img height="300" width="50%" src="${_base}/resources/images/jieya.png">
											<br><br>
												<li><p>
														配置http端口：编辑conf/jetty.properties文件，找到属性jetty.connector.port配置开放的http端口，其他属性可根据自身需求可调整，属性上面都有说明。<span
															style="color: red;">（注意：属性jetty.web.appRoots不可更改）</span>
													</p></li>
												<li><p>配置布署授权码：（布署授权码可在平台主页导航处找到“我的布署授权码”获得）编辑conf/project.properties文件，找到属性pro.dev.depAuthCode配置授权码。</p></li>
												<li><p>配置代理：如果目标布署服务器不能直接访问平台，而需要通过代理的方式访问的话，可通配置conf/project.properties文件中属性pro.dev.needProxy、pro.dev.proxyHost、pro.dev.proxyPort来配置。</p></li>
												<li><p>启动：执行./bin/start.sh即可。</p></li>
										</ol>
										<h5>
											<a id="step2.2" href="javascript:void(0);"><strong>2.1.轻松访问</strong></a>
											<p>
											  <br> 启动访问之后您可以通过输入：http://127.0.0.1:port/OPT-STester/api/index.html 进行访问。<br><br>
											   <em>备注</em>：这里的port是您自己设置的端口，默认是8080
											</p>
										</h5>
										<br>
										<h4>
											<a id="step3" href="javascript:void(0);"><span>3.使用说明</span></a>
										</h4>
										<h5>
											<a id="step3.1" href="javascript:void(0);"><strong>3.1.离线服务导入</strong></a>
											
										</h5>
										
											<ol>
											<li>
											<p>服务下载：刚进入页面的时候是没有服务数据的，您需要根据根据页面上的提示从“<font color="red">运营家服务在线</font>”【<a href="http://10.1.234.160:14021/runner-osp-web/api/index.html" target="_blank">内网</a>&nbsp;<a href="http://124.207.3.10:28033/runner-osp-web/api/index.html" target="_blank">外网</a>】中下载数据<br></p>
											<ul style="list-style-type:upper-roman;">
											<li>
											下载整个产品的服务数据：点击下图中的红框位置处，即可下载整个产品的服务数据
											<br>
											<br>
											<img height="400" width="80%" src="${_base}/resources/images/download.png"></img>
											
											</li>
											<br>
											<li>
											下载单个服务数据：点击某个产品，或者点击“<font color="red">服务搜索</font>”菜单进入服务搜索页面,查找您需要的服务，点击下载即可。<br><br>
											比如要下载runner-bis下的某个服务，点击红框位置
											<br><br>
											<img height="400" width="80%" src="${_base}/resources/images/inSer.png"></img>
											<br>
											<br>
											进入服务搜索页面，找到对应的服务，点击下方的“<font color="red">下载数据</font>”链接，即可下载单个服务的数据
											<br>
											<br>
											<img height="400" width="80%" src="${_base}/resources/images/download1.png"></img>
											</li>
											
											</ul>
											</li>
											<br><br>
											<li>
											离线服务数据导入：点击“<font color="red">导入</font>”按钮即可导入您的数据，导入后的效果如下：<br><br>
											<img height="400" width="80%" src="${_base}/resources/images/shujudaoru2.png"></img>
											</li>
											</ol>
										
										<h5>
											<a id="step3.2" href="javascript:void(0);"><strong>3.2.离线服务测试</strong></a>
										</h5>
										<ol>
										<li>
										  服务搜索：我们提供了两种途径可以进入搜索页面
										</li>
										 
										 <br>
										<ul style="list-style-type:upper-roman;">
										<li>
										    第一种，点击单个产品进入数据的搜索，这种方式是针对单个产品进行的服务搜索
										 <br>
										 <br>
										 比如点击runner-bis下标记的红框
										 <br>
										 <br>
										 <img height="400" width="80%" src="${_base}/resources/images/inSer2.png"></img>
										 <br>
										 <br>
										 进入后效果如下
										 <br>
										 <br>
										 <img height="400" width="80%" src="${_base}/resources/images/search1.png" />
										</li>
										<br>
										<br>
										<li>
										  第二种，点击左侧的“<font color="red">离线服务测试</font>”菜单，这种方式是针对所有的产品进行服务搜索
										<br>
										<br>
										 <img height="400" width="80%" src="${_base}/resources/images/search2.png" />
										</li>
										</ul>
										
										<br>
										<br>
										<!-- <h5>
											<a id="step3.3" href="javascript:void(0);"><strong>3.3.服务测试</strong></a>
										</h5> -->
										
										<li>
										查看服务详情：点击版本信息，查看服务的入参和出参的详细信息
										<br>
										<br>
										比如点击下面的服务
										<br>
										<br>
										<img height="400" width="80%" src="${_base}/resources/images/detail1.png" />
										<br>
										<br>
										结果如下
										<br>
										<br>
										<img height="400" width="80%" src="${_base}/resources/images/detail.png" />
										</li>
										<br>
										<br>
										<li>
										服务测试：我们提供了“<font color="red">dubbo</font>”和“<font color="red">rest</font>”两种形式的服务测试（如下图）;如果有的服务显示“<font color="red">无模板</font>”，则需要到“<font color="red">运营家服务在线</font>”下载最新的服务数据。
										</li>
										<br>
										&nbsp;&nbsp;&nbsp;<img height="400" width="80%" src="${_base}/resources/images/test.png" />
										<br>
										<br>
										<ul style="list-style-type:upper-roman;">
										<li>
										dubbo测试：点击“<font color="red">dubbo测试</font>”链接，进入dubbo测试页面，在注册中心一栏您可以填写您的zookeper的地址，点击“<font color="red">连通性测试</font>”连接，如果提示“<font color="red">连接成功</font>”，则可以进行测试；
										如果提示“<font color="red">测试注册中心连通性失败,请检查</font>”则表示服务不通，不能进行测试。
										<br>
										<br>
										<img height="400" width="80%" src="${_base}/resources/images/dubbo1.png" />
										<br>
										<br>
										您可以根据模板修改里面的数据来迎合您的测试，修改好数据以后，点击执行测试，即可以看到测试结果
										<br>
										<br>
										<img height="400" width="80%" src="${_base}/resources/images/dubbo2.png" />
										
										</li>
										<br>
										<br>
										<li>
										rest测试：如果有的服务显示支持rest，则您既可以对其进行rest测试，点击“<font color="red">rest测试</font>”链接，既可以进进入rest的测试页面，rest测试基本同dubbo测试相同，只不过需要在“<font color="red">http地址</font>”一栏填入rest服务地址即可，这里不再赘述。
										<br>
										<br>
										<img height="400" width="80%" src="${_base}/resources/images/rest.png" />
										
										</li>
										</ul>
										<br>
										<br>
										
										</ol>
								</div>
							</div>
						</div>
					</div>

					<%@ include file="/inc/foot.jsp"%>
				</div>
			</div>
		</div>
	</div>


</body>
</html>


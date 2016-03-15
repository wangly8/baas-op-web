<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="nav-col">
<section id="col-left" class="col-left-nano">
<div id="col-left-inner" class="col-left-nano-content">

<div class="collapse navbar-collapse navbar-ex1-collapse" id="sidebar-nav">
<ul class="nav nav-pills nav-stacked">
<li id="m_home" >
<a href="${_base}/api/index.html">
<i class="fa fa-cloud-upload"></i>
<span>离线服务导入</span>
</a>
<li>
</li>
<li id="m_api">
<a href="${_base}/api/tosearch.html?activemenu=m_api">
<i class="fa fa-th-large"></i>
<span>离线服务测试</span>
</a>
</li>
<li id="m_help">
<a href="${_base}/api/help.html?activemenu=m_help" >
<i class="fa fa-bullhorn"></i>
<span>使用帮助</span>
</a>
</li>

</ul>
</div>
</div>
</section>
</div>
<script type="text/javascript">
(function () {
	seajs.use(['app/inc/leftmenu'], function (LeftMenuPager) {
		var activemenu="${param.activemenu}";
		var pager = new LeftMenuPager({element: document.body,activemenu:activemenu});
		pager.render();
	});
})();

</script>
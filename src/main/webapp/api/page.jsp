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
		<button id="BTN_UPLOAD">提交</button>
		
		</div>
				  <div id="view" class="pagination">	

					<%@ include file="/inc/foot.jsp"%>
			
	</div>
<script type="text/javascript">

var currentPage=${currentPage};

function refresh(currentPage)
{
	seajs.use('paging/0.0.1/paging' ,function (Paging) {
		
		
		var html=Paging.render(
				{
			        currentPage: currentPage,
			        // 总页数
			        pageCount: 10,
			        // 链接前缀
			        link: ''
			    }		
		);
		
		
		
		document.getElementById('view').innerHTML = html;
		
		
	})
}

seajs.use('paging/0.0.1/paging' ,function (Paging) {
	
	
	var html=Paging.render(
			{
		        currentPage: currentPage,
		        // 总页数
		        pageCount: 10,
		        // 链接前缀
		        link: '?id='
		    }		
	);
	
	
	
	document.getElementById('view').innerHTML = html;
	
	
})


</script>

<script id="peopleTemplate" type="text/x-jsrender">
  {{for people}}
    <div class="person">
      <span class="{{if role.indexOf('Lead')>=0}}special{{/if}}">
        {{:first}} <b>{{:last}}</b>
      </span>
      <span class="details">
        {{:role}}
      </span>
    </div>
  {{/for}}
</script>
</body>
</html>


<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${request.contextPath}/libs/bootstrap/2.3.2/css/bootstrap.css">
<title>${title}</title>
<style type="text/css">
	#footer table {
		font-size:20px;
		min-height: 200px;
	}
</style>
</head>
<body>
	<h1 class="box-shadow text-shadow">${name}</h1>
	<h2>Welcome ${user} !</h2>
	<#if name != null > name not null </#if>
	<table class="table">
		<tr>
			<th>index</th>
			<th>username</th>
			<th>password</th>
			<th>id</th>
		</tr>
		<tr>
			<td>1</td>
			<td>yohoo</td>
			<td>*******</td>
			<td>93920</td
		</tr>
		<#list studentlist as student>
		<tr>
			<td>${student_index}</td>
			<td>${student.user?if_exists}</td>
			<td>${student.psw?if_exists}</td>
			<td>${student.id?if_exists}</td
		</tr>
		</#list>
	</table>
	<p>
		当前语言: <@spring.message "language" />
		<a href="?locale=en_US">英文</a>
		<a href="?locale=zh_CN">中文简体</a>
		<a href="?locale=zh_TW">中文繁体</a>
	</p>
	<p><#list ["星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"] as x> ${x} </#list></p>
	
	<p><#assign map = {"语文":78, "数学":80} />语文: ${map.语文}</p>
	<p><#list map?keys as m>${m}</#list></p>
	<pre>
	${"herry & ketty" ? length}
	${"herry & ketty" ? html}
	${"herry & ketty" ? upper_case}
	${"herry & ketty" ? upper_case ? html}
	${"herry & ketty" ? replace('tt','nd')}
	${"abc<table>sdfsf" ? html}
	#{20; m1M2}
	${request}
	${request.request}
	${RequestParameters ["locale"]}
	<#assign contextPath = request.contextPath/>
	${contextPath}
	${request.contextPath}
	</pre>
</body>
</html>

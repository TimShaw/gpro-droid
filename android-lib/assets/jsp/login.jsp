<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>login</title>
     
</head>

 
<body>
		<%
			String action = request.getParameter("action");
			System.out.println("action :"+action);
			
			if("login".equals(action)){
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				System.out.println("username:"+username+", password:"+password);
			}
		%>
		
    <form name="login" method="post">
    	<input name="action" value="login" type="hidden"/>
    	用户名：<input name="username" id="username" type="text" /> <br/>	
    	密码：<input name="password" id="password" type="text" /><br/>
    	<input type="button" value="登录" onclick="doLogin()"/>
    	
    </form>
    
    <script>
    	var doLogin = function(){
    		
    		document.login.submit();	
    	}
    </script>
    	
</body>
</html>
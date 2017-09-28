<!DOCTYPE html>
<html>
	<head>
	<link rel="stylesheet" type="text/css" href="CSS/MyStyles.css">
	
	</head>
	<body>
	<div class="outer"> 	
		<div >
			<div class="inner">
			
					   <h1> Bazinga Search </h1> 
					   <form method="post" action="http://localhost:8080/IRFinalProject/SearchServlet">
					   <input type="text"  id="tBSearch" name="tBSearch" placeholder="Ask Bazinga a question"> 
						 <input type="submit"  value="Ask">
						</form>			
			</div>
			<hr width=100% >	
			Query: <% out.println(request.getAttribute("query"));%>
			<%out.println("<br>");%>
			<%out.println("<br>");%>
			<%@ page import="java.util.ArrayList" %>
			<%ArrayList<String> list=(ArrayList<String>)request.getAttribute("posts");
			out.println("<ol>");
			for(int i=0;i<list.size();i++)
			{
				out.println("<li>"+list.get(i)+"</li>");
				out.println("<br>");
			}
			out.println("</ol>");
			%> 
		</div>	 	
	</div>
	</body>
</html>

<!DOCTYPE html>
<html>
	<head>
	<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
	<title>Search Home Page</title>

	<!-- web-fonts -->
	<link href='http://fonts.googleapis.com/css?family=Montserrat:400,700' rel='stylesheet' type='text/css'>
	<!-- font-awesome -->
	<link href="fonts/font-awesome/css/font-awesome.min.css" rel="stylesheet">
	<!-- Style CSS -->
	<link href="CSS/style.css" rel="stylesheet">

	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
      <![endif]-->
      <script type="text/javascript">
      function myFunction()
      {
    	  //alert('hi');
    	  document.getElementById("tSearch").value=<%=request.getAttribute("query").toString()%>;
    	 
      }
      </script>
  </head>
	
	</head>
	<body onload="myFunction();" background="img/N2.jpg" >
		
		<div class="outer">
		<div>
			<div class="inner">
  	<section class="wraper">
  		<header class="header">
  			<h1><a href="http://52.43.176.194:8080/IRFinalProject/HTML/index_v1.html">BAZINGA SEARCH</a></h1>
  			<h2>When it's Demonetization Info that you are looking for.</h2>
  		</header>


  		<section class="subscribe">
  			<form class="subscribe-form" role="form" method="post" action="http://52.43.176.194:8080/IRFinalProject/SearchServlet">
  				<input type="text"  placeholder="Ask Bazinga a question" id="tSearch" name="tSearch" required="required" oninvalid="this.setCustomValidity('Please enter a query !')"> <!--class="form-control" id="exampleInputEmail1" -->
  				<input type="submit" value="Search">
  			</form>
  		</section><!-- .subscribe-->
  		
  	</section>
			</div>
			<br><br><br><br><br><br>
			<center style="font-weight: bold; font-size:125%;">Query: <%out.println(request.getAttribute("query"));%></center>
			
			
			<center><p style="font-style:italic;font-size:110%;">This is what the Twitteratis have to say :</p></center>
			
			<%@ page import="java.util.List"%>
		
			<%List<String> list=(List<String>)request.getAttribute("posts");
			if(list.size()==0)
			{
				out.println("Sorry !! This query had no responses. Please try some other query..");
			}
			else
			{
				out.println("<center>");
				out.println("<ol>");
				for(int i=0;i<list.size();i++)
				{	
					out.print(i+1);
					out.print("<blockquote class=\"twitter-tweet\">"+list.get(i).replace("[","").replace("]","")+"</blockquote>");
					out.print("<br>");
					//out.println("<br>");
				}
				out.println("</ol>");
				out.println("</center>");
			}
			%> 
		</div>
	</div>
	<div class="fullscreen-bg">
  		<video loop muted autoplay poster="img/videoframe.jpg" class="fullscreen-bg__video">
  			<source src="../img/video-bg.mp4" type="video/mp4">
  		</video>
  	</div>
	</body>
</html>



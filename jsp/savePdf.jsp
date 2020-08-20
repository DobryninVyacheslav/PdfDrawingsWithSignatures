<%@page language="java"%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@page import="java.lang.*"%>
<%@page import="ru.ruselprom.signs.*"%>
<%@page import="ru.ruselprom.signs.data.*"%>
<%@page import="ru.ruselprom.signs.exceptions.*"%>

<html>
 <head>
  <meta charset="utf-8">
  <title>Фон</title>
   <style>
	body {
		background: #000 url(bg.png); /* Фоновый цвет и фоновый рисунок*/
		color: #fff; /*Цвет текста на странице*/
	}
   </style>
 </head>
 <body>
	 <% 	
		String oid = request.getParameter("oid").toString();
		String filePath = "D:\\Ruselprom\\Projects\\pdf with signs\\";
		out.println(oid);
		SignaturesApp signaturesApp  = new SignaturesApp();
		String result = signaturesApp.start(oid, filePath);
		out.println("<br><h1>" + result + "<h1>");
	%>
 </body>
</html>
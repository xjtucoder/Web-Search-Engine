<%@ page language="java" import="java.util.*,com.vo.*,java.sql.*" pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
List WebList=(List)session.getAttribute("WebList");
KeyWord keyword=(KeyWord)session.getAttribute("KeyWords");
Integer counts = WebList.size();
WebPage[] WebArray=(WebPage[])WebList.toArray(new WebPage[counts]);
%>
<%! 
public static final int PAGESIZE = 10;
int pageCount;
int curPage = 1;
//int curId = 0;
%>
<%
  pageCount = (counts%PAGESIZE==0)?(counts/PAGESIZE):(counts/PAGESIZE+1);  
  String tmp = request.getParameter("curPage"); 
  //String tnp = request.getParameter("curId");
  if(tmp==null){  
      tmp="1";  
  }  
  curPage = Integer.parseInt(tmp);  
  int curId = curPage*PAGESIZE-10;
  if(curPage>=pageCount) curPage = pageCount;  
    
  int count = 0;   
 %>
<html>
  <head>
    <title>XJTU Search Engine - Search Results</title>
    <style type="text/css">
     body{    
      background-image: url(images/2.jpg);    
      background-repeat: repeat-x;    
      } 
    </style>
  </head>
  <body>
  
  <form name="form_res" action="Search" method="post">
  <br>
  <h2 style="font-family:verdana">
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;西交搜索 
  <input type="text" name="keywords" style="width:450px;height:20px;font-size:70%;" value="<%=keyword.getKeywords()%>"></input>
  <input type="submit"  style="width:50px;height:25px;font-size:70%;" value="搜索"></input></h2>
  </form>
  <hr width="宽度" height="1"/> 
  <table border="0" width="1000" align="center">
  <%
  int i;
  for(i=curId;i<counts;i++)
  {
  	if(count >= PAGESIZE)break;
  	count++; 
   %><tr align="center"><td width="1000">&nbsp;</tr>
   <tr align="center"><td width="1000"><%=WebArray[i].getTitle()%><a href=<%=WebArray[i].getUrl() %> target="_blank"> <% if(WebArray[i].getUrl().length() > 40) 
       out.print(WebArray[i].getUrl().substring(0,40)); else out.print(WebArray[i].getUrl());%> </a></tr>
   <%
   } 
   %>

  </table>
  <br><br>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <%if(curPage != 1) {%>
  <a href = "SearchResults.jsp?curPage=1&curId=0" >首页</a>   
  <a href = "SearchResults.jsp?curPage=<%=curPage-1%>" >上一页</a>  
  <%
  }else {
  %>
  首页&nbsp;&nbsp;上一页
  <%
  }
  %>
  <a href = "SearchResults.jsp?curPage=<%=curPage+1%>" >下一页</a>  
  <a href = "SearchResults.jsp?curPage=<%=pageCount%>" >尾页</a>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
第<%=curPage%>页/共<%=pageCount%>页  
  </body>
</html>

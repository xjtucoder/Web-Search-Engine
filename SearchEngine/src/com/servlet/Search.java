package com.servlet;

import com.vo.KeyWord;
import com.vo.WebPage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class Search extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        
        //取到jsp中搜索词
        String KeyWords = request.getParameter("keywords");
        //未输入关键词返回搜索主页
        System.out.println(KeyWords);
        if(KeyWords==null || KeyWords=="")
        {
        	response.sendRedirect("/SearchEngine/index.jsp");
        }
        else{
        KeyWord kw = new KeyWord();
        kw.setKeywords(KeyWords);
        String INDEX_DIR = "D:\\luceneIndex";
        Analyzer analyzer = null;
        Directory directory = null;
        ScoreDoc[] hits = null;
        //String sql1 = null;
        List<WebPage> WebList=new ArrayList<WebPage>();
        
        //查索引，搜索关键词
        try{
            directory = FSDirectory.open(new File(INDEX_DIR));
            analyzer = new StandardAnalyzer(Version.LUCENE_40);
            DirectoryReader ireader = DirectoryReader.open(directory);
            IndexSearcher isearcher = new IndexSearcher(ireader);
        	
            String[] fields = {"title","content"}; 
            QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer);
            parser.setDefaultOperator(QueryParser.AND_OPERATOR);
            
    
            Query query = parser.parse(KeyWords);
            TopDocs results= isearcher.search(query, 5000);
            hits = results.scoreDocs;
            
            //sql1 = "select title, url from webpage where id=";
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                WebPage st=new WebPage();
                st.setTitle(hitDoc.get("title"));
                st.setUrl(hitDoc.get("url"));
                WebList.add(st);
            }
            ireader.close();
            directory.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //到数据库中去验证
  		Connection ct=null; 
  		int rs= 0;
  		Statement ps=null;
  		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 		Date now = new Date();
  		String time = sdf.format(now);
  		String sql = "insert into log (keyword, url, time) values ('"+KeyWords+"', ' ', '"+time+"')";
  		try {
  			//加载驱动
  			Class.forName("com.mysql.jdbc.Driver");
  			//得到连接
  			ct=DriverManager.getConnection("jdbc:mysql://localhost:3306/searchengine?characterEncoding=utf-8", "root", "123456");
  			//创建PreparedStatement
  		    ps=ct.createStatement();
  			//执行操作
  			rs=ps.executeUpdate(sql);

  			//处理结果
  			 if(rs>0){
  		  	     System.out.println("insert log successfully!");
  		  	   }else{
  		  	     System.out.println("insert log unsuccessfully!");
  		  	     }
  		}catch (ClassNotFoundException e) {
  			e.printStackTrace();
  		  	}catch (SQLException e){
  		  		e.printStackTrace();
  		  	}finally{
  				try {
  					ps.close();
  				}catch (SQLException e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				}
  				//关闭资源
  	            try {
  					ct.close();
  				} catch (SQLException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
  					
  			HttpSession session;
  			session=request.getSession(true);
  			session.setAttribute("WebList", WebList);
  			session.setAttribute("KeyWords",kw);
            response.sendRedirect("/SearchEngine/SearchResults.jsp");
  		  	}
  		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
          
		this.doGet(request, response);
	}

}

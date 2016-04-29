package index_test;



import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class IndexCreator{
	

    private static String INDEX_DIR = "D:\\luceneIndex";
    private static Analyzer analyzer = null;
    private static Directory directory = null;
    private static IndexWriter indexWriter = null;   

    public static boolean createIndex( ){
        Date date1 = new Date();
        String sql = "select * from webpage";
        
        //到数据库中去验证
  		Connection ct=null; 
  		ResultSet rs=null;
  		Statement ps=null;
  		List<Webpage> WebList=new ArrayList<Webpage>();
  		try {

  			Class.forName("com.mysql.jdbc.Driver");
  			ct=DriverManager.getConnection("jdbc:mysql://localhost:3306/searchengine?characterEncoding=utf-8", "root", "123456");
  		    ps=ct.createStatement();
  			rs=ps.executeQuery(sql);

  			//处理结果
  			while(rs.next()){
  				Webpage wp = new Webpage();
  				wp.setId(rs.getString(1));
  				wp.setTitle(rs.getString(2));
  				wp.setContent(rs.getString(3));
  				wp.setUrl(rs.getString(4));
  				WebList.add(wp);
  			}
  		
  		} catch (ClassNotFoundException e) {
  			e.printStackTrace();
  		} catch (SQLException e){
  			e.printStackTrace();
  		} finally{
  			if(rs!=null){
  				try {
  					rs.close();
  				} catch (SQLException e) {
  					e.printStackTrace();
  				}
  			}
  		}
        
        for (Webpage web : WebList) {           
            try{
                analyzer = new StandardAnalyzer(Version.LUCENE_40);
                directory = FSDirectory.open(new File(INDEX_DIR));
    
                File indexFile = new File(INDEX_DIR);
                if (!indexFile.exists()) {
                    indexFile.mkdirs();
                }
                IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
                indexWriter = new IndexWriter(directory, config);
                
                Document document = new Document();
                document.add(new TextField("title", web.getTitle(), Store.YES));
                document.add(new TextField("content", web.getContent(), Store.YES));
                document.add(new TextField("url", web.getUrl(), Store.YES));
                indexWriter.addDocument(document);
                indexWriter.commit();
                closeWriter();
    
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        Date date2 = new Date();
        System.out.println("创建索引-----耗时：" + (date2.getTime() - date1.getTime()) + "ms\n");
        return true;
    }
    
    public static void closeWriter() throws Exception {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }
    
    /**
     * 删除文件目录下的所有文件
     * @param file 要删除的文件目录
     * @return 如果成功，返回true.
     */
    public static boolean deleteDir(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(int i=0; i<files.length; i++){
                deleteDir(files[i]);
            }
        }
        file.delete();
        return true;
    }
    
    public static void main(String[] args){
        File fileIndex = new File(INDEX_DIR);
        if(deleteDir(fileIndex)){
            fileIndex.mkdir();
        }else{
            fileIndex.mkdir();
        }
        
        createIndex();
    }
}
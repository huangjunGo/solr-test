import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class SolrExample {
	private HttpSolrClient solrClient;
	
	/**
	 * 初始化
	 * @since 1.0 
	 * <br><b>作者： @author huangjun</b>
	 * <br>创建时间：2016年7月26日 上午11:43:29
	 */
	@Before
	public void before(){
		solrClient = new HttpSolrClient("http://127.0.0.1:8080/solr/mynode");
	}
	
	/**
	 * 提交 关闭
	 * @since 1.0 
	 * @throws Exception
	 * <br><b>作者： @author huangjun</b>
	 * <br>创建时间：2016年7月26日 上午11:43:35
	 */
	@After
	public void after() throws Exception{
		solrClient.commit();
		solrClient.close();
	}
	
	/**
	 * 添加一条记录
	 * @since 1.0 
	 * @throws SolrServerException
	 * @throws IOException
	 * <br><b>作者： @author huangjun</b>
	 * <br>创建时间：2016年7月26日 上午11:43:49
	 */
	@Test
	public void addOne() throws SolrServerException, IOException{
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", 8888);
		document.addField("number", 123);
		document.addField("updateTime", new Date());
		document.addField("name", "123124124124");
		solrClient.add(document);
	}
	
	/**
	 * 添加多条
	 * @since 1.0 
	 * @throws SolrServerException
	 * @throws IOException
	 * <br><b>作者： @author huangjun</b>
	 * <br>创建时间：2016年7月26日 上午11:43:58
	 */
	@Test
	public void addList() throws SolrServerException, IOException{
		List<SolrInputDocument> list = new ArrayList<SolrInputDocument>();
		for (int i = 400; i < 600; i++) {
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", i);
			document.addField("number", i);
			document.addField("updateTime", new Date());
			document.addField("name", "黄俊"+i);
			list.add(document);
		}
		solrClient.add(list);
	}
	
	@Test
	public void query() throws SolrServerException, IOException{
		SolrQuery query = new SolrQuery("name:黄俊*");//查询条件
		query.set("fl", "id,name,number");//返回字段
		query.setSort("number", SolrQuery.ORDER.desc);//排序
		query.setHighlight(true);//是否高亮
		query.setHighlightFragsize(2);
		query.setHighlightSimplePre("<font color=\"red\">");
		query.setHighlightSimplePost("</font>");
		query.setParam("hl.fl", "name");
		query.setStart(20);
		query.setRows(10);
		
		
		QueryResponse response = solrClient.query(query);
		List<User> list = response.getBeans(User.class);
		Assert.assertNotNull(list);
	}
	
}

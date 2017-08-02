package mycourse.onkshare.search.test;

import mycourse.onkshare.mapper.FileMapper;
import mycourse.onkshare.model.File;
import mycourse.onkshare.model.FileMap;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by doll on 3/9.
 */
public class SolrTest {

    //public ApplicationContext context = new ClassPathXmlApplicationContext("servlet-config.xml");

    private HttpSolrClient client;

    @Before
    public void before() {
        String url = "http://localhost:8100/solr/onkshare";
        client = new HttpSolrClient(url);
        client.setParser(new XMLResponseParser());
        client.setConnectionTimeout(500);
    }

    @Test
    public void addTest() throws Exception{
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");
        SqlSessionTemplate template = context.getBean(SqlSessionTemplate.class);
        FileMapper mapper = template.getMapper(FileMapper.class);
        File file = mapper.selectByPrimaryKey(1101L);
        SolrInputDocument inputDocument = new SolrInputDocument();
        inputDocument.setField("id", file.getId());
        inputDocument.setField("file_name", file.getName());
        inputDocument.setField("file_attraction", file.getAttraction());
        inputDocument.setField("file_status", file.getStatus());
        inputDocument.setField("file_parentId", file.getParentId());
        inputDocument.setField("file_updated", file.getUpdated());
        client.add(inputDocument);
        client.commit();
    }

    @Test
    public void queryTest() throws Exception{
        String keyword = "root";
        int page = 0;
        int rows = 10;

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("file_name:" + keyword);;
        solrQuery.setStart(page);
        solrQuery.setRows(rows);

        QueryResponse queryResponse = client.query(solrQuery);
        SolrDocumentList results = queryResponse.getResults();
        for (SolrDocument solrDocument: results) {
            System.out.println(solrDocument);
        }
    }

    @Test
    public void testFullInsert() {
        return ;

    }
}

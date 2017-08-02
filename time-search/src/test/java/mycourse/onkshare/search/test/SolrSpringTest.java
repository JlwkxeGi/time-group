package mycourse.onkshare.search.test;

import mycourse.onkshare.mapper.FileMapper;
import mycourse.onkshare.model.File;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by doll on 3/9.
 */
public class SolrSpringTest {

    public ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");


    @Test
    public void test() {
        Object o  = context.getBean("solrClient");
        System.out.println(o);
    }
}

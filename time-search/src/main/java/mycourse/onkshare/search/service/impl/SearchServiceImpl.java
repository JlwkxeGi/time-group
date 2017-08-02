package mycourse.onkshare.search.service.impl;

import mycourse.onkshare.mapper.FileMapper;
import mycourse.onkshare.model.File;
import mycourse.onkshare.model.result.Result;
import mycourse.onkshare.model.result.SearchResult;
import mycourse.onkshare.search.service.SearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.springframework.data.solr.core.SolrCallback;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * Created by doll on 3/8.
 */
@Service("searchService")
public class SearchServiceImpl implements SearchService {

    @Resource
    FileMapper fileMapper;

    @Resource
    SolrTemplate solrTemplate;

    public List<File> findFile(Map map) {
        return null;
    };

    @Override
    public boolean importAll() {
        List<File> files = fileMapper.selectByExample(null);
        Collection<SolrInputDocument> solrDocuments = new ArrayList<>();

        for (File file:files) {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", file.getId());
            document.addField("file_name", file.getName());
            document.addField("file_children", file.getChildren());
            document.addField("file_attraction", file.getAttraction());
            document.addField("file_parentId", file.getParentId());
            document.addField("file_status", file.getStatus());
            document.addField("file_updated", file.getUpdated());
            document.addField("file_created", file.getCreated());
            document.addField("file_level", file.getLevel());
            document.addField("file_type", file.getType());
            document.addField("file_size", file.getSize());
            document.addField("file_url", file.getUrl());
            document.addField("file_user_id", file.getUserId());
            solrDocuments.add(document);
        }
        UpdateResponse updateResponse = solrTemplate.saveDocuments(solrDocuments);
        int status = updateResponse.getStatus();
        solrTemplate.commit();
        return false;
    }

    @Override
    public SearchResult search(final String keyword, final Integer startPage, final Integer pageSize, final Integer shared) {

        final SearchResult searchResult = new SearchResult();
        searchResult.setPageSize(pageSize);
        searchResult.setPageNumber(startPage);

        List results = solrTemplate.execute(new SolrCallback<List>() {
            @Override
            public List doInSolr(SolrClient solrClient) throws SolrServerException, IOException {
                StringBuffer condtions = new StringBuffer("file_name:" + keyword);
                if (shared != -1) {
                    condtions.append(" AND file_status:" + shared);
                }

                SolrQuery params  = new SolrQuery();
                params.setStart((searchResult.getPageNumber()-1)*searchResult.getPageSize());
                params.setRows(searchResult.getPageSize());
                params.setQuery(condtions.toString());
                params.setHighlight(true);
                params.setHighlightSimplePre("<font color='red'>");
                params.setHighlightSimplePost("</font>");
                params.addHighlightField("file_name");

                QueryResponse query = solrClient.query(params);
                SolrDocumentList results = query.getResults();
                searchResult.setTotal(results.getNumFound());
                Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();

                List list = new ArrayList();
                for (SolrDocument result: results) {
                    String file_highlighting = (String) highlighting.get(result.get("id")).get("file_name").get(0);
                    String file_name = (String)result.get("file_name");
                    String file_id = (String) result.get("id");
                    Long file_parentId = (Long) result.get("file_parent_id");
                    Integer file_children = (Integer) result.get("file_children");
                    Date file_updated = (Date) result.get("file_updated");
                    Date file_created = (Date) result.get("file_created");
                    String file_attraction = (String) result.get("file_attraction");
                    Integer file_status = (Integer) result.get("file_status");
                    String file_type = (String)result.get("file_type");
                    Long file_size = (Long) result.get("file_size");
                    String file_url = (String)result.get("file_url");
                    Integer file_level = (Integer) result.get("file_level");
                    Long file_user_id = (Long) result.get("file_user_id");


                    Map map = new HashMap();
                    map.put("id", file_id);
                    map.put("name", file_name);
                    map.put("text", file_highlighting);
                    map.put("children", file_children);
                    map.put("attraction", file_attraction == null ? "" : file_attraction);
                    map.put("parentId", file_parentId);
                    map.put("status", file_status);
                    map.put("updated", file_updated);
                    map.put("created", file_created);
                    map.put("type", file_type);
                    map.put("size", file_size);
                    map.put("url", file_url);
                    map.put("level", file_level);
                    map.put("userId", file_user_id);
                    list.add(map);
                }

                return list;
            }
        });

        searchResult.setRows(results);
        //searchResult.setTotal(results.size());

        return searchResult;
    }
}

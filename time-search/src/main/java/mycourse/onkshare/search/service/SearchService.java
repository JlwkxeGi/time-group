package mycourse.onkshare.search.service;

import mycourse.onkshare.model.result.Result;
import mycourse.onkshare.model.result.SearchResult;

import java.util.List;

/**
 * Created by doll on 3/8.
 */
public interface SearchService {

    public boolean importAll();
    public SearchResult search(String keyword, Integer startPage, Integer pageSize, Integer shared);

}
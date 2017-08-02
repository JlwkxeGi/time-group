package mycourse.onkshare.search.web;

import mycourse.onkshare.model.result.Result;
import mycourse.onkshare.model.result.SearchResult;
import mycourse.onkshare.search.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by doll on 3/8.
 */
@Controller
public class SearchController extends BaseController{

    @Resource
    SearchService searchService;

    @RequestMapping("/importAll")
    @ResponseBody
    public boolean importAll() {
        boolean b = searchService.importAll();
        return b;
    }

    @RequestMapping("/search")
    @ResponseBody
    public Result search(@RequestParam(value = "q", required = true, defaultValue = "") String keyword,
                         @RequestParam(value = "shared", defaultValue="1") Integer shared,
                         @RequestParam(value = "s", defaultValue="1") Integer startPage,
                         @RequestParam(value = "e", defaultValue = "10") Integer pageSize) {

        if ("".equals(keyword)) {
            return Result.success("keyword为空!!");
        }
        SearchResult search = searchService.search(keyword, startPage, pageSize, shared);
        return search;
    }

}

package mycourse.onkshare.manager.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController extends BaseController{

	@RequestMapping(value = "/")
	public ModelAndView index() {
		response.setContentType("text/html;charset=utf-8");
		return new ModelAndView("index");
	}
	
	@RequestMapping("/{page}")
	public String page(@PathVariable String page) {
		return page;
	}
}

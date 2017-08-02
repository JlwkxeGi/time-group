package mycourse.onkshare.client.web;

import mycourse.onkshare.constant.WebConstant;
import mycourse.onkshare.stereotype.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController extends BaseController{

	@RequestMapping(value = "/")
	public ModelAndView index() {
		response.setContentType("text/html;charset=utf-8");
		ModelAndView view = new ModelAndView("index");
		view.addObject("basepath", WebConstant.BASEPATH);
		return view;
	}

	@RequestMapping(value = "/index")
	public ModelAndView index1() {
		response.setContentType("text/html;charset=utf-8");
		ModelAndView view = new ModelAndView("index");
		view.addObject("basepath", WebConstant.BASEPATH);
		return view;
	}

	@RequestMapping("/page/{page}")
	@Permission(value = Permission.USER)
	public String page2(@PathVariable String page) {
		return page;
	}
	
	@RequestMapping("/{page}")
	@Permission
	public String page(@PathVariable String page) {
		return page;
	}


}

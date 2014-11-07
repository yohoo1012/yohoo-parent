package yo.hoo.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyController {
	
	@RequestMapping("/")
	public String home(){
		return new String("redirect:emr/gis.html");
	}
	
	@RequestMapping("/jsp")
	public String jsp(){
		return new String("index.jsp");
	}
	
	@RequestMapping("/ftl")
	public String ftl(){
		return new String("index.ftl");
	}

}

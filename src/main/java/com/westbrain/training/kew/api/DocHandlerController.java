package com.westbrain.training.kew.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docHandler")
public class DocHandlerController {

	@RequestMapping("")
	public String index() {
		return "index.html";
	}
	
}

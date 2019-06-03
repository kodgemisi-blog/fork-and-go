package com.kodgemisi.blog.dynamicauthorization;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created on May, 2019
 *
 * @author destan
 */
@Controller
@RequestMapping("/")
class HomeController {

	@GetMapping
	String home() {
		return "index";
	}

}

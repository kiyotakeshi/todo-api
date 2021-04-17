package com.kiyotakeshi.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApiReferenceController {

	/**
	 * return API reference
	 * @return api-reference.html
	 */
	@RequestMapping({ "/", "/index", "/api" })
	public String index() {
		return "api-reference";
	}

}

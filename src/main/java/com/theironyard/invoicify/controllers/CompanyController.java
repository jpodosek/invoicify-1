package com.theironyard.invoicify.controllers;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.Company;
import com.theironyard.invoicify.models.Invoice;
import com.theironyard.invoicify.repositories.CompanyRepository;
import com.theironyard.invoicify.repositories.InvoiceRepository;

@Controller
@RequestMapping("/admin/companies")
public class CompanyController {
	
	private CompanyRepository companyRepo;
	private InvoiceRepository invoiceRepo;
	
	CompanyController(CompanyRepository companyRepo, InvoiceRepository invoiceRepo) {
		this.companyRepo = companyRepo;
		this.invoiceRepo = invoiceRepo;
	}
	
	@GetMapping("")
	public ModelAndView list() {
		//List<Company> companies = companyRepo.findAll();
		List<Company> companies = companyRepo.findAll(new Sort("name"));
		ModelAndView mv = new ModelAndView("companies/list");
		mv.addObject("companies", companies );
		return mv;
	}
	
	@PostMapping("")
	public ModelAndView create(String name) {
		ModelAndView mv = new ModelAndView();
		try
		{
			Company company = new Company(name);
			companyRepo.save(company);
			mv.setViewName("redirect:/admin/companies");
		} catch (DataIntegrityViolationException dive)
		{
			//duplicate company name entered; redirect.
			System.err.println(dive);
			List<Company> companies = companyRepo.findAll(new Sort("name"));
			mv.setViewName("companies/list");
			mv.addObject("companies", companies );
			mv.addObject("errorMessage", "That company name already exists.");
		}
		
		return mv;
	}

}

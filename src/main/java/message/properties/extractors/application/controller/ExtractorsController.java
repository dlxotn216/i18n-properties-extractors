package message.properties.extractors.application.controller;

import message.properties.extractors.application.service.ExtractorsService;
import message.properties.extractors.domain.Extractors;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project message-extractor
 * @since 2018-08-15
 */
@Controller
public class ExtractorsController {
	
	private ExtractorsService extractorsService;
	
	public ExtractorsController(ExtractorsService extractorsService) {
		this.extractorsService = extractorsService;
	}
	
	@GetMapping(value = "export-excel", produces = "application/vnd.ms-excel")
	public String export(Model model) {
		Extractors extractors = extractorsService.addAllTargetPathsInDirectory("download");
		model.addAttribute("table", extractors.resolveToLocalePropertyTable());
		return "export-excel-view";
	}
	
	@GetMapping("export-excel.xls")
	public String get(Model model) {
		Extractors extractors = extractorsService.addAllTargetPathsInDirectory("download");
		model.addAttribute("table", extractors.resolveToLocalePropertyTable());
		return "export-excel-view";
	}
	
}

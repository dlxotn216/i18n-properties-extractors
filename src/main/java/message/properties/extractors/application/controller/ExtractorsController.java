package message.properties.extractors.application.controller;

import message.properties.extractors.application.dto.ExtractorsRequest;
import message.properties.extractors.application.service.ExtractorsService;
import message.properties.extractors.domain.Extractors;
import message.properties.file.application.service.FileService;
import message.properties.locale.application.service.LocaleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project message-extractor
 * @since 2018-08-15
 */
@Controller
public class ExtractorsController {

    private ExtractorsService extractorsService;
    private LocaleService localeService;
    private FileService fileService;
	
	public ExtractorsController(ExtractorsService extractorsService, LocaleService localeService, FileService fileService) {
		this.extractorsService = extractorsService;
		this.localeService = localeService;
		this.fileService = fileService;
	}
	
	@GetMapping("extractors")
    public String getExtractorsView(Model model) {
        model.addAttribute("locales", localeService.getLocales());
        return "extractors";
    }

    @PostMapping("extractors")
    public String exportExcelFromRequestPropertiesFile(ExtractorsRequest request, Model model) {
        request.requestValidation();
		Extractors extractors = extractorsService.addAllTargetPathFromUploadedFiles(request);
		model.addAttribute("table", extractors.resolveToLocalePropertyTable(extractors.resolveToProperties()));
		fileService.deleteFiles(extractors.getTargetFilePaths());
		return "export-excel-view";
    }

    @GetMapping(value = "export-excel", produces = "application/vnd.ms-excel")
    public String export(Model model) {
        Extractors extractors = extractorsService.addAllTargetPathsInDirectory("download");
        model.addAttribute("table", extractors.resolveToLocalePropertyTable(extractors.resolveToProperties()));
        return "export-excel-view";
    }

    @GetMapping("export-excel.xls")
    public String get(Model model) {
        Extractors extractors = extractorsService.addAllTargetPathsInDirectory("download");
        model.addAttribute("table", extractors.resolveToLocalePropertyTable(extractors.resolveToProperties()));
        return "export-excel-view";
    }

}

package message.properties.common.application.view;

import message.properties.extractors.application.dto.LocalePropertyRow;
import message.properties.extractors.application.dto.LocalePropertyTable;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project message-extractor
 * @since 2018-08-15
 */
@Component("export-excel-view")
public class ExcelExportView extends AbstractXlsView {
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
									  HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String filename = String.format("i18n_properties (%s).xls", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초")));
		final String attachFilename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		
		response.setHeader("Content-Disposition", "attachment; filename=\"" + attachFilename + "\"");
		LocalePropertyTable table = (LocalePropertyTable) model.get("table");
		
		CellStyle numberCellStyle = workbook.createCellStyle();
		DataFormat numberDataFormat = workbook.createDataFormat();
		numberCellStyle.setDataFormat(numberDataFormat.getFormat("#,##0"));
		
		Sheet sheet = workbook.createSheet("i18n");
		
		//Initial
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Message ID");
		headerRow.createCell(getColumnIndexByLocale(new Locale("base"))).setCellValue(new Locale("base").getDisplayName());
		headerRow.createCell(getColumnIndexByLocale(Locale.ENGLISH)).setCellValue(Locale.ENGLISH.getDisplayName());
		headerRow.createCell(getColumnIndexByLocale(Locale.KOREA)).setCellValue(Locale.KOREA.getDisplayName());
		headerRow.createCell(getColumnIndexByLocale(Locale.SIMPLIFIED_CHINESE)).setCellValue(Locale.SIMPLIFIED_CHINESE.getDisplayName());
		headerRow.createCell(getColumnIndexByLocale(Locale.JAPAN)).setCellValue(Locale.JAPAN.getDisplayName());
		
		table.getRows().sort(Comparator.comparing(LocalePropertyRow::getMessageId));
		
		//rowIndex는 1부터 시작
		IntStream.rangeClosed(1, table.getRows().size())
				.forEach(rowIndex -> {
					Row row = sheet.createRow(rowIndex);
					
					LocalePropertyRow localePropertyRow = table.getRows().get(rowIndex - 1);
					row.createCell(0).setCellValue(localePropertyRow.getMessageId());
					
					localePropertyRow.getElements()
							.forEach(element ->
									row.createCell(getColumnIndexByLocale(element.getLocale())).setCellValue(element.getMessage()));
				});
		
		IntStream.rangeClosed(0, 6).forEach(sheet::autoSizeColumn);
		
	}
	
	private Integer getColumnIndexByLocale(Locale locale) {
		if(locale.getDisplayName().equals(new Locale("base").getDisplayName())) {
			return 1;
		} else if(locale.getDisplayName().equals(Locale.ENGLISH.getDisplayName())) {
			return 2;
		} else if(locale.getDisplayName().equals(Locale.KOREA.getDisplayName())) {
			return 3;
		} else if(locale.getDisplayName().equals(Locale.SIMPLIFIED_CHINESE.getDisplayName())) {
			return 4;
		} else if(locale.getDisplayName().equals(Locale.JAPAN.getDisplayName())) {
			return 5;
		} else {
			return 6;
		}
	}
}

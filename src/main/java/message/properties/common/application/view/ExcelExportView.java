package message.properties.common.application.view;

import message.properties.extractors.application.dto.LocalePropertyRow;
import message.properties.extractors.application.dto.LocalePropertyRowElement;
import message.properties.extractors.application.dto.LocalePropertyTable;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
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
		
		//sorting
		table.getRows().sort(Comparator.comparing(LocalePropertyRow::getMessageId));
		
		final List<Locale> locales = table.getRows().stream()
				.flatMap(localePropertyRow -> localePropertyRow.getElements().stream().map(LocalePropertyRowElement::getLocale))
				.distinct()
				.collect(Collectors.toList());
		
		Sheet sheet = workbook.createSheet("i18n");
		initHeaderColumns(sheet, locales);
		renderExcelRows(sheet, table, locales);
	}
	
	private void initHeaderColumns(Sheet sheet, List<Locale> locales) {
		//Init Header Columns
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Message ID");
		
		IntStream.rangeClosed(1, locales.size())
				.forEach(columnIndex -> headerRow.createCell(columnIndex).setCellValue(locales.get(columnIndex - 1).getDisplayName()));
	}
	
	private void renderExcelRows(Sheet sheet, LocalePropertyTable table, List<Locale> locales) {
		//rowIndex는 1부터 시작
		IntStream.rangeClosed(1, table.getRows().size())
				.forEach(rowIndex -> {
					Row row = sheet.createRow(rowIndex);
					
					LocalePropertyRow localePropertyRow = table.getRows().get(rowIndex - 1);
					row.createCell(0).setCellValue(localePropertyRow.getMessageId());
					
					//columnIndex 1 이후부터 데이터 set
					localePropertyRow.getElements()
							.forEach(element -> row.createCell(getColumnIndexByLocale(locales, element.getLocale()) + 1)
													.setCellValue(element.getMessage()));
				});
		
		//resize all columns
		IntStream.rangeClosed(0, 6).forEach(sheet::autoSizeColumn);
	}
	
	private Integer getColumnIndexByLocale(List<Locale> locales, Locale locale) {
		for(int i = 0, length = locales.size(); i < length; i++) {
			if(locales.get(i) == locale) {
				return i;
			}
		}
		return -1;
	}
}

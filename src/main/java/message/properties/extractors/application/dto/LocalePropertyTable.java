package message.properties.extractors.application.dto;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project message-extractor
 * @since 2018-08-14
 */
@Value
public class LocalePropertyTable {
	private List<LocalePropertyRow> rows = new ArrayList<>();
	
	public void addLocalePropertyRowElement(String messageId, Locale locale, String message) {
		if(isCommentLine(messageId)){
			return;
		}
		
		Optional<LocalePropertyRow> first = rows.stream()
				.filter(localePropertyRow -> localePropertyRow.getMessageId().equals(messageId))
				.findFirst();
		
		if(first.isPresent()){
			first.get().addElement(locale, message);
		} else {
			LocalePropertyRow localePropertyRow = new LocalePropertyRow(messageId);
			localePropertyRow.addElement(locale, message);
			rows.add(localePropertyRow);
		}
		
	}
	
	private Boolean isCommentLine(String messageId){
		return messageId != null && messageId.contains("#");
	}
	
}

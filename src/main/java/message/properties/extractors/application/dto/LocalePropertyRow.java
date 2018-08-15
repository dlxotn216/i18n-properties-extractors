package message.properties.extractors.application.dto;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project message-extractor
 * @since 2018-08-14
 */
@Value
public class LocalePropertyRow {
	private String messageId;
	private List<LocalePropertyRowElement> elements = new ArrayList<>();
	
	public void addElement(Locale locale, String message){
		this.elements.add(new LocalePropertyRowElement(locale, message));
	}
}

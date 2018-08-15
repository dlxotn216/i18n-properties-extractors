package message.properties.extractors.application.dto;

import lombok.Value;

import java.util.Locale;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project message-extractor
 * @since 2018-08-14
 */
@Value
public class LocalePropertyRowElement {
	private Locale locale;
	private String message;
}

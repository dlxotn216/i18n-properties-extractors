package message.properties.extractors.application.dto;

import lombok.Value;

import java.util.Locale;
import java.util.Properties;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project message-extractor
 * @since 2018-08-14
 */
@Value
public class LocaleProperties {
	private Locale locale;
	private Properties properties;
}

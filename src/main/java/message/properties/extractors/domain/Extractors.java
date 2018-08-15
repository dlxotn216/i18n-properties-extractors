package message.properties.extractors.domain;

import lombok.extern.slf4j.Slf4j;
import message.properties.extractors.application.dto.LocaleProperties;
import message.properties.extractors.application.dto.LocalePropertyTable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project message-extractor
 * @since 2018-08-14
 */
@Slf4j
public class Extractors {
	private List<Path> targetFilePaths = new ArrayList<>();
	
	public void addTargetPath(Path path) {
		this.targetFilePaths.add(path);
	}
	
	public List<Path> getTargetFilePaths() {
		return new ArrayList<>(targetFilePaths);
	}
	
	public Stream<LocaleProperties> resolveToProperties() {
		return this.targetFilePaths.stream()
				.map(path -> {
					Properties properties = new Properties();
					try (FileInputStream input = new FileInputStream(path.toFile())){
						properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
					} catch (IOException e) {
						log.warn(path.toString()+"경로의 Properties 파일 로드 실패", e);
					}
					return new LocaleProperties(getLocale(path), properties);
				});
	}
	
	public LocalePropertyTable resolveToLocalePropertyTable() {
		LocalePropertyTable table = new LocalePropertyTable();
		
		this.resolveToProperties()
				.forEach(localeProperties -> localeProperties.getProperties()
						.forEach((key, value) -> table.addLocalePropertyRowElement(key.toString(), localeProperties.getLocale(), value.toString())));
		
		return table;
	}
	
	private Locale getLocale(Path path) {
		if(path.toString().contains("_en")) {
			return Locale.ENGLISH;
		} else if(path.toString().contains("_ko")) {
			return Locale.KOREA;
		} else if(path.toString().contains("_cn")) {
			return Locale.SIMPLIFIED_CHINESE;
		} else if(path.toString().contains("_jp")) {
			return Locale.JAPAN;
		} else {
			return new Locale("base");
		}
	}
}

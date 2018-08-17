package message.properties.extractors.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;

/**
 * Created by taesu on 2018-08-16.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExtractorsRequest {
	private List<MultipartFile> files;
	private List<Locale> locales;
	
	public void requestValidation() {
		if(CollectionUtils.isEmpty(files) || CollectionUtils.isEmpty(locales)) {
			throw new IllegalArgumentException("파일 또는 Locale 정보가 비어있습니다");
		}
		
		if(!ObjectUtils.nullSafeEquals(files.size(), locales.size())) {
			throw new IllegalArgumentException("파일의 개수와 Locale 정보의 개수가 일치하지 않습니다");
		}
		
		files.stream().filter(multipartFile -> multipartFile.getSize() <= 0)
				.findAny().ifPresent(multipartFile -> {
			throw new IllegalStateException("비어있는 file parameter가 있습니다");
		});
	}
}

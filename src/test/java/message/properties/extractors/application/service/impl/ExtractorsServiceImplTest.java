package message.properties.extractors.application.service.impl;

import message.properties.extractors.application.dto.LocalePropertyRow;
import message.properties.extractors.application.dto.LocalePropertyTable;
import message.properties.extractors.application.service.ExtractorsService;
import message.properties.extractors.domain.Extractors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Lee Tae Su on 2018-08-14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExtractorsServiceImplTest {
	
	@Autowired
	private ExtractorsService extractorsService;
	
	@Test
	public void 타겟_Path_추가_테스트() {
		//Given
		//When
		Extractors extractors = extractorsService.addTargetPath("download/base.properties");
		
		//Then
		assertThat(extractors.getTargetFilePaths().size()).isEqualTo(1);
		extractors.getTargetFilePaths().forEach(path -> assertThat(Files.exists(path)).isTrue());
	}
	
	@Test
	public void 하위의_모든_properties파일_추가_테스트() {
		//Given	
		//When
		Extractors extractors = extractorsService.addAllTargetPathsInDirectory("download");
		
		//Then
		assertThat(extractors.getTargetFilePaths().size()).isEqualTo(5);
		extractors.getTargetFilePaths().forEach(path -> assertThat(Files.exists(path)).isTrue());
	}
	
	@Test
	public void 모든_properties_파일_경로로부터_Properties_생성_테스트() {
		//Given
		//When
		Extractors extractors = extractorsService.addAllTargetPathsInDirectory("download");
		
		//Then
		extractors.resolveToProperties()
				.forEach(localeProperties -> {
					System.out.println(localeProperties.getLocale());
					localeProperties.getProperties().forEach((key, value) -> System.out.println(key + "=" + value));
					
					System.out.println("\n\n");
				});
		
	}
	
	@Test
	public void 테이블_구성_테스트() {
		//Given		
		//When
		Extractors extractors = extractorsService.addAllTargetPathsInDirectory("download");
		
		//Then
		LocalePropertyTable table = extractors.resolveToLocalePropertyTable(extractors.resolveToProperties());
		table.getRows().stream()
				.sorted(Comparator.comparing(LocalePropertyRow::getMessageId))
				.forEach(localePropertyRow -> {
					System.out.print(localePropertyRow.getMessageId() + " ->  ");
					localePropertyRow.getElements().forEach(localePropertyRowElement -> {
						System.out.print(localePropertyRowElement.getLocale() + "::" + localePropertyRowElement.getMessage());
						System.out.print("  ");
					});
					System.out.println();
				});
		
	}
}
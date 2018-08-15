##### Properties 파일 형식으로 관리하는 다국어 정보를 Excel 형태로 출력하는 프로젝트.

##### 1. 사용법

 (1) /src/main/webapp/download 디렉토리 하위에   
 Application에서 사용하는 다국어 정보가 존재하는 properties 파일을 배치  
 * 지원하는 Locale은 en, ko, jp, cn
 * 각각의 Locale에 대한 구분은 properties 파일명에 _en, _ko, _jp, _cn으로 한다.
 * 그 외엔 모두 base라는 Default Locale로 처리한다
 
 (2) 프로젝트를 실행  

 (3) 브라우저에 localhost:8080/export-excel.xls 주소로 접속하여 Excel 파일을 다운로드
  
  
 ##### 2. 사용기술
 
 (1) 기본 로직  
 기본적인 로직의 처리를 Java8의 API를 사용하려고 하였으며  
 Directory 하위 검색 등의 File과 관련된 작업은 Java7의 API(Path, FileVisitor 등)을 사용

 (2) 프로젝트 환경  
 * Spring boot2.0 기반 WebMVC  
 * Apache POI  
 
 
 (3) 패키지 구성  
 DDD Layer 위주로 구성
  
 ##### 3. TODO List
 * properties 파일을 파일 업로드 형태로 받을 수 있도록 처리  
 * properties 파일 외에도 Java의 Properties 객체가 load 할 수 있는 파일에 대해서 처리  
 * Locale 정보를 _en, _jp, _ko, _cn 등의 하드코딩이 아닌 Java에서 정의한 Locale을 통해 처리하도록 개선
 * 파일 업로드를 지원한다면 Locale을 파일 명이 아닌 Request로부터 얻어오도록 처리
 * Download URI에 .xls와 같이 확장자가 존재하는데 ContentNegotiatingViewResolver를   
   관련 설정을 변경하여  Controller에서 반환하는 이름으로 View를 찾을 수 있도록 개선    
   
   -> 처리 완료  
   BeanNameViewResolver의 순서를 ContentNegotiatingViewResolver 보다 앞에 적용    

   
   
```java
	@Configuration
	public class ViewResolverConfig {
		@Bean
		public ViewResolver viewResolver(){
			BeanNameViewResolver beanNameViewResolver = new BeanNameViewResolver();
			beanNameViewResolver.setOrder(0);
			return beanNameViewResolver;
		}
	}
```
	
(사실 제일 좋은것은 MediaType을 이용하여 요청하고 ContentNegotiatingViewResolver를 이용하는 것)


 ##### 4. ISSUE tracking
 (1) properties 파일을 Load한 후 encoding이 깨지는 문제 발생  
 * properties 파일을 Java의 Properties에 load 할 때 encoding을 지정하지 않아 발생
 
```java
	Properties properties = new Properties();
	try (FileInputStream input = new FileInputStream(path.toFile())){
		properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));	//encoding 처리
	} catch (IOException e) {
		log.warn(path.toString()+"경로의 Properties 파일 로드 실패", e);
	}
```

(2) InputStreamReader에 encoding을 명시하였으나 그래도 깨지는 문제 발생
  * IDE에서 encoding 설정에 문제가 있었음
  * Intellij 기준 ctrl+alt+s -> File encoding 메뉴에서 UTF-8로 변경

(3) Excel export 시 POI의 autoSizeColumn 설정을 처리하였으나 제대로 적용되지 않음
 * POI에서 autoSizeColumn 관련 로직을 처리하는 시점이 Cell을 생성하는 시점이 아닌 모든 Cell이 입력된 후에 처리 함
 * 아래와 같이 모든 Cell을 생성하고 마지막에 sheet.autoSizeColumn()을 적용
 
```java
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
```

(4) Export 시 파일명 encoding 문제
* 년월일시분초 관련 정보를 포함하려 DateFormatter를 사용하여 파일명을 지정하였으나 인코딩이 깨짐
* 아래와 같이 파일명 문자열의 인코딩을 UTF-8에서 ISO-8859-1 형태로 변경
```java
	final String filename = String.format("i18n_properties (%s).xls", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초")));
	final String attachFilename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
```
package message.properties.extractors.application.service.impl;

import lombok.extern.slf4j.Slf4j;
import message.properties.extractors.application.service.ExtractorsService;
import message.properties.extractors.domain.Extractors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project message-extractor
 * @since 2018-08-14
 */
@Service
@Slf4j
public class ExtractorsServiceImpl implements ExtractorsService {
	
	private final PathMatcher propertiesFileMatcher = FileSystems.getDefault().getPathMatcher("glob:**.{properties}");
	
	@Autowired
	private ServletContext servletContext;
	
	@Override
	public Extractors addAllTargetPathsInDirectory(String relativePath) {
		Extractors extractors = new Extractors();
		Path directoryPath = Paths.get(servletContext.getRealPath("/")).resolve(relativePath);
		
		if(!Files.isDirectory(directoryPath)) {
			return extractors;
		}
		
		FileVisitor<Path> visitor = new FileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if(propertiesFileMatcher.matches(file)) {
					log.info("addTarget : {}", file);
					extractors.addTargetPath(file);
				}
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		};
		
		try {
			Files.walkFileTree(directoryPath, visitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return extractors;
	}
	
	@Override
	public Extractors addAllTargetPaths(String... relativePaths) {
		Extractors extractors = new Extractors();
		Arrays.asList(relativePaths).forEach(relativePath 
				-> extractors.addTargetPath(Paths.get(servletContext.getRealPath("/")).resolve(relativePath)));
		return extractors;
	}
	
	@Override
	public Extractors addTargetPath(String relativePath) {
		Extractors extractors = new Extractors();
		if(StringUtils.isEmpty(relativePath)) {
			return extractors;
		}
		
		extractors.addTargetPath(Paths.get(servletContext.getRealPath("/")).resolve(relativePath));
		return extractors;
	}
}

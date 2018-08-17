package message.properties.file.application.service.impl;

import message.properties.file.application.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project message-extractor
 * @since 2018-08-17
 */
@Service
public class FileServiceImpl implements FileService {
	@Autowired
	private ServletContext servletContext;
	
	@Override
	public List<Path> uploadFiles(List<MultipartFile> files) {
		final Path directoryPath = Paths.get(servletContext.getRealPath("/")).resolve("download").resolve("temporal");
		try {
			Files.createDirectories(directoryPath);
		} catch (IOException e) {
			//
		}
		return files.stream()
				.map(multipartFile -> uploadFile(multipartFile, directoryPath))
				.collect(Collectors.toList());
	}
	
	@Override
	public Path uploadFile(MultipartFile multipartFile, Path directoryPath) {
		final Path path = directoryPath.resolve(multipartFile.getOriginalFilename());
		try {
			multipartFile.transferTo(path.toFile());
		} catch (IOException e) {
			
		}
		return path;
	}
	
	@Override
	public void deleteFiles(List<Path> paths) {
		paths.forEach(this::deleteFile);
	}
	
	@Override
	public void deleteFile(Path path) {
		try {
			Files.delete(path);
		} catch (IOException e) {
			
		}
	}
}

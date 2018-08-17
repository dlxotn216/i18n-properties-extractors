package message.properties.file.application.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project message-extractor
 * @since 2018-08-17
 */
public interface FileService {
	List<Path> uploadFiles(List<MultipartFile> files);
	
	Path uploadFile(MultipartFile multipartFile, Path directoryPath);
	
	void deleteFiles(List<Path> paths);
	
	void deleteFile(Path path);
}

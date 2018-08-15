package message.properties.extractors.application.service;

import message.properties.extractors.domain.Extractors;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project message-extractor
 * @since 2018-08-14
 */
public interface ExtractorsService {
	Extractors addAllTargetPathsInDirectory( String relativePath);
	
	Extractors addAllTargetPaths(String... relativePaths);
	
	Extractors addTargetPath(String relativePath);
}

package aszka.metadata;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Descriptor;
import com.drew.metadata.exif.ExifIFD0Directory;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ReadExif {

	//private static Logger LOG = LogManager.getLogger(Metadata.class);
	public static final String JPG = ".jpg";
	public static final String SAMSUNG = "Samsung";
	public static final String SONY = "Sony";
	public static String SRC = "C:\\temp\\gallery\\";

	public static void main(String[] args) {

		Path srcPath = Paths.get(SRC);

		try {
			DirectoryStream<Path> directoryStream = Files.newDirectoryStream(srcPath,
					path -> path.toString().endsWith(JPG));
			directoryStream.forEach(path -> {
				File file = path.toFile();

				try {
					Metadata metadata = ImageMetadataReader.readMetadata(path.toFile());
					String value = getTagValue(metadata);
					if(SAMSUNG.equalsIgnoreCase(value)){
						moveFile(file, SAMSUNG);
					} else if(SONY.equalsIgnoreCase(value)) {
						moveFile(file, SONY);
					}
				} catch (ImageProcessingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void moveFile(File file, String cameraProducent) {
		try {
			Files.move(Paths.get(file.getAbsolutePath()), Paths.get(SRC+cameraProducent+"\\"+file.getName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getTagValue(Metadata metadata) {
		ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
		ExifIFD0Descriptor descriptor = new ExifIFD0Descriptor(directory);
		return descriptor.getDescription(ExifIFD0Directory.TAG_MAKE);
	}

}

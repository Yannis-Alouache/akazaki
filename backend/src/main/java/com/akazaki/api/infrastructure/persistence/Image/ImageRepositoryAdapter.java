package com.akazaki.api.infrastructure.persistence.Image;

import com.akazaki.api.domain.model.Image;
import com.akazaki.api.domain.ports.out.ImageRepository;
import com.akazaki.api.infrastructure.exceptions.UnableToSaveFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
@Profile("prod")
public class ImageRepositoryAdapter implements ImageRepository {

    @Value("${upload.dir}")
    private String uploadDir;

    private static final String ROOT = "/uploads/";

    @Override
    public String save(Image image) {
        String extension = StringUtils.getFilenameExtension(image.fileName());

        String uniqueFilename = UUID.randomUUID() + "." + extension;

        Path destinationFile = Paths.get(uploadDir).resolve(uniqueFilename).normalize();

        try {
            Files.copy(image.imageStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UnableToSaveFileException();
        }

        return ROOT + uniqueFilename;
    }
}

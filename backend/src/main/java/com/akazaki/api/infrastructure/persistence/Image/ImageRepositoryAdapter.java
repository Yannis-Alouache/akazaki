package com.akazaki.api.infrastructure.persistence.Image;

import com.akazaki.api.domain.ports.out.ImageRepository;
import com.akazaki.api.infrastructure.exceptions.UnableToSaveFileException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class ImageRepositoryAdapter implements ImageRepository {
    @Value("${upload.dir}")
    private String uploadDir;

    private static final String ROOT = "/uploads/";

    @Override
    public String save(String fileName, String contentType, InputStream imageStream) {
        String extension = StringUtils.getFilenameExtension(fileName);

        String uniqueFilename = UUID.randomUUID() + "." + extension;

        Path destinationFile = Paths.get(uploadDir).resolve(uniqueFilename).normalize();

        try {
            Files.copy(imageStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UnableToSaveFileException();
        }

        return ROOT + uniqueFilename;
    }
}

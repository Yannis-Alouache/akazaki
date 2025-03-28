package com.akazaki.api.infrastructure.persistence.Image;

import com.akazaki.api.domain.model.Image;
import com.akazaki.api.domain.ports.out.ImageRepository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Repository
@Profile("test")
public class InMemoryImageRepository implements ImageRepository {
    private static final String ROOT = "/uploads/";

    @Override
    public String save(Image image) {
        String extension = StringUtils.getFilenameExtension(image.fileName());

        String uniqueFilename = UUID.randomUUID() + "." + extension;

        return ROOT + uniqueFilename;
    }
}
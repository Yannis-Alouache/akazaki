package com.akazaki.api.domain.ports.out;

import java.io.InputStream;

public interface ImageRepository {
    String save(String fileName, String contentType, InputStream imageStream);
}

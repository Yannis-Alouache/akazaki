package com.akazaki.api.domain.ports.out;

import com.akazaki.api.domain.model.Image;

public interface ImageRepository {
    String save(Image image);
}

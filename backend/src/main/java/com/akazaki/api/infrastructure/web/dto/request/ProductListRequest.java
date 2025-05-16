package com.akazaki.api.infrastructure.web.dto.request;

import lombok.Data;

@Data
public class ProductListRequest {
    private int page = 0;
    private int size = 10;
}
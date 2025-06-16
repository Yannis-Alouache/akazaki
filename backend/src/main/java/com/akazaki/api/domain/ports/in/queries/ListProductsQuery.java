package com.akazaki.api.domain.ports.in.queries;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListProductsQuery {
    private int page;
    private int size;
}
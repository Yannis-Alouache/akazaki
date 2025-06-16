package com.akazaki.api.domain.ports.in.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ListProductsQuery {
    private int page;
    private int size;
    private List<String> categories;
}
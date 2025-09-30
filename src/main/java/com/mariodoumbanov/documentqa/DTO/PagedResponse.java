package com.mariodoumbanov.documentqa.DTO;

import java.util.List;

public record PagedResponse<M> (
    List<M> items,
    long size,
    long totalElements,
    int page
){}
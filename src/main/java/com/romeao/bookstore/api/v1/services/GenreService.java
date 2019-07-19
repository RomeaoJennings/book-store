package com.romeao.bookstore.api.v1.services;

import com.romeao.bookstore.api.v1.models.GenreSummaryDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GenreService {
    List<GenreSummaryDto> summarizeAll();

    Page<GenreSummaryDto> summarizeAll(int pageNum, int pageLimit);
}

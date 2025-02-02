package com.example.demo.recipe.APiAndDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PagedResponseDTO<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private Long totalElements;
    private int totalPages;
    // metoden tar inn et page Objekt, en side med data
    public static <T> PagedResponseDTO<T> fromPage(Page<T> page) {
        return new PagedResponseDTO<>( // konstruktør, setter verdier
                page.getContent(), // henter den faktiske listen
                page.getNumber(), // henter sidenummer
                page.getSize(), // henter sidestørrelse, antall kolonner nedover
                page.getTotalElements(), // henter det totale elementer i hele datamengden
                page.getTotalPages() // henter det totale antall sider
        );
    }
}

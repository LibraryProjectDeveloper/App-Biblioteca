package com.WebBiblioteca.DTO.Book;

import com.WebBiblioteca.Model.Category;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CountBookByCategory {
    private Category category;
    private Long count;

    public CountBookByCategory(Category category, Long count) {
        this.category = category;
        this.count = count;
    }

}

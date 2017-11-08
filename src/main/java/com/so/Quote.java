package com.so;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Quote {

    private String text;
    private String author;
    private int rating;
}

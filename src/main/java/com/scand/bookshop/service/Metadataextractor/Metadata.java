package com.scand.bookshop.service.Metadataextractor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Metadata {
    private String title;
    private String author;
    private String subject;
    private String producer;
    private String creator;
}

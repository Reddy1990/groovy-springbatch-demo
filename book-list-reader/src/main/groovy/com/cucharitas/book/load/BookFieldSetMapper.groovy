package com.cucharitas.book.load

import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet
import org.springframework.stereotype.Component

import com.cucharitas.book.pojo.Book

public class BookFieldSetMapper implements FieldSetMapper<Book> {

    public Book mapFieldSet(FieldSet fieldSet){
        Book book = new Book(
            letra: fieldSet.readString(0),
            autor:fieldSet.readString(1),
            titulo:fieldSet.readString(2),
            genero:fieldSet.readString(3),
            papyreid:fieldSet.readString(4),
            description:fieldSet.readString(5)
            )
    }
}

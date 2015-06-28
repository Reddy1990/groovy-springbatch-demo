package com.cucharitas.book.pojo


/**
 * LETRA|AUTOR|TÍTULO|GÉNERO|PAPYREID|DESCRIPCIÓN
 */
public class Book {

    String letra
    String autor
    String titulo
    String genero
    String papyreid
    String description

    @Override
    String toString(){
        return "$titulo, $autor"
    }

}



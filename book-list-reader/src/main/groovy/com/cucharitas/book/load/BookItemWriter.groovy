package com.cucharitas.book.load

import groovy.util.logging.Slf4j

import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import com.cucharitas.book.pojo.Book

@Component('writer')
public class BookItemWriter implements ItemWriter<Book>{

    /**
     * Writes to the selected output
     */
    void write(List<? extends Book> data){
        log.info "Writing chunk: $data"
    }

}

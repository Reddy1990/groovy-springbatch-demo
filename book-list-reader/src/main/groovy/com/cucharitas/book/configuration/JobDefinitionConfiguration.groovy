package com.cucharitas.book.configuration

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.batch.item.file.mapping.DefaultLineMapper
import org.springframework.batch.item.file.LineMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource

import com.cucharitas.book.pojo.Book
import com.cucharitas.book.load.BookFieldSetMapper

@Configuration
@Import(AppConfiguration.class)
@ComponentScan("com.cucharitas.book")
public class JobDefinitionConfiguration{

    @Autowired
    private JobBuilderFactory jobs

    @Autowired
    private StepBuilderFactory steps

    @Bean
    ItemReader<Book> reader() {
        FlatFileItemReader<Book> reader = new FlatFileItemReader<>()
        LineMapper lineMapper = new DefaultLineMapper()
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer("|"))
        lineMapper.setFieldSetMapper(new BookFieldSetMapper())

        reader.setResource(new ClassPathResource("Listado-CSV-consinopsis.csv"))
        reader.setLineMapper(lineMapper)
        reader.open(new ExecutionContext())

        reader
    }

    @Bean
    Job job(Step bookReadWriteStep){
        jobs.get("bookJob")
        .start(bookReadWriteStep)
        .build()
    }

    @Bean
    Step bookReadWriteStep(ItemReader<Book> reader, ItemWriter<Book> writer){
        steps.get("bookReadWriteStep")
        .<Object, Object>chunk(1)
        .reader(reader)
        .writer(writer)
        .build()
    }

}

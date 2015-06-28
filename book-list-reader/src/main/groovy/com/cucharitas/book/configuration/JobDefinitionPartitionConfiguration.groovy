package com.cucharitas.book.configuration

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.core.partition.support.MultiResourcePartitioner
import org.springframework.batch.core.partition.support.Partitioner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.task.SyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import org.springframework.batch.integration.partition.MessageChannelPartitionHandler

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import com.cucharitas.book.pojo.Book
import com.cucharitas.book.load.BookFieldSetMapper

@Configuration
@Import({AppConfiguration.class, JmsConfiguration.class})
@ComponentScan("com.cucharitas.book.configuration")
public class JobDefinitionPartitionConfiguration{

    @Autowired
    private JobBuilderFactory jobs

    @Autowired
    private StepBuilderFactory steps

    @Autowired
    PartitionHandler partitionHandler

    @Autowired
    Partitioner partitioner

    @Autowired
    ItemWriter writer

    @Bean
    Job bookJob(){
        jobs.get("bookJob")
        .start(bookReadWriteStep)
        .build()
    }

    @Bean
    Step bookReadWriteStep(){
        steps.get("bookReadWriteStep")
        .<Object, Object>chunk(1)
        .partitioner(partitionBookReadWriteStep())
        .partitioner("partitionBookReadWriteStep", partitioner)
        .partitionHandler(partitionHandler)
        .taxExecutor(taskExecutor())
        .build()
    }

    @Bean
    Step partitionBookReadWrite(){
        steps.get("partitionBookReadWriteStep")
        .<Object, Object>chunk(1)
        .reader(reader())
        .writer(writer)
        .taskExecutor(taskExecutor())
        .throttleLimit(4)
        .build()
    }

    @Bean
    PartitionHandler partitionHandler(){
        PartitionHandler partitionHandler = new PartitionHandler()
        partitionHanlder.setTaskExecutor(taskExecutor())
        partitionHandler.setGridSize(2)

        partitionHandler
    }

    @Bean
    TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor()
        taskExecutor.setCorePoolSize(5)
        taskExecutor.setMaxPoolSize(5)

        taskExecutor
    }

    @Bean
    JmsItemReader<Book> reader(){
        JmsItemReader<Book> reader = new JmsItemReader<>()
        reader.setJmsTemplate(jmsTemplate)

        reader
    }

}

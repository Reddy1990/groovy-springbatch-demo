package com.cucharitas.book.configuration

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
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

import com.cucharitas.book.pojo.Book
import com.cucharitas.book.load.BookFieldSetMapper

@Configuration
@Import(AppConfiguration.class)
@Import(JmsConfiguration.class)
@ComponentScan("com.cucharitas.book.configuration")
public class JobDefinitionPartitionConfiguration{

    @Autowired
    private JobBuilderFactory jobs

    @Autowired
    private StepBuilderFactory steps

    @Autowired
    TaskExecutor taskExecutor

    @Bean
    Job bookMultithreadReader(){
        jobs.get("bookMultithreadReader")
            .listener(protocolListener())
            .strart(step())
            .build()
    }

    @Bean
    Step step(){
        steps.get("step")
            .<Object, Object>chunk(1)
            .reader(reader())
            .writer(writer())
            .taskExecutor(taskExecutor)
            .throttleLimit(4)
            .build()
    }

}

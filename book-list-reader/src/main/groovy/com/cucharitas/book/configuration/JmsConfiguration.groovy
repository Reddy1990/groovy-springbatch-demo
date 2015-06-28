package com.cucharitas.book.configuration

import javax.jms.ConnectionFactory

import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.config.SimpleJmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.messaging.MessageHandler;
import org.springframework.integration.jms.DynamicJmsTemplate
import org.springframework.integration.jms.JmsSendingMessageHandler

import org.springframework.core.task.SimpleAsyncTaskExecutor


@Configuration
@EnableBatchProcessing
public class JmsConfiguration{
    private static final String BROKER_URL =
        "vm://embedded?broker.persistent=false,useShutdownHook=false"

    @Bean
    JmsListenerContainerFactory jmsContainerFactory ( connectionFactory)
    {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory())

        factory
    }

    @Bean
    ConnectionFactory connectionFactory(){
        new ActiveMQConnectionFactory(BROKER_URL)
    }

    @Bean
    Queue queue(){
        new ActiveMQQeue("partition.queue")
    }

    @Bean
    BrokerService broker() throws Exception {
        BrokerService broker = new BrokerService()

        broker.addConnector(BROKER_URL)
    }

    @Bean
    JmsTemplate jmsTemplate(){
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory())
        jmsTemplate.setDefaultDestination(queue())
        jmsTemplate.setReceiveTimeout(500)

        jmsTemplate
    }

    @Bean
    TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor()
        taskExecutor.setMaxPoolSize(4)
        taskExecutor.afterPropertiesSet()

        taskExecutor
    }


    //@Bean
    //IntegrationFlow jmsOutboundFlow() {
    //return IntegrationFlows.from("bookReaderChannel")
    //.handleWithAdapter(h ->
    //h.jms(this.jmsConnectionFactory).destination("slave-queue"))
    //.get();
    //}

    //@Bean
    //@ServiceActivator(inputChannel = "requests.partitioning")
    //MessageHandler jsmOutboundAdapter() {
        //JmsTemplate template = new DynamicJmsTemplate()
        //template.setConnectionFactory(connectionFactory())
        //JmsSendingMessageHandler handler = new JmsSendingMessageHandler(template)
        //handler.setDestinationName("requests-partitioning")
        //return handler
    //}

    //@Bean
    //@JmsListener(concurrency=5)
    //@ServiceActivator(inputChannel = "in.partitioning")
    //DefaultMessageListenerContainer messageDrivenMessageAdapter(){
        //DefaultMessageListenerContainer dmlc  = new DefaultMessageListenerContainer()

        //JmsTemplate template = new DynamicJmsTemplate()
        //template.setConnectionFactory(ConnectionFactory())
        //JmsSendingMessageHandler handler =  new JmsSendingMessageHandler(template)
        //handler.setDestinationName("in-partitioning")
        //handler.set

    //@Bean
    //TaskExecutorPartitionHandler partitionHander(){
        //TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler()
        //partitionHandler.setGridSize(5)
        //partitionHandler.setTaskExecutor(new SimpleAsyncTaskExecutor())
        ////XXX:Set the right step
        //partitionHandler.setSteps(partitionStep)

        //partitionHandler
    //}

//    @Bean
//    MultiResourcePartitioner partitioner(){
//        MultiResourcePartitioner partitioner = new MultiResourcePartitioner()
//        partitioner.setResources(
//
//    }
}

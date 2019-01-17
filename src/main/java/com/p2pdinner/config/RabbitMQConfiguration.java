package com.p2pdinner.config;

import com.p2pdinner.cqrs.EventType;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    static final String topicExchangeName = "menu-exchange";

    static final String createQueueName = "create-queue";
    static final String updateQueueName = "update-queue";
    static final String deleteQueueName = "delete-queue";

    @Bean
    @Qualifier("createQueue")
    Queue createQueue() {
        return new Queue(createQueueName, true);
    }

    @Bean
    Binding createBinding(@Qualifier("createQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(EventType.CMD_CREATE.name());
    }

    @Bean
    Queue updateQueue() {
        return new Queue(updateQueueName, true);
    }

    @Bean
    Binding updateBinding(@Qualifier("updateQueue")Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(EventType.CMD_UPDATE.name());
    }

    @Bean
    Queue deleteQueue() {
        return new Queue(deleteQueueName, true);
    }

    @Bean
    Binding deleteBinding(@Qualifier("deleteQueue")Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(EventType.CMD_DELETE.name());
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

}

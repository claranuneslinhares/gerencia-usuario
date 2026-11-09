package br.edu.ifrn.gerenciausuarios.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdmRabbitConfig {

    public static final String FILA_ADM = "fila_adm";
    public static final String FILA_MODULO = "fila_vendas";

    @Bean
    public Queue filaAdmSaida() {
        return new Queue(FILA_ADM, true);
    }

    @Bean
    public Queue filaAdmEntrada() {
        return new Queue(FILA_MODULO, true);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
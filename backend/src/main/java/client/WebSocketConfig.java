package client;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // Set prefix for the endpoint that the client listens for our messages from
        registry.enableSimpleBroker("/topic");

        // Set prefix for endpoints the client will send messages to
        registry.setApplicationDestinationPrefixes("/actions2");

    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //registry.addEndpoint("/stomp1").setAllowedOrigins("http://localhost:3001");
        registry.addEndpoint("/stomp2").setAllowedOrigins("http://localhost:3002");
    }
}
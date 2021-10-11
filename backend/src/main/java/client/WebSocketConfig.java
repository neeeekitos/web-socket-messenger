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
        registry.setApplicationDestinationPrefixes("/actions");

    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//
//        // Registers the endpoint where the connection will take place
//        registry.addEndpoint("/")
//                // Allow the origin http://localhost:63343 to send messages to us. (Base URL of the client)
//                .setAllowedOrigins("http://localhost:3001")
//                // Enable SockJS fallback options
//                .withSockJS();
//
//    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp").setAllowedOrigins("http://localhost:3001");
    }
}
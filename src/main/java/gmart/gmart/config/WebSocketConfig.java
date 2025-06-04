//package gmart.gmart.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
///**
// * 실시간 채팅을 위한 웹 소켓 설정 클래스
// */
//@Configuration
//
////내부적으로 메시지 브로커(Message Broker)를 만들고,
////메시지 송수신 흐름을 Spring이 알아서 처리할 수 있게 해줌.
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//
//    /**
//     * 클라이언트가 최초에 WebSocket 연결을 맺을 URL 을 정하는 메서드
//     * @param registry
//     */
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws")
//                .setAllowedOriginPatterns("*")
//                .withSockJS();
//    }
//
//
//    /**
//     * 메시지 라우팅 경로 설정
//     * @param registry
//     */
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setApplicationDestinationPrefixes("/app");
//        registry.setUserDestinationPrefix("/user");
//        registry.enableSimpleBroker("/user","/topic");
//    }
//}

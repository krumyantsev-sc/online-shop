package com.scand.bookshop.socket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class SocketIOConfig {

  @Value("${scand.bookshop.socket-server.hostname}")
  private String hostname;

  @Value("${scand.bookshop.socket-server.port}")
  private int port;

  @Bean
  public SocketIOServer socketIOServer() {
    Configuration config = new Configuration();
    config.setHostname(hostname);
    config.setPort(port);

    return new SocketIOServer(config);
  }
}
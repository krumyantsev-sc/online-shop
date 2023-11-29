package com.scand.bookshop.socket;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketIOServerManager {

  private final SocketIOServer server;

  @Autowired
  public SocketIOServerManager(SocketIOServer server) {
    this.server = server;
    server.start();
  }

  @PreDestroy
  public void stopSocketIOServer() {
    server.stop();
  }
}

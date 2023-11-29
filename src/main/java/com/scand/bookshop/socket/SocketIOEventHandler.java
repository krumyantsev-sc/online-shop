package com.scand.bookshop.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.entity.User.Role;
import com.scand.bookshop.security.jwt.JwtUtils;
import com.scand.bookshop.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SocketIOEventHandler {

  private static final String ADMINS_ROOM_NAME = "admins";

  private final SocketIOServer server;
  private final JwtUtils jwtUtils;
  private final UserService userService;

  @PostConstruct
  private void initializeEventListeners() {
    server.addConnectListener(onConnected());
    server.addDisconnectListener(onDisconnected());
  }

  private ConnectListener onConnected() {
    return client -> {
      String token = client.getHandshakeData().getSingleUrlParam("token");
      User user = userService.findUserByUsername(jwtUtils.getUserNameFromJwtToken(token, false))
          .get();
      String uuid = user.getUuid().toString();
      client.joinRoom(uuid);
      if (user.getRole().equals(Role.ADMIN)) {
        client.joinRoom(ADMINS_ROOM_NAME);
      }
      log.debug("User connected with uuid {}", uuid);
    };
  }

  private DisconnectListener onDisconnected() {
    return client -> {
      System.out.println("Client disconnected: " + client.getAllRooms());
    };
  }
}

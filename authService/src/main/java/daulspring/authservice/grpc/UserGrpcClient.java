package daulspring.authservice.grpc;

import daulspring.grpc.user.CreateProfileRequest;
import daulspring.grpc.user.CreateProfileResponse;
import daulspring.grpc.user.ExistsByEmailRequest;
import daulspring.grpc.user.ExistsByNicknameRequest;
import daulspring.grpc.user.UserGrpcServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserGrpcClient {

  @Value("${grpc.client.user-service.host}")
  private String host;

  @Value("${grpc.client.user-service.port}")
  private int port;

  private UserGrpcServiceGrpc.UserGrpcServiceBlockingStub stub;

  @PostConstruct
  public void init() {
    ManagedChannel channel = ManagedChannelBuilder
        .forAddress(host, port)
        .usePlaintext()
        .build();
    stub = UserGrpcServiceGrpc.newBlockingStub(channel);
  }

  public Long createProfile(String email, String userName,
      String nickname, String profileImg) {

    CreateProfileRequest request = CreateProfileRequest.newBuilder()
        .setEmail(email)
        .setUserName(userName)
        .setNickname(nickname)
        .setProfileImg(profileImg != null ? profileImg : "")
        .build();

    CreateProfileResponse response = stub.createProfile(request);
    return response.getUserId();
  }

  public boolean existsByEmail(String email) {
    return stub.existsByEmail(
        ExistsByEmailRequest.newBuilder()
            .setEmail(email)
            .build()
    ).getExists();
  }

  public boolean existsByNickname(String nickname) {
    return stub.existsByNickname(
        ExistsByNicknameRequest.newBuilder()
            .setNickname(nickname)
            .build()
    ).getExists();
  }
}
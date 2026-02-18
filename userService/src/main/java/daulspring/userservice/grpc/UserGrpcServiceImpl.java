package daulspring.userservice.grpc;

import daulspring.grpc.user.CreateProfileRequest;
import daulspring.grpc.user.CreateProfileResponse;
import daulspring.grpc.user.ExistsByEmailRequest;
import daulspring.grpc.user.ExistsByEmailResponse;
import daulspring.grpc.user.ExistsByNicknameRequest;
import daulspring.grpc.user.ExistsByNicknameResponse;
import daulspring.grpc.user.UserGrpcServiceGrpc;
import daulspring.userservice.dto.UserCreateRequestDTO;
import daulspring.userservice.service.UserService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class UserGrpcServiceImpl extends UserGrpcServiceGrpc.UserGrpcServiceImplBase {

  private final UserService userService;

  @Override
  public void createProfile(CreateProfileRequest request,
      StreamObserver<CreateProfileResponse> responseObserver) {
    try {
      UserCreateRequestDTO dto = new UserCreateRequestDTO(
          request.getEmail(),
          request.getUserName(),
          request.getNickname(),
          request.getProfileImg()
      );

      Long userId = userService.createProfile(dto);

      responseObserver.onNext(
          CreateProfileResponse.newBuilder()
              .setUserId(userId)
              .build()
      );
      responseObserver.onCompleted();

    } catch (IllegalArgumentException e) {
      log.warn("createProfile 실패: {}", e.getMessage());
      responseObserver.onError(
          Status.INVALID_ARGUMENT
              .withDescription(e.getMessage())
              .asRuntimeException()
      );
    } catch (Exception e) {
      log.error("createProfile 서버 오류", e);
      responseObserver.onError(
          Status.INTERNAL
              .withDescription("서버 오류가 발생했습니다.")
              .asRuntimeException()
      );
    }
  }

  @Override
  public void existsByEmail(ExistsByEmailRequest request,
      StreamObserver<ExistsByEmailResponse> responseObserver) {
    try {
      boolean exists = userService.existsByEmail(request.getEmail());

      responseObserver.onNext(
          ExistsByEmailResponse.newBuilder()
              .setExists(exists)
              .build()
      );
      responseObserver.onCompleted();

    } catch (Exception e) {
      log.error("existsByEmail 서버 오류", e);
      responseObserver.onError(
          Status.INTERNAL
              .withDescription("서버 오류가 발생했습니다.")
              .asRuntimeException()
      );
    }
  }

  @Override
  public void existsByNickname(ExistsByNicknameRequest request,
      StreamObserver<ExistsByNicknameResponse> responseObserver) {
    try {
      boolean exists = userService.existsByNickname(request.getNickname());

      responseObserver.onNext(
          ExistsByNicknameResponse.newBuilder()
              .setExists(exists)
              .build()
      );
      responseObserver.onCompleted();

    } catch (Exception e) {
      log.error("existsByNickname 서버 오류", e);
      responseObserver.onError(
          Status.INTERNAL
              .withDescription("서버 오류가 발생했습니다.")
              .asRuntimeException()
      );
    }
  }
}
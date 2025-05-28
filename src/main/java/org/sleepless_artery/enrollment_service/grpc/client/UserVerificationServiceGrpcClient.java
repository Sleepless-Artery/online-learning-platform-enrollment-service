package org.sleepless_artery.enrollment_service.grpc.client;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sleepless_artery.enrollment_service.VerifyUserExistenceRequest;
import org.sleepless_artery.enrollment_service.config.grpc.UserServiceGrpcClientConfig;
import org.sleepless_artery.enrollment_service.exception.GrpcProcessingException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserVerificationServiceGrpcClient {

    private final UserServiceGrpcClientConfig grpcClientConfig;


    public boolean verifyUserExistence(Long userId) {
        log.info("Sending gRPC request to verify user's existence");

        VerifyUserExistenceRequest request = VerifyUserExistenceRequest.newBuilder()
                .setId(userId)
                .build();

        try {
            return grpcClientConfig.userVerificationServiceBlockingStub()
                    .withDeadlineAfter(30, TimeUnit.SECONDS)
                    .verifyUserExistence(request)
                    .getExistence();
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                return false;
            }

            log.error("gRPC error: {}", e.getStatus());
            throw new GrpcProcessingException("User verification service unavailable");
        }
    }
}

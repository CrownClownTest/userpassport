package com.graduation.userpassport.business.user;

import com.graduation.userpassport.bo.user.UserInfoBO;
import com.graduation.userpassport.bo.user.UserQueryBO;
import com.graduation.userpassport.bo.user.UserRegisterBO;
import com.graduation.userpassport.bo.user.UserUpdateMeBO;
import com.graduation.userpassport.bo.user.UserUpdateMeContextBO;
import com.graduation.userpassport.bo.user.operation.UserInfoSnapshotBO;
import com.graduation.userpassport.bo.user.operation.UserOperationDetailBO;
import com.graduation.userpassport.bo.user.operation.UserOperationLogCreateBO;
import com.graduation.userpassport.constant.user.UserInfoFieldEnum;
import com.graduation.userpassport.constant.user.UserOperationSourceEnum;
import com.graduation.userpassport.constant.user.UserOperationTypeEnum;
import com.graduation.userpassport.resource.user.entity.UserInfoEntity;
import com.graduation.userpassport.resource.user.repository.UserInfoRepository;
import com.graduation.userpassport.utils.security.PasswordHashUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserBusiness {
    private final UserInfoRepository userInfoRepository;
    private final UserOperationLogBusiness userOperationLogBusiness;

    public UserBusiness(UserInfoRepository userInfoRepository, UserOperationLogBusiness userOperationLogBusiness) {
        this.userInfoRepository = userInfoRepository;
        this.userOperationLogBusiness = userOperationLogBusiness;
    }

    public UserInfoBO register(UserRegisterBO registerBO) {
        if (userInfoRepository.existsByPhoneOrEmail(registerBO.getPhone(), registerBO.getEmail())) {
            throw new IllegalArgumentException("手机号或邮箱已注册");
        }
        UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                .email(registerBO.getEmail())
                .phone(registerBO.getPhone())
                .nickname(registerBO.getNickname())
                .password(PasswordHashUtils.hash(registerBO.getPassword()))
                .build();
        UserInfoEntity savedUser = userInfoRepository.save(userInfoEntity);
        return toBO(savedUser);
    }

    public UserInfoBO query(UserQueryBO queryBO) {
        Optional<UserInfoEntity> userOpt = switch (queryBO.getQueryType()) {
            case USER_ID -> queryByUserId(queryBO.getQueryValue());
            case PHONE -> userInfoRepository.findByPhone(queryBO.getQueryValue());
            case EMAIL -> userInfoRepository.findByEmail(queryBO.getQueryValue());
        };
        UserInfoEntity userInfoEntity = userOpt.orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return toBO(userInfoEntity);
    }

    public UserInfoBO queryByUserId(Long userId) {
        UserInfoEntity userInfoEntity = userInfoRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return toBO(userInfoEntity);
    }

    public UserInfoBO updateMe(UserUpdateMeBO updateBO) {
        // 1) 构建上下文：读取当前用户实体、规范化入参、计算哪些字段发生变化
        UserUpdateMeContextBO ctx = buildUpdateMeContext(updateBO);

        // 2) 前置校验：必须至少有一个字段变更；任何变更都必须校验原密码；邮箱/手机号需要唯一性校验
        validateHasAnyChange(ctx);
        validateOldPassword(ctx);
        validateUniqueFields(ctx);

        // 3) 执行更新：记录旧快照 -> 将变更写回实体 -> 持久化保存
        ctx.setOldSnapshot(snapshot(ctx.getUserInfoEntity()));
        applyUpdateToEntity(ctx);
        UserInfoEntity saved = userInfoRepository.save(ctx.getUserInfoEntity());

        // 4) 记录日志：构建新快照与变更字段列表，并写入 user_operation_log（不记录任何密码明文/哈希）
        ctx.setNewSnapshot(snapshot(saved));
        ctx.setChangedFields(buildChangedFields(ctx));
        recordUpdateMeLog(ctx);

        return toBO(saved);
    }

    private Optional<UserInfoEntity> queryByUserId(String queryValue) {
        try {
            return userInfoRepository.findById(Long.parseLong(queryValue));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("USER_ID 查询值必须是数字");
        }
    }

    private UserInfoBO toBO(UserInfoEntity userInfoEntity) {
        return UserInfoBO.builder()
                .userId(userInfoEntity.getUserId())
                .email(userInfoEntity.getEmail())
                .phone(userInfoEntity.getPhone())
                .nickname(userInfoEntity.getNickname())
                .createdAt(userInfoEntity.getCreatedAt())
                .build();
    }

    private UserInfoSnapshotBO snapshot(UserInfoEntity userInfoEntity) {
        return UserInfoSnapshotBO.builder()
                .email(userInfoEntity.getEmail())
                .phone(userInfoEntity.getPhone())
                .nickname(userInfoEntity.getNickname())
                .build();
    }

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return trimmed;
    }

    private UserUpdateMeContextBO buildUpdateMeContext(UserUpdateMeBO updateBO) {
        if (updateBO == null) {
            throw new IllegalArgumentException("请求不能为空");
        }
        if (updateBO.getUserId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        UserInfoEntity userInfoEntity = userInfoRepository.findById(updateBO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        String newEmail = normalizeOptional(updateBO.getEmail());
        String newPhone = normalizeOptional(updateBO.getPhone());
        String newNickname = normalizeOptional(updateBO.getNickname());
        String newPassword = normalizeOptional(updateBO.getNewPassword());
        String oldPassword = normalizeOptional(updateBO.getOldPassword());

        boolean emailChanged = newEmail != null && !newEmail.equals(userInfoEntity.getEmail());
        boolean phoneChanged = newPhone != null && !newPhone.equals(userInfoEntity.getPhone());
        boolean nicknameChanged = newNickname != null && !newNickname.equals(userInfoEntity.getNickname());
        boolean passwordChanged = newPassword != null;

        return UserUpdateMeContextBO.builder()
                .request(updateBO)
                .userInfoEntity(userInfoEntity)
                .newEmail(newEmail)
                .newPhone(newPhone)
                .newNickname(newNickname)
                .newPassword(newPassword)
                .oldPassword(oldPassword)
                .emailChanged(emailChanged)
                .phoneChanged(phoneChanged)
                .nicknameChanged(nicknameChanged)
                .passwordChanged(passwordChanged)
                .build();
    }

    private void validateHasAnyChange(UserUpdateMeContextBO ctx) {
        if (ctx.isEmailChanged() || ctx.isPhoneChanged() || ctx.isNicknameChanged() || ctx.isPasswordChanged()) {
            return;
        }
        throw new IllegalArgumentException("没有需要修改的字段");
    }

    private void validateOldPassword(UserUpdateMeContextBO ctx) {
        if (ctx.getOldPassword() == null) {
            throw new IllegalArgumentException("原密码不能为空");
        }
        if (!PasswordHashUtils.verify(ctx.getOldPassword(), ctx.getUserInfoEntity().getPassword())) {
            throw new IllegalArgumentException("原密码错误");
        }
    }

    private void validateUniqueFields(UserUpdateMeContextBO ctx) {
        Long userId = ctx.getRequest().getUserId();
        if (ctx.isEmailChanged() && userInfoRepository.existsByEmailAndUserIdNot(ctx.getNewEmail(), userId)) {
            throw new IllegalArgumentException("邮箱已被使用");
        }
        if (ctx.isPhoneChanged() && userInfoRepository.existsByPhoneAndUserIdNot(ctx.getNewPhone(), userId)) {
            throw new IllegalArgumentException("手机号已被使用");
        }
    }

    private void applyUpdateToEntity(UserUpdateMeContextBO ctx) {
        UserInfoEntity userInfoEntity = ctx.getUserInfoEntity();
        if (ctx.isEmailChanged()) {
            userInfoEntity.setEmail(ctx.getNewEmail());
        }
        if (ctx.isPhoneChanged()) {
            userInfoEntity.setPhone(ctx.getNewPhone());
        }
        if (ctx.isNicknameChanged()) {
            userInfoEntity.setNickname(ctx.getNewNickname());
        }
        if (ctx.isPasswordChanged()) {
            userInfoEntity.setPassword(PasswordHashUtils.hash(ctx.getNewPassword()));
        }
    }

    private List<UserInfoFieldEnum> buildChangedFields(UserUpdateMeContextBO ctx) {
        List<UserInfoFieldEnum> changedFields = new ArrayList<>();
        if (ctx.isEmailChanged()) {
            changedFields.add(UserInfoFieldEnum.EMAIL);
        }
        if (ctx.isPhoneChanged()) {
            changedFields.add(UserInfoFieldEnum.PHONE);
        }
        if (ctx.isNicknameChanged()) {
            changedFields.add(UserInfoFieldEnum.NICKNAME);
        }
        if (ctx.isPasswordChanged()) {
            changedFields.add(UserInfoFieldEnum.PASSWORD);
        }
        return changedFields;
    }

    private void recordUpdateMeLog(UserUpdateMeContextBO ctx) {
        UserUpdateMeBO req = ctx.getRequest();
        userOperationLogBusiness.record(UserOperationLogCreateBO.builder()
                .operatorId(req.getUserId())
                .targetUserId(req.getUserId())
                .operationType(UserOperationTypeEnum.UPDATE_ME)
                .operationDetail(UserOperationDetailBO.builder()
                        .source(UserOperationSourceEnum.SELF)
                        .scene(normalizeOptional(req.getScene()))
                        .changedFields(ctx.getChangedFields())
                        .passwordChanged(ctx.isPasswordChanged())
                        .build())
                .oldValue(ctx.getOldSnapshot())
                .newValue(ctx.getNewSnapshot())
                .ipAddress(req.getIpAddress())
                .userAgent(req.getUserAgent())
                .build());
    }
}

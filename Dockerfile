# 第一阶段：构建阶段
FROM gradle:8.10-jdk21 AS builder

# 接收构建参数（Gradle 缓存目录）
ARG GRADLE_USER_HOME=/gradle-cache
ENV GRADLE_USER_HOME=${GRADLE_USER_HOME}

# 设置工作目录
WORKDIR /app

# 复制构建文件
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY gradlew gradlew
COPY gradlew.bat gradlew.bat

# 预热 Gradle 依赖（仅当构建文件变化时重新执行）
RUN ./gradlew dependencies --no-daemon

# 复制源代码
COPY src src

# 构建应用
RUN ./gradlew clean build -x test

# 第二阶段：运行阶段
FROM eclipse-temurin:21-jre-alpine

# 设置工作目录
WORKDIR /app

# 从构建阶段复制jar文件（使用通配符，避免硬编码版本号）
COPY --from=builder /app/build/libs/*.jar app.jar

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
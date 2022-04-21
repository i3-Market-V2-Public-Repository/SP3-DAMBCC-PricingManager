# syntax=docker/dockerfile:experimental
FROM openjdk:11.0.11-jdk-slim as build
WORKDIR /workspace/app

COPY . /workspace/app

RUN --mount=type=cache,target=/root/.gradle ./gradlew clean build
RUN mkdir -p build/libs/dependency && (cd build/libs/dependency; find ../ -name "*.jar" -exec jar -xf {} \;)

FROM openjdk:11.0.11-jdk-slim
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build/libs/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","com.gft.i3market.I3MarketApplication"]

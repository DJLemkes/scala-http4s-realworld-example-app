FROM        oracle/graalvm-ce:19.0.0 as assembler
LABEL       maintainer="Herdy Handoko <herdy.handoko@gmail.com>"
LABEL       description="http4s GraalVM assembler"

WORKDIR     assembler
# Copy only the required files to setup sbt
COPY        project/*.properties project/*.sbt project/
COPY        project/project/*.sbt project/project/
RUN         (SBT_VERSION=$(cat project/build.properties | cut -d '=' -f 2 | tr -d '[:space:]') \
              && curl -L -O https://piccolo.link/sbt-${SBT_VERSION}.tgz \
              && tar -xzf sbt-${SBT_VERSION}.tgz \
              && ./sbt/bin/sbt -mem 4096 sbtVersion)

# Copy the rest of the application source files
COPY        project/*.scala project/
COPY        src/ src/
COPY        build.sbt VERSION.txt ./
RUN         ./sbt/bin/sbt -mem 4096 clean assembly

# ~~~~~~

FROM        oracle/graalvm-ce:19.0.0 as packager
LABEL       maintainer="Herdy Handoko <herdy.handoko@gmail.com>"
LABEL       description="http4s GraalVM native-image packager"

ARG         APP_NAME
ENV         APP_NAME ${APP_NAME:-realworld-assembly}
ARG         APP_VERSION
ENV         APP_VERSION ${APP_VERSION:-1.0.0-SNAPSHOT}

WORKDIR     packager
RUN         gu install native-image
COPY        --from=assembler /assembler/target/scala-2.12/${APP_NAME}-${APP_VERSION}.jar ./
RUN         native-image \
              --no-server \
              -cp ${APP_NAME}-${APP_VERSION}.jar

# ~~~~~~

FROM        frolvlad/alpine-glibc
LABEL       maintainer="Herdy Handoko <herdy.handoko@gmail.com>"
LABEL       description="http4s GraalVM native-image runtime container"

WORKDIR     app
COPY        --from=packager /packager/realworld ./
COPY        --from=packager /opt/graalvm-ce-19.0.0/jre/lib/amd64/libsunec.so ./

EXPOSE      8080
ENTRYPOINT  ["./realworld"]

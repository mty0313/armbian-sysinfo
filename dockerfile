FROM openjdk:8
RUN mkdir /armbian-sysinfo
WORKDIR /armbian-sysinfo
COPY ./target/armbian-sysinfo-0.0.1-SNAPSHOT.jar /armbian-sysinfo
EXPOSE 8080
CMD ["java", "-jar", "armbian-sysinfo-0.0.1-SNAPSHOT.jar"]
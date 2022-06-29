# Set base image from Docker image repo
FROM amazoncorretto:17

ADD OSRSBot.jar OSRSBot.jar

RUN yum install libXext.x86_64 libXrender.x86_64 libXtst.x86_64 -y

EXPOSE 8080

CMD java -jar OSRSBot.jar -bot-runelite -developer-mode
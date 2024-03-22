FROM openjdk:17-alpine

ARG WORKING_DIR=/opt/brands-service
ARG ARTIFACT_NAME=weshopify-brands-service.jar
ARG SERVICE_PORT=5018

ENV FINAL_ARTIFACT=${ARTIFACT_NAME}
#create a directory
RUN mkdir ${WORKING_DIR}

#make it as a working directory
WORKDIR ${WORKING_DIR}

COPY target/${ARTIFACT_NAME} ${WORKING_DIR}

EXPOSE ${SERVICE_PORT}
#the artifact name we are supplying to CMD should be an environment variable
#when an environment variable has been supplied we must have to take the bash as
#an cmd argument to take the artifact name as command line argument.

CMD [ "sh","-c", "java -jar ${FINAL_ARTIFACT}"]
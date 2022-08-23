FROM jupyter/pyspark-notebook
ARG APP_HOME .
ENV JUPYTER_ENABLE_LAB: "yes"
WORKDIR ${APP_HOME}
COPY requirements.txt ${APP_HOME}
RUN pip install -r requirements.txt
COPY . ${APP_HOME}
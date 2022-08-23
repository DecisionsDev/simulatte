FROM python:3.9
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1
ARG APP_HOME ./work
WORKDIR ${APP_HOME}
COPY requirements.txt ${APP_HOME}
RUN pip install -r requirements.txt
COPY . ${APP_HOME}
#CMD [ "python", "./manage.py", "runserver", "0.0.0.0:8000"] #Uncomment only if you want to build analytic-server image
FROM continuumio/miniconda:latest

WORKDIR /home/app

COPY environment.yml ./
RUN conda env create -f environment.yml
RUN echo "source activate microservices" > ~/.bashrc
ENV PATH /opt/conda/envs/microservices/bin:$PATH

COPY *.py ./

COPY run.sh ./
RUN chmod +x run.sh

EXPOSE 8080

ENTRYPOINT ["./run.sh"]

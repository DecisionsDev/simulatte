# SimuLatte
This projects aims to build confidence in your decision automation through simulations.

## Motivations
   * Trust your decision automation on large datasets coming from your production warehouse or synthetic.
   * How to execute a large dataset (millions to billions) of automated decisions based on business rules and machine learning.
   * How to apply analytics on these large decision sets to get business insights by computing KPIs and visualizing the results.
   * How can we navigate with ease in a decision set to search for specific decisions?
   * How to compose analytical dashboards for tour decision automation?
   * How to empower champion/challenger comparisions for automated decision services?
   
Simulatte combines:
   * a Spark based execution invoker to read a JSON request dataset and automate at scale the business decisions, and write them with their traces,
   * an analytics post-processing for computing metrics, KPIs and visualizations through Python and Jupyter notebooks.
   * a decision automation delegated to IBM Automation Decision Services(ADS) or IBM Operational Decision Services(ODM), 2 capabilities of the Cloud Pak for Business Automation. These 2 capabilities run an inference rule engine and ADS allows to call any ML model to bring probabilities in the decision making.

You can use Simulatte in an online mode through REST calls on microservices. Additionaly you can submit the decision invoker in a Spark batch to run in an Hadoop Spark cluster for maximum performances.

## Flavors
The simulation frameworks delivers a matrix of modes on the following dimensions:
   * online vs offline execution
   * remote REST vs embedded Java calls for the decision invocation,
   * basic Java single process single threaded, or Spark local, Spark cluster for full power,

## How to use the project

### Setup for docker use
Clone the repository:
By using http protocol
```bash
git clone https://github.com/DecisionsDev/simulatte.git
```
or ssh protocol
```bash
git clone git@github.com:DecisionsDev/simulatte.git
```

Move to the project directory.
```bash
cd simulatte
```
### Configure Decision Service invocation
SimuLatte comes by default with a standalone ODM Docker container.
To run simulations with IBM ADS or another ODM setup please follow the following configuration steps:
   * [Configure with ADS](docs/CONNECT_ADS.md "Configure with ADS")
   * [Configure with ODM](docs/CONNECT_ODM.md "Configure with ODM")
   

### Run
Run docker compose commands 
```bash
docker compose build 
```

Note: If you don't configure an ADS connection then you will face the following error:
```bash
ERROR [simulatte/simulation-service:latest stage-1 3/3] COPY --from=builder /builder/simulatte-online/t  0.0s
```
Reason is that the ADS Java libraries are missing to build simulatte. Neverthess SimuLatte is good to manage simulations through remote invocations with ODM.

```bash
docker compose up -d
```

At the end you should see all containers running as follows:
```bash
[+] Running 1/1
 ⠿ grafana Pulled                           1.4s                                                                                         
[+] Running 7/7
 ⠿ Container simulatte-db        Healthy    11.4s
 ⠿ Container prometheus          Started    0.6s
 ⠿ Container notebooks           Started    0.9s
 ⠿ Container simulation-service  Started    12.0s
 ⠿ Container analytic-service    Started    12.0s
 ⠿ Container grafana             Started    1.3s
 ⠿ Container odm                 Started     0.7s
 ```

#### Ports
Be aware that Simulatte uses by default the following ports:
   * port 3000 for grafana
   * port **8080** for simulatte online
   * port **8888** for Jupyter notebooks
   * port **4040** for Spark jobs monitoring
   * port **9060** only for ODM image use
   * port **9443** only for ODM image use
   * port **5432** for database
   * port **9090** for Prometheus
   
In case these port numbers collide with existing applications you can change then in the docker-compose.yml.

The OpenAPI UI is available at http://localhost:8080/swagger-ui/index.html

To use simulatte notebooks, run the command below:
```bash
docker logs notebooks
```
Click on the `http://127.0.0.1:8888` link printed in your console.
You see the Jupyter web page.

<img alt="Entering in the notebook server" src="./docs/images/simulatte.notebooks.server.page1.png" width="50%">

On the left side navigate into the `work/notebooks` hierarchy.
You see ADS and ODM folders. <br>
#### ODM
Select the decisioning capability ODM. <br>
Select notebook for loanvalidation. <br>
Run all cells. <br>
#### ADS
Select the decisioning capability ADS. <br>
Select notebook for loyalty. <br>
Run all cells. <br>
Congratulations you have created a decision simulation, run it, to get insights about how your logic applies to the sample dataset!

### Stop
To stop all simulatte containers : 
```bash
docker compose stop
```

### Troubleshooting
* Build - No space left on device during Build
   You can encounter a space limit when building the images with the message like:
   ```bash
   ERROR: Could not install packages due to an OSError: [Errno 28] No space left on device: '/tmp/pip-uninstall-t79kgz4i'
   ```
   Resolution: Clean up the temporary folders and unnecessary resources in your Docker installation by running:
   ```bash
   docker system prune    
   ```

## Structure
The simulatte project is composed of the following building blocks:
   * [core](docs/core.md): a core library. It contains all common material to run simulations in online and offline. It covers the default decision service invokers for ADS and ODM.
   * [online](docs/online.md): online. This part comes as Docker microservices to execute decisions through a Java based container, and perform analytic post processing to produce a simulation report.
   * [offline](docs/offline.md): offline. This part gives a generic batch simulation runner capable to run standalone but on Hadoop Spark cluster too for maximum of scalability and performance with data residing in the lake. 

![Structure](docs/images/simulatte.project.structure.png "structure of the project")

## Benchmarks
[benchmarks](docs/benchmarks.md): performances

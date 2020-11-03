# Backend for VCareUrCar

Microservices based backend for [VCareUrCar](https://github.com/adarshm-26/VCareUrCar)
built during NeuralHack Season 4 by Virtusa along with [Ajay Rayudu](https://github.com/Ajay8919)

Features -
* Concerns are split across simple microservices
* Authentication and routing is handled using [Zuul](https://github.com/Netflix/zuul) and also [Eureka](https://github.com/Netflix/eureka) for service registry
* User, Job & Car services provide business logic services
* Simple Report generation is done using [JasperReports by TIBCO](https://github.com/TIBCOSoftware/jasperreports)
* Docker ready configuration of all services along with [docker-compose](https://github.com/docker/compose)

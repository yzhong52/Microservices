# Kubernetes Microservices Demo

Three simple apps in this repo:

**books**

To keep things simple, we don't really have a database of books.
For whatever request that is made to the `/api/v1/book/:id` endpoint, we always return a same book.

**auth**

It will check the authorization token.
If it matches `SUPERSECUREAUTTHTOKEN`, then it returns `{ok: true}`;
otherwise, it returns `{ok: false}`.

**gateway**

`/api/v1/book/:bookId` validates the authentication the request by forwarding a request to **auth**.
If it is authenticated, it will then make a request to **books** asking for details for the book given the `bookId `.

## Implementations

The applications above are implemented in 3 different tech stacks. Each in separate folders:

* scala + finch
* typescript + express
* python + flask

## Running Services

We are using **books** (typescript) as an example here.
But the principle is the same for ther other two apps: **auth** and **gateway**,
as well as the scala and python implementations. Further details in the blog post. 

### Running services locally

Make sure you have typescript installed.

```
$ npm install -g typescript
$ tsc --version

Version 3.4.5
```

Build and run `books_svc`.

```
cd books_svc
npm install
npm run build
npm run serve

curl http://localhost:8080/api/v1/book/1
```

### Running services inside a minikube cluster

Build docker images for the docker daemon inside minikube.
And then create kubenete deployment and service.

```
eval $(minikube docker-env)
docker build ./books_svc -t books

kubectl apply -f books_svc/books-deployment.yaml
kubectl apply -f books_svc/books-service.yaml

curl $(minikube service books-service --url)/api/v1/book/1
```

### Running services in a hybrid mode

When we have an entire system with multiple services running inside a minikube cluster and we want to develop or investigate one of them,
it would be easier to launch that service locally while leaving the rest of the services running inside the cluster.
It is a faster change/build/validate development cycle and we can also utilize any IDE or debugger if needed. 

That's what `books-service-local.yaml` is for: routing requests to **books** from the cluster to the your local machine.
In more details, it does two things:

* Create a **Headless Service** without selectors. See kubernetes doc about [services-networking](https://kubernetes.io/docs/concepts/services-networking/service/#services-without-selectors). 
* Create an **Endpoint** object that point to localhost `10.0.2.2`, which is the host machine instead of the VM. 

The IP to reach the host machine `10.0.2.2` maybe driver dependend. Use `ifconfig` to find out the host IP, which should be under `vboxnet0`.

Some other known alternatives:

* `10.0.2.2` according to [SO](https://stackoverflow.com/q/1261975/1035008)
* `192.168.64.1` according to [github.com/machine-drivers/docker-machine-driver-xhyve](https://github.com/machine-drivers/docker-machine-driver-xhyve/issues/196#issuecomment-328363611)
* `192.168.99.1` according to [github.com/kubernetes/minikube](https://github.com/kubernetes/minikube/issues/2735)



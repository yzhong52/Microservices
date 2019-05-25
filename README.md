# Kubernetes Microservices Demo

Three simple services:

**books_svc**

To keep things simple, we don't really have a database of books.
For whatever request that is made to the `/api/v1/book/:id` endpoint, we always return a same book.

**auth_svc**

It will check the authorization token.
If it matches `SUPERSECUREAUTTHTOKEN`, then returns `{ok: true}`;
otherwise, return `{ok: false}`.

**gateway_svc**

It has two endpoints `api/v1/hey` and `/api/v1/book/:id`.
`api/v1/hey` will simply response "Hello World".
`/api/v1/book/:id` will first authenticate the request by sending a request to **auth-svc**.
If it is authenticated, it will then make a request to **books-svc** asking for details for the book given id.

## Running Services

We are using `books_svc` as an example here. But the principle is the same for `auth_svc` and `gateway_svc`.

### Running service locally

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

### Running service inside minikube cluster

Build docker image (for the docker daemon inside minikube). 
Create kubenete deployment and service.

```
eval $(minikube docker-env)
docker build ./books_svc -t books

kubectl apply -f books_svc/books-deployment.yaml
kubectl apply -f books_svc/books-service.yaml

curl $(minikube service books-service --url)/api/v1/book/1
```

### Hybrid

When we have multiple services running inside a minikube cluster and we want to develop or investigate one of them,
it would be easier to launch that service locally while leaving the rest of the services running inside the cluster.
It is a faster change/build/validate development cycle and we can also utilize any IDE or debugger if needed. 
That's what `books-service-local.yaml` is for.
It does two things:

* Create a headless service without selectors. See kubernetes doc about [services-networking](https://kubernetes.io/docs/concepts/services-networking/service/#services-without-selectors). 
* Create an Endpoint object that point to localhost `10.0.2.2`, which is the host machine instead of the VM. 

The IP to reach the host machine maybe driver dependend. We can use `ifconfig` to find out the host IP, which should be under `vboxnet0`.

These are some known alternatives:

* `10.0.2.2` according to [link](https://stackoverflow.com/q/1261975/1035008)
* `192.168.64.1` according to [link](https://github.com/machine-drivers/docker-machine-driver-xhyve/issues/196#issuecomment-328363611)
* `192.168.99.1` according to [link](https://github.com/kubernetes/minikube/issues/2735)



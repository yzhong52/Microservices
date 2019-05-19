# Kubernetes Microservices Demo

Three simple services:

**books_svc**

To keep things simple, we don't really have a database of books.
For whatever request that is made to the `/api/v1/book/:id` endpoint, we always return a same book.

**auth_svc**

It will check the authorization token.
If it matches `SUPERSECUREAUTTHTOKEN`, then returns `{ok: true}`;
otherwise, return `{ok: false}`.

**gatewa_svc**

It has two endpoints `api/v1/hey` and `/api/v1/book/:id`.
`api/v1/hey` will simply response "Hello World".
`/api/v1/book/:id` will first authenticate the request by sending a request to **auth-svc**.
If it is authenticated, it will then make a request to **books-svc** asking for details for the book given id.

## Running Service

Running `books_svc` as an example.

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
```

Test it out.

```
$ curl http://localhost:8080/api/v1/book/1 | jq

{
  "book_id": "1",
  "title": "An Absolutely Remarkable Thing",
  "auther": "Hank Green",
  "published_date": "September 25th, 2018"
}
```

### Running service with docker run

Make sure you have docker running. Build the container and run it.

```
docker build ./ -t books
docker run -d -it -p 3000:8080 books
```

Test it out.

```
$ curl http://localhost:3000/api/v1/book/1 | jq

{
  "book_id": "1",
  "title": "An Absolutely Remarkable Thing",
  "auther": "Hank Green",
  "published_date": "September 25th, 2018"
}
```

### Running service inside minikube

A bit more complicated. Please refer to the blog for details.

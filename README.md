# Kubernetes Microservices Demo

Three simple services:

**auth_svc**

It will check the authorization token. If it matches `SUPERSECUREAUTTHTOKEN`, then returns `{ok: true}`; otherwise, return `{ok: false}`.

**books_svc**

To keep things simple, we don't really have a database of books. For whatever request that is made to the `/api/v1/book/:id` endpoint, we always return the same book An Absolutely Remarkable Thing by Hank Green.

**gatewa_svc**

It has two endpoints `api/v1/hey` and `/api/v1/book/:id`. `api/v1/hey` will simply response "Hello World". `/api/v1/book/:id` will first authenticate the request by sending a request to **auth-svc**. If it is authenticated, it will then make a request to **books-svc** asking for details for the book given id.

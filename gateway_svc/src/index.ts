import express from 'express';
import * as request from "request-promise";

const app = express();
const port = 8080;

app.get('/api/v1/hey', (req, res) => res.send('Hello World!'));

app.get("/api/v1/book/:id", async (req, res) => {
  const headers = { headers: { authorization: req.get('authorization') } }
  request.get('http://auth-service.default.svc.cluster.local', headers)
    .then((data) => {
      request.get(`http://books-service.default.svc.cluster.local/api/v1/book/${req.params.id}`)
        .then(book => {
          res.send(book)
        }).catch(err => {
          res.status(401).send("Book not found")
        })
    }).catch(err => {
      res.status(403).send("Access denied")
    })
})


app.listen(port, () => console.log(`Listening on port ${port}!`));
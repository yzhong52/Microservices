import requests
from flask import Flask, request, Response

app = Flask(__name__)

auth_url = "http://auth-service.default.svc.cluster.local"
books_url = "http://books-service.default.svc.cluster.local"


@app.route('/api/v1/book/<book_id>')
def gateway(book_id):
    token = request.headers['Authorization']
    auth = requests.get(auth_url, headers={'Authorization': token})
    if auth.ok:
        book_response = requests.get(f"${books_url}/api/v1/book/${book_id}")
        return book_response.content, book_response.status_code, book_response.headers.items()
    else:
        return Response('Access denied', 401)


if __name__ == "__main__":
    app.run(debug=False)

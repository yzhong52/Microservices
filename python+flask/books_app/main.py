from flask import Flask, jsonify

app = Flask(__name__)


@app.route('/api/v1/book/<book_id>')
def book(book_id):
    return jsonify({
        "book_id": book_id,
        "title": "An Absolutely Remarkable Thing",
        "author": "Hank Green",
        "published_date": "September 25th, 2018"
    })


if __name__ == "__main__":
    app.run(debug=False)

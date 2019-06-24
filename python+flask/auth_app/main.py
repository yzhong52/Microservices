from flask import Flask, jsonify, request

app = Flask(__name__)


@app.route('/')
def auth():
    token = request.headers['Authorization']
    if token == "SUPERSECUREAUTTHTOKEN":
        return jsonify({"ok": True})
    else:
        return jsonify({"ok": False})


if __name__ == "__main__":
    app.run(debug=False)

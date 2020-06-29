cd ../python+flask

docker build ./gateway_app -t gateway
docker build ./auth_app -t auth
docker build ./books_app -t books

cd -

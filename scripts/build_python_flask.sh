cd ../python+flask

docker build ./gateway_svc -t gateway
docker build ./auth_svc -t auth
docker build ./books_svc -t books

cd -

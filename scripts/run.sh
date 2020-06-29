set -e

# # Start minikube cluster
# minikube start

# # Check nodes
# kubectl get nodes

# # Use the docker deamon with minikube
# eval $(minikube docker-env)

deploy_all() {
    cd ../ops

    kubectl create -f auth-deployment.yaml
    kubectl create -f auth-service.yaml
    kubectl create -f books-deployment.yaml
    kubectl create -f books-service.yaml
    kubectl create -f gateway-deployment.yaml
    kubectl create -f gateway-service.yaml
    
    cd -
}

cd ../scripts

build_images_scripts=(
    "build_typescript_express"
    "build_python_flask"
    "build_scala_finch"
)

mkdir -p output

function compare() {
    file1=$1
    file2=$2
    difference=(diff file1 file2)
    
    if [ -n "$difference" ]; then
      echo "File '$file1' and '$file2' are identical."
    else
      echo $difference
      exit 1
    fi
}

for build_images in ${build_images_scripts[@]}
do
    echo "\nBuilding iamge vith ${build_images}.sh...\n"
    sleep 5

    sh ${build_images}.sh

    kubectl delete --all deployments
    kubectl delete --all services

    deploy_all

    echo "\nWaiting for services to start...\n"
    sleep 20

    curl $(minikube service books-service --url)/api/v1/book/1 \
        | jq '.' > output/books.json
    compare output/books.json spec/books.json

    curl $(minikube service gateway-service --url)/api/v1/book/1 \
        -H "authorization: SUPERSECUREAUTTHTOKEN" \
        | jq '.' > output/gateway.json
    compare output/gateway.json spec/gateway.json

    curl $(minikube service auth-service --url) \
        -H "authorization: SUPERSECUREAUTTHTOKEN" \
        | jq '.' > output/auth_success.json
    compare output/auth_success.json spec/auth_success.json

    curl $(minikube service auth-service --url) \
        -H "authorization: NOTAVALIDTOKEN" \
        | jq '.' > output/auth_fail.json
    compare output/auth_fail.json spec/auth_fail.json
done



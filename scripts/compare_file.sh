file1=$1
file2=$2

if diff $file1 $file2; then
    echo "File '$file1' and '$file2' are the same."
else
    echo "File '$file1' and '$file2' are different."
    exit 1
fi

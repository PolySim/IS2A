path=$1

echo path
if [ "$path" != "" ]; then
    php -S localhost:8000 -t ./"$path"
else
    php -S localhost:8000 -t .
fi
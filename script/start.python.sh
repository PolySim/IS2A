path=$1

if [ "$path" != "" ]; then
    python3 $path
else
  echo "No path provided"
fi
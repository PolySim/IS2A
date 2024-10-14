path=$1
filename=$(basename "$path")
filename_without_ext=$(basename "$path" .c)
directory=$(dirname "$path")

bin_dir="$directory/bin"
mkdir -p "$bin_dir"

if [ "$filename" != "" ]; then
  # compile c
  gcc -Wall "$path" -o "$bin_dir/$filename_without_ext"
  echo "build finish"
  ./"$bin_dir/$filename_without_ext"
else
  echo "No path provided"
fi
path=$1
filename=$(basename "$path")
filename_without_ext=$(basename "$path" .c)
directory=$(dirname "$path")
args=$2
shift 2  # Ignore les deux premiers arguments
for arg in "$@"; do
  args+=("$arg")
done

bin_dir="$directory/bin"
mkdir -p "$bin_dir"

if [ "$filename" != "" ]; then
  # compile c
  gcc -Wall "$path" -o "$bin_dir/$filename_without_ext"
  echo "build finish"
  ./"$bin_dir/$filename_without_ext" "${args[@]}"
else
  echo "No path provided"
fi
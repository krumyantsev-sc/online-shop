localization_path="$1"
output_type="$2"
out_path="$3"

bash ./bin/process_document.sh "$localization_path"
if [ $? -ne 0 ]; then
  echo "Ошибка при выполнении process_document.sh"
  exit 1
fi

rm -rf ./out/*
bash ./bin/divide.sh "$output_type" "$out_path"